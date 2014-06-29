package main.java.ui.GUI.gamePanels;

import javax.swing.*;

import main.java.logic.Game;
import main.java.logic.data.Player;
import main.java.logic.exceptions.GameNotStartedException;

public class JOrderInfoGUI extends JTextArea {

	private final Player player;
	private final Game game;
	
	public JOrderInfoGUI(Game game, Player player){
		super("");
		this.setWrapStyleWord(true);
		this.setLineWrap(true);
		this.player = player;
		this.game = game;
	}
	
	private void setContext(){
		if(this.game.getCurrentGameState() == Game.gameStates.RUNNING){

                this.setText(this.player.getOrder().toString());
		}
	}
	
	public void update(){
		setContext();
	}

}
