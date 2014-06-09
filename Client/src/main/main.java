package main;
import main.java.GameManager;
import main.java.gui.CUI.GameManagerCUI;
import main.java.gui.GUI.GameGUI;
import main.java.persistence.PersistenceManager;
import main.java.persistence.dataendpoints.PersistenceEndpoint;
import main.java.logic.Game;
import main.java.logic.data.Player;

import java.lang.*;

/**
 * Hauptklasse, die zum Starten des Spiels verwendet wird.
 *
 */
public class main {

    /**
     * Main Anweiseung
     * @param args - Keine
     * @throws Exception
     */
    public static void main(String[] args) throws Exception{


        PersistenceManager persistenceManager = new PersistenceManager();
        //Erstellen des Spiels
        GameManager  gameManager = new GameManager(persistenceManager);
        //Erstellen des UI
        // GameManagerCUI ui = new GameManagerCUI(game);
        Game game = new Game();
        
        GameGUI gui = new GameGUI(game);
        
        //ui.listenConsole();
    }
}
