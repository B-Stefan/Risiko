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
 * Created by Stefan on 30.06.14.
 */
public class JGameLoadRunningGameMenu extends JMenu {

    private final IGameManager manager;

    public JGameLoadRunningGameMenu(IGameManager manager, JGameManagerGUI GameManagerGUI) throws RemoteException{
        super("Spiel beitreten");
        this.manager = manager;

        List<IGame> runningGames;
        try {
            runningGames = this.manager.getRunningGameList();
        }catch (PersistenceEndpointIOException e){
            new JExceptionDialog(this,e);
            return;
        }

        //Add saved games to Meue
        for(IGame runningGame : runningGames){
            JMenuItem item = new JGameLoadMenuItem(runningGame,GameManagerGUI);
            this.add(item);
        }
    }
}
