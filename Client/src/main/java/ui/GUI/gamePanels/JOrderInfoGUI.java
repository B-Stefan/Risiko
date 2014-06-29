package main.java.ui.GUI.gamePanels;

import javax.swing.*;

import interfaces.IGame;
import interfaces.data.IPlayer;

public class JOrderInfoGUI extends JTextArea {

	private final IPlayer player;
	private final IGame game;
	
	public JOrderInfoGUI(IGame game, IPlayer player){
		super("");
		this.setWrapStyleWord(true);
		this.setLineWrap(true);
		this.player = player;
		this.game = game;
	}
	
	private void setContext(){
		if(this.game.getCurrentGameState() == IGame.gameStates.RUNNING){

                this.setText(this.player.getOrder().toString());
		}
	}
	
	public void update(){
		setContext();
	}

}
