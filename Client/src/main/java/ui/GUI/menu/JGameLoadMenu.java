package ui.GUI.menu;

import exceptions.PersistenceEndpointIOException;
import interfaces.IGame;
import interfaces.IGameManager;
import ui.GUI.JGameManagerGUI;
import ui.GUI.utils.JExceptionDialog;

import javax.swing.*;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Created by Stefan on 25.06.14.
 */
public class JGameLoadMenu extends JMenu{

    private final IGameManager manager;
    public JGameLoadMenu(IGameManager manager, JGameManagerGUI GameManagerGUI) throws RemoteException{
        super("Laden");
        this.manager = manager;

        List<IGame> savedGames;
        try {
            savedGames = this.manager.getGameList();
        }catch (PersistenceEndpointIOException e){
            new JExceptionDialog(this,e);
            return;
        }

        //Add saved games to Meue
        for(IGame savedGame : savedGames){
            JMenuItem item = new JGameLoadMenuItem(savedGame,GameManagerGUI);
            this.add(item);
        }
    }
}
