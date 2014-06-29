package main.java.ui.GUI.menu;

import main.java.GameManager;
import logic.Game;
import persistence.exceptions.PersistenceEndpointIOException;
import main.java.ui.GUI.JGameManagerGUI;
import main.java.ui.GUI.utils.JExceptionDialog;

import javax.swing.*;
import java.util.List;

/**
 * Created by Stefan on 25.06.14.
 */
public class JGameLoadMenu extends JMenu{

    private final GameManager manager;
    public JGameLoadMenu(GameManager manager, JGameManagerGUI GameManagerGUI){
        super("Laden");
        this.manager = manager;

        List<Game> savedGames;
        try {
            savedGames = this.manager.getGameList();
        }catch (PersistenceEndpointIOException e){
            new JExceptionDialog(this,e);
            return;
        }

        //Add saved games to Meue
        for(Game savedGame : savedGames){
            JMenuItem item = new JGameLoadMenuItem(savedGame,GameManagerGUI);
            this.add(item);
        }
    }
}
