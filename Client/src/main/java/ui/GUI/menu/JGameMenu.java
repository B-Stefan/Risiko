package ui.GUI.menu;

import exceptions.PersistenceEndpointIOException;
import interfaces.IGame;
import ui.GUI.JGameGUI;
import ui.GUI.utils.JExceptionDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

/**
 * Created by Stefan on 25.06.14.
 */
public class JGameMenu extends JMenu{

    private final IGame game;
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
            }catch (PersistenceEndpointIOException | RemoteException e){
                new JExceptionDialog(JGameMenu.this,e);
                return;
            }
        }
    }
    public JGameMenu(IGame game){
        super("Spiel");
        this.game = game;
        this.saveMenuItem = new JMenuItem("Speichern");
        this.saveMenuItem.addActionListener(new SaveGameListener());
        this.add(this.saveMenuItem);

    }

}
