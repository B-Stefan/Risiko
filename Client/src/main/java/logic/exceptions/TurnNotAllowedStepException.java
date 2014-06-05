package main.java.logic.exceptions;
import main.java.logic.Turn;

/**
 * Created by Stefan on 01.05.14.
 */
public class TurnNotAllowedStepException extends Exception {
    public TurnNotAllowedStepException(Turn.steps step, Turn turn){
        super("Der step " + step + " ist beim Turn " + turn + " nicht erlaubt. Sie dürfen noch " + turn.getAllowedSteps() + " und sind aktuell im Step" + turn.getCurrentStep() );
    }
}
