package main.java.logic.exceptions;

/**
 * Created by Stefan on 29.04.14.
 */
public class GameIsCompletedException extends Exception {
    public GameIsCompletedException(){
        super ("Ein Spieler hat das Spiel erfolgreich beendet ");
    }
}
