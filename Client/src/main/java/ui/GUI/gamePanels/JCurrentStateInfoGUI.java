package main.java.ui.GUI.gamePanels;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.*;

import main.java.logic.*;
import main.java.logic.data.*;
import main.java.logic.exceptions.*;
import main.java.ui.GUI.JGameGUI;
import main.java.ui.GUI.utils.JExceptionDialog;

public class JCurrentStateInfoGUI extends JPanel {
	private final Game game;
    private final JGameGUI gameGUI;
	private final JTextArea stepInfo = new JTextArea("");
	private final JButton nextButton = new JButton("");

    private class UpdateActionListener implements ActionListener{

        /**
         * Invoked when an action occurs.
         *
         * @param e
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            JCurrentStateInfoGUI.this.gameGUI.update();
        }
    }

    private class StartGameActionListener implements ActionListener{

        /**
         * Invoked when an action occurs.
         *
         * @param event
         */
        @Override
        public void actionPerformed(ActionEvent event) {
            if(JCurrentStateInfoGUI.this.game.getCurrentGameState() != Game.gameStates.WAITING){
                return;
            }
            try {
                JCurrentStateInfoGUI.this.game.onGameStart();
            }catch ( NotEnoughPlayerException | TooManyPlayerException | NotEnoughCountriesException | GameAllreadyStartedException | PlayerAlreadyHasAnOrderException e ){
                new JExceptionDialog(JCurrentStateInfoGUI.this,e);
                return;
            }
            JCurrentStateInfoGUI.this.update();
        }
    }


    private class NextTurnOrRoundActionListener implements ActionListener{

        /**
         * Methode, die das Spiel in den nächsten Set oder Turn versetzt
         *
         * @param event
         */
        @Override
        public void actionPerformed(ActionEvent event) {
            Round currentRound;
            //Holen der aktuellen Runde
            try {
                currentRound = JCurrentStateInfoGUI.this.game.getCurrentRound();
            }catch (GameNotStartedException e){
                return;
            }
            //Holen des Aktuellen Turns
            try {
                currentRound.setNextTurn();
            }catch (ToManyNewArmysException | TurnNotCompleteException e){
                new JExceptionDialog(JCurrentStateInfoGUI.this,e);
                return;
            }catch (RoundCompleteException unused){
                //Wenn Runde komplett erledigt, neue Runde
                try {
                    game.setNextRound();
                }catch (ToManyNewArmysException | RoundNotCompleteException | GameNotStartedException | GameIsCompletedException e){
                    new JExceptionDialog(JCurrentStateInfoGUI.this,e);
                    return;
                }
            }
            JCurrentStateInfoGUI.this.update();
        }
    }
	
	public JCurrentStateInfoGUI(final Game game, final Player player, final JGameGUI gameGUI){
		//Konstruktor bearbeiten (Update entfehrnen)
		this.setLayout(new GridLayout(2, 1));
		this.stepInfo.setWrapStyleWord(true);
		this.stepInfo.setLineWrap(true);
		this.game = game;
        this.gameGUI = gameGUI;
        this.nextButton.addActionListener(new UpdateActionListener());
        this.nextButton.addActionListener(new StartGameActionListener());
        this.nextButton.addActionListener(new NextTurnOrRoundActionListener());
		update();
	}
	
	public void update() {
        String textAreaMsg = "";
        String btnMsg = "";
        switch (this.game.getCurrentGameState()){
            case WAITING:
                textAreaMsg = "Spiel nicht gestartet";
                btnMsg = "Spiel starten";
                break;
            case RUNNING:
                btnMsg = "Nächster Spieler";
                try{
                    Turn currentTurn= this.game.getCurrentRound().getCurrentTurn();
                    if (currentTurn != null ){
                        Player currentPlayer = currentTurn.getPlayer();
                        switch (currentTurn.getCurrentStep()){
                            case DISTRIBUTE:
                                textAreaMsg = String.format(currentPlayer + " %n %n du musst noch " +currentTurn.getNewArmysSize() + " Einheiten verteilen.");break;
                            case FIGHT:
                                textAreaMsg = currentPlayer + "Du darfst angreifen ";break;
                            case MOVE:
                                textAreaMsg = currentPlayer + "du darfst nur noch Einheiten bewegen";break;
                        }
                    }
                }catch (GameNotStartedException e) {
                    throw new RuntimeException(e);
                }
            break;
            case FINISHED: textAreaMsg = "Das Spiel wurde beendet";

        }
		this.stepInfo.setText(textAreaMsg);
        this.nextButton.setText(btnMsg);
		this.add(this.stepInfo);
		this.add(this.nextButton);

	}


}
