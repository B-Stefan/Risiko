package exceptions;
import logic.Turn;

/**
 * Created by Stefan on 01.05.14.
 */
public class NotEnoughNewArmysException extends Exception {
    public NotEnoughNewArmysException(Turn turn){
        super("Dir steht keine Einheit merhr zur Verfügung, die du verteilen kannst");
    }
}
