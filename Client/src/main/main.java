package main;
import main.java.logic.Game;
import main.java.gui.GameCUI;
import java.lang.*;
/**
 * Created by Stefan on 01.04.2014.
 */
public class Main {

    public static void main(String[] args) throws Exception{

        //Erstellen des Spiels
        Game game = new Game();
        //Erstellen des UI
        GameCUI ui = new GameCUI(game);

        //Startet das warten auf eine Eingbae
        ui.init();
    }
}
