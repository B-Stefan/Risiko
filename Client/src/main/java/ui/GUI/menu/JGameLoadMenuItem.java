package main.java.ui.GUI.menu;

import interfaces.IGame;
import main.java.ui.GUI.JGameManagerGUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Stefan on 25.06.14.
 */
public class JGameLoadMenuItem extends JMenuItem {
    private final  IGame game;
    private final  JGameManagerGUI gameManagerGUI;

    private class OnItemClickedListener implements ActionListener {

        /**
         * Invoked when an action occurs.
         *
         * @param event
         */
        @Override
        public void actionPerformed(ActionEvent event) {
            JGameLoadMenuItem.this.gameManagerGUI.openGameGUI(JGameLoadMenuItem.this.game);
        }
    }
    public JGameLoadMenuItem (IGame game, JGameManagerGUI gameManagerGUI){
        super(game.toString());
        this.gameManagerGUI = gameManagerGUI;
        this.game = game;
        this.addActionListener(new OnItemClickedListener());
    }
}
