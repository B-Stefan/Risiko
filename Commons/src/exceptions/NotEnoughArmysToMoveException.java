package exceptions;
import logic.Turn;
import logic.data.Country;

/**
 * Created by Stefan on 01.05.14.
 */
public class NotEnoughArmysToMoveException extends Exception {
    public NotEnoughArmysToMoveException(Country from){
        super("Dir stehen auf dem Land " + from + " nicht mehr genügend Einheiten zur Verfügung" );
    }
}
