package ui.GUI.gamePanels;

import java.awt.GridLayout;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import interfaces.data.IPlayer;
import interfaces.data.cards.ICard;

public class JCardInfo extends JFrame{
	private final IPlayer player;
	private JTextArea cardInfo = new JTextArea("");
	private JPanel context;
	private JButton exchange;
	
	public JCardInfo(IPlayer player) throws RemoteException{
		this.player = player;
		this.context = new JPanel();
		this.context.setLayout(new GridLayout(2,1));
		this.cardInfo.setWrapStyleWord(true);
		this.cardInfo.setLineWrap(true);
		this.exchange = new JButton("eintauschen");
		setContext();
	}
	
	private void setContext() throws RemoteException{
		update();
		setExchange();
		this.context.add(this.cardInfo);
		this.context.add(this.exchange);
	}
	
	public void update() throws RemoteException{
		List<? extends ICard> cards = this.player.getCards();

        String msg = "";
        Iterator<? extends ICard> iter = cards.iterator();
        while (iter.hasNext()){
            ICard currentCard = iter.next();
            msg += String.format(currentCard.toStringRemote() + "%n");
        }

		this.cardInfo.setText(msg);
	}
	
	private void setExchange(){
	}
	
	public JPanel getContext(){
		return this.context;
	}
}
