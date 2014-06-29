package exceptions;
import interfaces.data.IPlayer;

/**
 * Created by Stefan on 06.05.14.
 */
public class PlayerAlreadyHasAnOrderException extends Exception {
    public PlayerAlreadyHasAnOrderException (IPlayer player){
        super("Der Spieler " + player + " hat bereits eine Order.");
    }
}
