package main.java.ui.GUI;

import main.java.GameManager;
import logic.Game;
import logic.data.Player;
import persistence.exceptions.PersistenceEndpointIOException;
import main.java.ui.CUI.GameCUI;
import main.java.ui.GUI.menu.JGameLoadMenu;
import main.java.ui.GUI.utils.JExceptionDialog;
import main.java.ui.GUI.utils.JModalDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Stefan on 23.06.14.
 */
public class JGameManagerGUI extends JFrame {

    private final JTextField playerNameTxt = new JTextField("");
    private final JButton startGameBtn = new JButton("Spiel starten");
    private final GameManager manager;



    private class StartGameActionListener implements ActionListener {

        /**
         * Invoked when an action occurs.
         *
         * @param event
         */
        @Override
        public void actionPerformed(ActionEvent event) {
            JGameManagerGUI.this.openGameGUI();
        }
    }

    public JGameManagerGUI(GameManager manager){
        this.manager = manager;
        initialize();
    }
    private void initialize()  {
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
    public void openGameGUI(){
        Game game;
        try {
            game = manager.addGame();
        }catch (PersistenceEndpointIOException e){
            new JExceptionDialog(JGameManagerGUI.this,e);
            return;
        }
        openGameGUI(game);
    }
    public void openGameGUI(Game game){


        String playerName = JGameManagerGUI.this.playerNameTxt.getText();
        if (playerName.length() < 4 ){
            playerName = JModalDialog.showInputDialog(this,"Bitte geben Sie einen Spielernamen mit mindestens 4 Buchstaben ein", "Spielername");
            JGameManagerGUI.this.playerNameTxt.setText(playerName);
        }


        Player currentPlayer = new Player(playerName);
        game.addPlayer(currentPlayer);
        JGameGUI    gui = new JGameGUI(game,currentPlayer);
        GameCUI     cui = new GameCUI(game, null);

        Thread t = new Thread(cui);
        t.start();

        JGameManagerGUI.this.setVisible(false);
        JGameManagerGUI.this.dispose();
    }
}
