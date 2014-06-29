package main.java.ui.GUI.gamePanels;

import java.awt.GridLayout;
import java.util.Iterator;
import java.util.Stack;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import logic.Game;
import logic.data.Player;
import logic.data.cards.Card;

public class JCardInfo extends JFrame{
	private final Player player;
	private JTextArea cardInfo = new JTextArea("");
	private JPanel context;
	private JButton exchange;
	
	public JCardInfo(Player player){
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
		Stack<Card> cards = this.player.getCards();

        String msg = "";
        Iterator<Card> iter = cards.iterator();
        while (iter.hasNext()){
            Card currentCard = iter.next();
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
