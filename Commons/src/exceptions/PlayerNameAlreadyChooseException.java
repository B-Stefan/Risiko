package exceptions;

/**
 * Created by Stefan on 29.06.14.
 */
public class PlayerNameAlreadyChooseException extends Exception {
    public PlayerNameAlreadyChooseException(String name){
        super("Der Spielername " + name + " wird bereits im Spiel verwendets");
    }
}
