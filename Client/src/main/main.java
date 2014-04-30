package main;
import main.java.logic.Game;
import main.java.gui.CUI.GameCUI;

import main.java.logic.Player;
import java.lang.*;

/**
 * Created by Stefan on 01.04.2014.
 */
public class main {

    public static void main(String[] args) throws Exception{

        //Erstellen des Spiels
        Game game = new Game();
        //Erstellen des UI
        GameCUI ui = new GameCUI(game);

        game.addPlayer(new Player("Bon"));
        game.addPlayer(new Player("Stefan"));
        game.addPlayer(new Player("Linda"));
        //Startet das warten auf eine Eingbae
        ui.listenConsole();
    }
}
