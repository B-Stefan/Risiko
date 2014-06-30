package ui.GUI;

import exceptions.CountryNotInListException;
import exceptions.PersistenceEndpointIOException;
import exceptions.PlayerNameAlreadyChooseException;
import exceptions.PlayerNotExsistInGameException;
import interfaces.IGame;
import interfaces.IGameManager;
import interfaces.data.ICountry;
import interfaces.data.IPlayer;
import interfaces.data.Orders.IOrder;
import interfaces.data.cards.ICard;
import ui.CUI.GameCUI;
import ui.GUI.menu.JGameLoadMenu;
import ui.GUI.utils.JExceptionDialog;
import ui.GUI.utils.JModalDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Stack;
import java.util.UUID;

/**
 * Created by Stefan on 23.06.14.
 */
public class JGameManagerGUI extends JFrame {

    private final JTextField playerNameTxt = new JTextField("");
    private final JButton startGameBtn = new JButton("Spiel starten");
    private final IGameManager manager;



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
            }catch (RemoteException e){
                new JExceptionDialog(JGameManagerGUI.this,e);
            }
        }
    }

    public JGameManagerGUI(IGameManager manager) throws RemoteException{
        this.manager = manager;
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
        menuBar.add(new JGameLoadMenu(this.manager,this));
        this.setJMenuBar(menuBar);

        this.add(centerPanel,BorderLayout.CENTER);

        this.startGameBtn.addActionListener(new StartGameActionListener());

        this.setPreferredSize(new Dimension(300,300));
        // Fenster anzeigen
        this.centreWindow();
        this.setVisible(true);
        this.pack();

    }
    private  void centreWindow( ) {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth()/2 - this.getWidth()) / 2);
        int y = (int) ((dimension.getHeight()/2 - this.getHeight()) / 2);
        this.setLocation(x, y);
    }
    public void openGameGUI() throws RemoteException{
        IGame game;
        try {
            game = manager.addGame();
        }catch (PersistenceEndpointIOException e){
            new JExceptionDialog(JGameManagerGUI.this,e);
            return;
        }
        openGameGUI(game);
    }
    public void openGameGUI(IGame game) throws RemoteException{


        String playerName = JGameManagerGUI.this.playerNameTxt.getText();
        if (playerName.length() < 4 ){
            playerName = JModalDialog.showInputDialog(this,"Bitte geben Sie einen Spielernamen mit mindestens 4 Buchstaben ein", "Spielername");
            JGameManagerGUI.this.playerNameTxt.setText(playerName);
        }


        IPlayer currentPlayer;


        if (game.getCurrentGameState() == IGame.gameStates.RUNNING){
            //Wenn Spiel geladen wird
            try {
                currentPlayer = game.getPlayer(playerName);
            }catch (PlayerNotExsistInGameException e){
                new JExceptionDialog(this,e);
                return;
            }
        }
        else{
            //Neues Spiel
            try {
                currentPlayer = game.addPlayer(playerName);
            }catch (PlayerNameAlreadyChooseException e){
                new JExceptionDialog(this,e);
                return;
            }
        }
        JGameGUI    gui = new JGameGUI(game,currentPlayer);
        GameCUI     cui = new GameCUI(game, null);

        Thread t = new Thread(cui);
        t.start();

        JGameManagerGUI.this.setVisible(false);
        JGameManagerGUI.this.dispose();
    }
}
