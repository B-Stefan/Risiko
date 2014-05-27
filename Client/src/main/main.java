package main;
import main.java.logic.Game;
import main.java.gui.CUI.GameCUI;

import main.java.logic.Player;
import main.java.persistence.PersistenceManager;
import main.java.persistence.dataendpoints.PersistenceEndpoint;
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

        //Erstellen des Spiels
        Game game = new Game();
        //Erstellen des UI
        GameCUI ui = new GameCUI(game);

        game.addPlayer(new Player("Bob"));
        game.addPlayer(new Player("Stefan"));
        game.addPlayer(new Player("Linda"));


        /**
         * Testen der Persistenz
         */
        //PersistenceEndpoint endpoint = new PersistenceManager().createHandler(game.getClass());
        //endpoint.save(game);

        //Startet das warten auf eine Eingbae des Benutzers
        ui.listenConsole();
    }
}
