package exceptions;
import logic.Turn;

/**
 * Created by Stefan on 01.05.14.
 */
public class TurnNotCompleteException extends Exception {
    public TurnNotCompleteException(Turn turn){
        super("Der Zug wurde noch nicht komplett beendet es müssen noch folgende Steps durchgeführt werden: " + turn.getAllowedSteps());
      }
}
