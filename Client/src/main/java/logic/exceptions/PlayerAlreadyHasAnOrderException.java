package main.java.logic.exceptions;
import main.java.logic.data.Player;

/**
 * Created by Stefan on 06.05.14.
 */
public class PlayerAlreadyHasAnOrderException extends Exception {
    public PlayerAlreadyHasAnOrderException (Player player){
        super("Der Spieler " + player + " hat bereits eine Order.");
    }
}
