package ui.GUI.menu;

import interfaces.IGame;
import ui.GUI.JGameManagerGUI;
import ui.GUI.utils.JExceptionDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

/**
 * Created by Stefan on 25.06.14.
 */
public class JGameLoadMenuItem extends JMenuItem {
    private final  IGame game;
    private final JGameManagerGUI gameManagerGUI;

    private class OnItemClickedListener implements ActionListener {

        /**
         * Invoked when an action occurs.
         *
         * @param event
         */
        @Override
        public void actionPerformed(ActionEvent event) {
            try {
                JGameLoadMenuItem.this.gameManagerGUI.openGameGUI(JGameLoadMenuItem.this.game);
            }catch (RemoteException e){
                new JExceptionDialog(JGameLoadMenuItem.this,e);
            }
        }
    }
    public JGameLoadMenuItem (IGame game, JGameManagerGUI gameManagerGUI){
        super(game.toString());
        this.gameManagerGUI = gameManagerGUI;
        this.game = game;
        this.addActionListener(new OnItemClickedListener());
    }
}
