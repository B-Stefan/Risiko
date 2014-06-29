package main.java.ui.GUI.gamePanels;

import java.awt.GridLayout;
import java.util.Iterator;
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
	
	public JCardInfo(IPlayer player){
		this.player = player;
		this.context = new JPanel();
		this.context.setLayout(new GridLayout(2,1));
		this.cardInfo.setWrapStyleWord(true);
		this.cardInfo.setLineWrap(true);
		this.exchange = new JButton("eintauschen");
		setContext();
	}
	
	private void setContext(){
		update();
		setExchange();
		this.context.add(this.cardInfo);
		this.context.add(this.exchange);
	}
	
	public void update(){
		Stack<ICard> cards = this.player.getCards();

        String msg = "";
        Iterator<ICard> iter = cards.iterator();
        while (iter.hasNext()){
            ICard currentCard = iter.next();
            msg += String.format(currentCard + "%n");
        }

		this.cardInfo.setText(msg);
	}
	
	private void setExchange(){
	}
	
	public JPanel getContext(){
		return this.context;
	}
}
