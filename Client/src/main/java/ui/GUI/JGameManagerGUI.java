package ui.GUI;

import exceptions.GameAllreadyStartedException;
import exceptions.PersistenceEndpointIOException;
import exceptions.PlayerNameAlreadyChooseException;
import exceptions.PlayerNotExsistInGameException;
import interfaces.IClient;
import interfaces.IGame;
import interfaces.IGameManager;
import interfaces.data.IPlayer;
import server.logic.ClientEventProcessor;
import ui.CUI.GameCUI;
import ui.GUI.menu.JGameLoadRunningGameMenu;
import ui.GUI.menu.JGameLoadSavedGameMenu;
import ui.GUI.utils.JExceptionDialog;
import ui.GUI.utils.JModalDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

/**
 * Created by Stefan on 23.06.14.
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
            }catch (RemoteException e){
                new JExceptionDialog(JGameManagerGUI.this,e);
            }
        }
    }

    public JGameManagerGUI(IGameManager manager) throws RemoteException{
        super("Risiko-Spielerauswahl");
        this.manager = manager;
        this.remoteEventProcessor = new ClientEventProcessor();
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
        menuBar.add(new JGameLoadRunningGameMenu(this.manager,this));
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
            new JExceptionDialog(JGameManagerGUI.this,"Bitte geben Sie einen Spielernamen ein, der mindestens 4 Zeichen lang ist");
            return;
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
                currentPlayer = game.addPlayer(playerName,this.remoteEventProcessor);
            }catch (PlayerNameAlreadyChooseException | GameAllreadyStartedException e){
                new JExceptionDialog(this,e);
                return;
            }
        }
        JGameGUI    gui = new JGameGUI(game,currentPlayer,this.remoteEventProcessor);
        GameCUI     cui = new GameCUI(game, null,currentPlayer);

        Thread t = new Thread(cui);
        t.start();

        JGameManagerGUI.this.setVisible(false);
        JGameManagerGUI.this.dispose();
    }
}
