package main.java.ui.GUI.gamePanels;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import main.java.logic.*;
import main.java.logic.data.*;
import main.java.logic.exceptions.GameNotStartedException;

public class JCurrentStateInfoGUI extends JFrame {
	private final Game game;
	private final Player player;
	private JTextArea stepInfo = new JTextArea("");
	private JPanel context;
	private JButton nextStep;
	
	
	public JCurrentStateInfoGUI(Game game, Player player, JButton Update){
		//Konstruktor bearbeiten (Update entfehrnen)
		this.context = new JPanel();
		this.context.setLayout(new GridLayout(2,1));
		this.stepInfo.setWrapStyleWord(true);
		this.stepInfo.setLineWrap(true);
		this.game = game;
		this.player = player;
		this.nextStep = Update;
		//this.nextStep = new JButton("nächster Zug");
		setContext();
	}
	
	private void setContext() {
		if(this.game.getCurrentGameState() == Game.gameStates.WAITING){
			this.stepInfo.setText("Das Spiel hat noch nicht gestartet");
		}else if(this.game.getCurrentGameState() == Game.gameStates.RUNNING){
            try {
                if(this.game.getCurrentRound().getCurrentTurn().getCurrentStep() == Turn.steps.DISTRIBUTE){
                    String n = String.format(this.game.getCurrentRound().getCurrentTurn().toString() + "%n%nDu musst noch "+ this.game.getCurrentRound().getCurrentTurn().getNewArmysSize() + " Armeen verteilen");
                    this.stepInfo.setText(n);
                }else {
                    this.stepInfo.setText(this.game.getCurrentRound().getCurrentTurn().toString());
                }
            }catch (GameNotStartedException e){
                this.stepInfo.setText("Spiel nicht gestartet");
            }

		}else if(this.game.getCurrentGameState() == Game.gameStates.FINISHED){
			this.stepInfo.setText("Das Spiel wurde beendet");
		}
		this.context.add(this.stepInfo);
		this.context.add(this.nextStep);
		
		this.nextStep.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent ae) {
            //Hinzufügen Funktion
            }
		});
	}
	
	public void update() {
		setContext();
	}
	
	public JPanel getContext(){
		return this.context;
	}
}
