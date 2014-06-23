package main.java.ui.GUI;

import main.java.logic.Game;
import main.java.logic.data.Player;
import main.java.logic.exceptions.GameNotStartedException;
import main.java.ui.CUI.GameCUI;
import main.java.ui.GUI.utils.JExceptionDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Stefan on 23.06.14.
 */
public class JPlayerManager extends JFrame {

    private JTextField playerNameTxt = new JTextField("");
    private JButton startGameBtn = new JButton("Spiel starten");


    private class StartGameActionListener implements ActionListener {

        /**
         * Invoked when an action occurs.
         *
         * @param e
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            if (JPlayerManager.this.playerNameTxt.getText().length() < 4 ){
                new JExceptionDialog(JPlayerManager.this,"Bitte geben Sie als Spielernamen mindestens 4 zeichen an ");
                return;
            }
            Player currentPlayer = new Player(JPlayerManager.this.playerNameTxt.getText());
            Game game = new Game();
            game.addPlayer(currentPlayer);

            JGameGUI    gui = new JGameGUI(game,currentPlayer);
            GameCUI     cui = new GameCUI(game, null);

            Thread t = new Thread(cui);
            t.start();

            JPlayerManager.this.setVisible(false);
            JPlayerManager.this.dispose();

        }
    }

    public JPlayerManager (){
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
}
