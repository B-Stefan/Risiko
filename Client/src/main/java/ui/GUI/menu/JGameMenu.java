package main.java.ui.GUI.menu;

import logic.Game;
import persistence.exceptions.PersistenceEndpointIOException;
import main.java.ui.GUI.JGameGUI;
import main.java.ui.GUI.utils.JExceptionDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Stefan on 25.06.14.
 */
public class JGameMenu extends JMenu{

    private final Game game;
    private final JMenuItem saveMenuItem;

    private class SaveGameListener implements ActionListener {
        /**
         * Invoked when an action occurs.
         *
         * @param event
         */
        @Override
        public void actionPerformed(ActionEvent event) {

            JGameGUI currentGameGUI =  (JGameGUI) SwingUtilities.getRoot(JGameMenu.this);
            if(currentGameGUI == null){
                throw new RuntimeException("Kann nur in Verbindung mit einer GAMGE GUI als RootFrame benutzt werden");
            }
            try {
                JGameMenu.this.game.save();
            }catch (PersistenceEndpointIOException e){
                new JExceptionDialog(JGameMenu.this,e);
                return;
            }
        }
    }
    public JGameMenu(Game game){
        super("Spiel");
        this.game = game;
        this.saveMenuItem = new JMenuItem("Speichern");
        this.saveMenuItem.addActionListener(new SaveGameListener());
        this.add(this.saveMenuItem);

    }

}