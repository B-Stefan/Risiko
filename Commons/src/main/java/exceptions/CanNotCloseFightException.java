package exceptions;

/**
 * Created by Stefan on 15.07.14.
 */
public class CanNotCloseFightException extends Exception {
    public CanNotCloseFightException(boolean defender){
        super("Du kannst den Fight nicht schließen" + (defender ? " Du bist Verteidiger" : " Du bist Angreifer"));
    }
}
