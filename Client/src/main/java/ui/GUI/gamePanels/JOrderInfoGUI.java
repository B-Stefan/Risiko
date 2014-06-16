package main.java.ui.GUI.gamePanels;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import main.java.logic.Game;
import main.java.logic.data.Player;
import main.java.logic.exceptions.GameNotStartedException;

public class JOrderInfoGUI extends JFrame {

	private JTextArea context;
	private final Player player;
	private final Game game;
	
	public JOrderInfoGUI(Game game, Player player){
		this.context = new JTextArea("");
		this.context.setWrapStyleWord(true);
		this.context.setLineWrap(true);
		this.player = player;
		this.game = game;
	}
	
	private void setContext() throws GameNotStartedException{
		if(this.game.getCurrentGameState() == Game.gameStates.RUNNING){
			this.context.setText(this.player.getOrder().toString());
		}
	}
	
	public void update() throws GameNotStartedException{
		setContext();
	}
	
	public JTextArea getContext(){
		return this.context;
	}
}
