package main;
import main.java.GameManager;
import main.java.ui.CUI.GameCUI;
import main.java.ui.GUI.JGameGUI;
import main.java.persistence.PersistenceManager;
import main.java.logic.Game;
import main.java.logic.data.Player;
import main.java.ui.GUI.JPlayerManager;

import java.awt.Color;
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
        // GameManagerCUI ui = new GameManagerCUI(gamePanels);


        new JPlayerManager();

    }
}
