package main.java.logic.exceptions;
import main.java.logic.Turn;
/**
 * Created by Stefan on 01.05.14.
 */
public class TurnNotInMoveStateException extends Exception {
    public  TurnNotInMoveStateException (Turn turn ){
        super("Der Zug " + turn + " befindet sich zurzeit in der stufe " + turn.getCurrentStep());
    }
}
