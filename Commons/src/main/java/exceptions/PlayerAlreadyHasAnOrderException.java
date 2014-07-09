package exceptions;
import interfaces.data.IPlayer;


public class PlayerAlreadyHasAnOrderException extends Exception {
    public PlayerAlreadyHasAnOrderException (IPlayer player){
        super("Der Spieler " + player + " hat bereits eine Order.");
    }
}
