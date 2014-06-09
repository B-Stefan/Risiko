package main.java.ui.GUI;

import javax.swing.JFrame;
import javax.swing.JTextArea;

import main.java.logic.*;
import main.java.logic.data.*;
import main.java.logic.exceptions.GameNotStartedException;

public class JCurrentStateInfoGUI extends JFrame {
	private final Game game;
	private final Player player;
	private JTextArea context;
	
	
	public JCurrentStateInfoGUI(Game game, Player player) throws GameNotStartedException{
		this.context = new JTextArea("");
		this.context.setWrapStyleWord(true);
		this.context.setLineWrap(true);
		this.game = game;
		this.player = player;
		setContext();
	}
	
	private void setContext() throws GameNotStartedException{
		if(this.game.getCurrentGameState() == Game.gameStates.WAITING){
			this.context.setText("Das Spiel hat noch nicht gestartet");
		}else if(this.game.getCurrentGameState() == Game.gameStates.RUNNING){
			if(this.game.getCurrentRound().getCurrentTurn().getCurrentStep() == Turn.steps.DISTRIBUTE){
				String n = String.format(this.game.getCurrentRound().getCurrentTurn().toString() + "%n%nDu musst noch "+ this.game.getCurrentRound().getCurrentTurn().getNewArmysSize() + " Armeen verteilen");
				this.context.setText(n);
			}else {
				this.context.setText(this.game.getCurrentRound().getCurrentTurn().toString());
			}
		}else if(this.game.getCurrentGameState() == Game.gameStates.FINISHED){
			this.context.setText("Das Spiel wurde beendet");
		}
	}
	
	public void update() throws GameNotStartedException{
		setContext();
	}
	
	public JTextArea getContext(){
		return this.context;
	}
}
