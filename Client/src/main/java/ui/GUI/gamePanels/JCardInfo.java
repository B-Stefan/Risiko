package main.java.ui.GUI.gamePanels;

import java.awt.GridLayout;
import java.util.Collections;
import java.util.Stack;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import main.java.logic.Game;
import main.java.logic.data.Player;

public class JCardInfo extends JFrame{
	private final Player player;
	private JTextArea cardInfo = new JTextArea("");
	private JPanel context;
	private JButton redeem;
	
	public JCardInfo(Player player){
		this.player = player;
		this.context = new JPanel();
		this.context.setLayout(new GridLayout(2,1));
		this.cardInfo.setWrapStyleWord(true);
		this.cardInfo.setLineWrap(true);
		this.redeem = new JButton("eintauschen");
		setContext();
	}
	
	private void setContext(){
		setCardInfo();
		setRedeem();
		this.context.add(this.cardInfo);
		this.context.add(this.redeem);
	}
	
	private void setCardInfo(){
		Stack<String> cards = this.player.getCards();
		String info = "";
		String old = cards.peek();
		Collections.reverse(cards);
		for(String n : cards){
			if(!old.equals(n)){
				info = String.format(info + "%n");
			}
			info = info + n + " ";
			old = n;
		}
		this.cardInfo.setText(info);
	}
	
	private void setRedeem(){
	}
	
	public JPanel getContext(){
		return this.context;
	}
}
