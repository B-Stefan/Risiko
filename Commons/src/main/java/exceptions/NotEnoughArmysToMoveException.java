package exceptions;
import interfaces.data.ICountry;

public class NotEnoughArmysToMoveException extends Exception {
    public NotEnoughArmysToMoveException(ICountry from){
        super("Dir stehen auf dem Land " + from + " nicht mehr genügend Einheiten zur Verfügung" );
    }
}
