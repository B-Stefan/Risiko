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

package Client.ui.GUI;

import commons.exceptions.*;
import commons.interfaces.IGame;
import commons.interfaces.IGameManager;
import commons.interfaces.data.IPlayer;
import server.logic.ClientEventProcessor;
import Client.ui.CUI.GameCUI;
import Client.ui.GUI.menu.JGameLoadRunningGameMenu;
import Client.ui.GUI.menu.JGameLoadSavedGameMenu;
import Client.ui.GUI.utils.JExceptionDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

/**
 *
 */
public class JGameManagerGUI extends JFrame {

    private final JTextField playerNameTxt = new JTextField("");
    private final JButton startGameBtn = new JButton("Spiel starten");
    private final IGameManager manager;
    private final ClientEventProcessor remoteEventProcessor;



    private class StartGameActionListener implements ActionListener {

        /**
         * Invoked when an action occurs.
         *
         * @param event
         */
        @Override
        public void actionPerformed(ActionEvent event) {
            try {
                JGameManagerGUI.this.openGameGUI();
            }catch (RemoteException | PersistenceEndpointIOException | GameNotFoundException e){
                new JExceptionDialog(JGameManagerGUI.this,e);
            }
        }
    }

    public JGameManagerGUI(IGameManager manager) throws RemoteException{
        super("Risiko-Spielerauswahl");
        this.manager = manager;
        this.remoteEventProcessor = new ClientEventProcessor();
        this.manager.getClientManager().addClient(this.remoteEventProcessor);
        initialize();
    }
    private void initialize() throws RemoteException {
        //this.setSize(600, 400);
        //this.setPreferredSize(this.getSize());

        // Klick auf Kreuz (Fenster schließen) behandeln lassen:
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout(10,10));

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(3,1,10,10));

        centerPanel.add(new JLabel("Spielername:"));
        centerPanel.add(this.playerNameTxt);
        centerPanel.add(this.startGameBtn);

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(new JGameLoadSavedGameMenu(this.manager,this));
        menuBar.add(new JGameLoadRunningGameMenu(this.manager,this,this.remoteEventProcessor));

        this.setJMenuBar(menuBar);

        this.add(centerPanel,BorderLayout.CENTER);

        this.startGameBtn.addActionListener(new StartGameActionListener());

        this.setPreferredSize(new Dimension(300,300));
        // Fenster anzeigen
        this.centreWindow();
        this.pack();
        this.setVisible(true);


    }
    private  void centreWindow( ) {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth()/2 - this.getWidth()) / 2);
        int y = (int) ((dimension.getHeight()/2 - this.getHeight()) / 2);
        this.setLocation(x, y);
    }
    public void openGameGUI() throws RemoteException, GameNotFoundException,PersistenceEndpointIOException{
        IGame game;
        try {
            game = manager.createGame();
        }catch (PersistenceEndpointIOException e){
            new JExceptionDialog(JGameManagerGUI.this,e);
            return;
        }
        openGameGUI(game);
    }
    public void openGameGUI(IGame game) throws RemoteException, GameNotFoundException, PersistenceEndpointIOException{


        String playerName = JGameManagerGUI.this.playerNameTxt.getText();
        if (playerName.length() < 4 ){
            new JExceptionDialog(JGameManagerGUI.this,"Bitte geben Sie einen Spielernamen ein, der mindestens 4 Zeichen lang ist");
            return;
        }

        IPlayer currentPlayer;
        ClientEventProcessor gameRemoteEventProcessor = new ClientEventProcessor(); // Empfängt die Broadcast und wird pro Spiel angelegt

        //Wenn Spiel geladen wird
        if (game.getCurrentGameState() == IGame.gameStates.RUNNING){
            try {
                currentPlayer = game.getPlayer(playerName);
                game.setClient(currentPlayer, gameRemoteEventProcessor);
            }catch (PlayerNotExistInGameException e){
                new JExceptionDialog(this,e);
                return;
            }
            game = manager.addGame(game); // Prüft, ob es sich um ein richtiges Game handelt und gibt das gameObject des Server zurück, außerdem fügt wird das Game der runnig gamelist hinzugefügt
        }
        else{
            //Neues Spiel
            try {
                currentPlayer = game.addPlayer(playerName,gameRemoteEventProcessor);
            }catch (PlayerNameAlreadyChooseException | GameAlreadyStartedException e){
                new JExceptionDialog(this,e);
                return;
            }
        }

        /**
         * GUI UND CUI Erstellen
         */
        JGameGUI    gui = new JGameGUI(game,currentPlayer,gameRemoteEventProcessor);
        GameCUI     cui = new GameCUI(game, null,currentPlayer);

        Thread t = new Thread(cui);
        t.start();

        this.manager.getClientManager().removeClient(this.remoteEventProcessor);
        JGameManagerGUI.this.setVisible(false);
        JGameManagerGUI.this.dispose();
    }
}
