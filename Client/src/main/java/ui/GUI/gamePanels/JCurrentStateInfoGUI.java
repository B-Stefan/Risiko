/*
 * RISIKO-JAVA - Game, Copyright 2014  Jennifer Theloy, Stefan Bieliauskas  -  All Rights Reserved.
 * Hochschule Bremen - University of Applied Sciences
 *
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 * Contact:
 *     Jennifer Theloy: jTheloy@stud.hs-bremen.de
 *     Stefan Bieliauskas: sBieliauskas@stud.hs-bremen.de
 *
 * Web:
 *     https://github.com/B-Stefan/Risiko
 *
 */



package ui.GUI.gamePanels;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

import javax.swing.*;

import interfaces.IGame;
import interfaces.IRound;
import interfaces.ITurn;
import interfaces.data.IPlayer;
import exceptions.*;
import ui.GUI.JGameGUI;
import ui.GUI.utils.JExceptionDialog;

public class JCurrentStateInfoGUI extends JPanel {
	private final IGame game;
    private final JGameGUI gameGUI;
	private final JTextArea stepInfo = new JTextArea("");
	private final JButton nextButton = new JButton("");
	private final IPlayer clientPlayer;

    private class UpdateActionListener implements ActionListener{

        /**
         * Invoked when an action occurs.
         *
         * @param event
         */
        @Override
        public void actionPerformed(ActionEvent event) {
            try {
                JCurrentStateInfoGUI.this.gameGUI.update();
            }catch (RemoteException | GameNotStartedException e){
                new JExceptionDialog(JCurrentStateInfoGUI.this,e);
            }

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
            IGame.gameStates currentGameState;
            try{
                currentGameState = JCurrentStateInfoGUI.this.game.getCurrentGameState();
            }catch (RemoteException e){
                new JExceptionDialog(JCurrentStateInfoGUI.this,e);
                return;
            }
            if(currentGameState != IGame.gameStates.WAITING){
                return;
            }
            try {
                JCurrentStateInfoGUI.this.game.onGameStart();
            }catch ( NotEnoughPlayerException | TooManyPlayerException | NotEnoughCountriesException | GameAllreadyStartedException | PlayerAlreadyHasAnOrderException | RemoteException e ){
                new JExceptionDialog(JCurrentStateInfoGUI.this,e);
                return;
            }
            try{
                JCurrentStateInfoGUI.this.update();
            }catch (RemoteException e){
                new JExceptionDialog(JCurrentStateInfoGUI.this,e);
            }

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
            IRound currentRound;
            //Holen der aktuellen Runde
            try {
                currentRound = JCurrentStateInfoGUI.this.game.getCurrentRound();
            }catch (GameNotStartedException | RemoteException e){
                return;
            }
            //Holen des Aktuellen Turns
            try {
                currentRound.setNextTurn(clientPlayer);
            }catch (ToManyNewArmysException | NotYourTurnException | TurnNotCompleteException | RemoteException e){
                new JExceptionDialog(JCurrentStateInfoGUI.this,e);
                return;
            }catch (RoundCompleteException unused){
                //Wenn Runde komplett erledigt, neue Runde
                try {
                    game.setNextRound();
                }catch (ToManyNewArmysException | RoundNotCompleteException | GameNotStartedException | GameIsCompletedException | RemoteException e){
                    new JExceptionDialog(JCurrentStateInfoGUI.this,e);
                    return;
                }
            }
            try {
                JCurrentStateInfoGUI.this.update();
            }catch (RemoteException e){
                new JExceptionDialog(JCurrentStateInfoGUI.this,e);
            }
        }
    }
	
	public JCurrentStateInfoGUI(final IGame game, final IPlayer player, final JGameGUI gameGUI, IPlayer cPlayer) throws RemoteException{
        super();
		//Konstruktor bearbeiten (Update entfehrnen)
		this.setLayout(new GridLayout(2, 1));
		this.clientPlayer = cPlayer;
		this.stepInfo.setWrapStyleWord(true);
		this.stepInfo.setLineWrap(true);
		this.game = game;
        this.gameGUI = gameGUI;
        this.nextButton.addActionListener(new UpdateActionListener());
        this.nextButton.addActionListener(new StartGameActionListener());
        this.nextButton.addActionListener(new NextTurnOrRoundActionListener());
		update();
	}
	
	public void update() throws RemoteException{
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
                    ITurn currentTurn= this.game.getCurrentRound().getCurrentTurn();
                    if (currentTurn != null ){
                        IPlayer currentPlayer = currentTurn.getPlayer();
                        switch (currentTurn.getCurrentStep()){
                            case DISTRIBUTE:
                                textAreaMsg = String.format(currentPlayer.toStringRemote() + " %n %n du musst noch " +currentTurn.getNewArmysSize() + " Einheiten verteilen.");break;
                            case FIGHT:
                                textAreaMsg = currentPlayer.toStringRemote() + "Du darfst angreifen ";break;
                            case MOVE:
                                textAreaMsg = currentPlayer.toStringRemote() + "du darfst nur noch Einheiten bewegen";break;
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
