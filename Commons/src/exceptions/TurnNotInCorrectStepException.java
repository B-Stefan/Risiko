package exceptions;
import logic.Turn;

/**
 * Created by Stefan on 01.05.14.
 */
public class TurnNotInCorrectStepException extends Exception {
    public TurnNotInCorrectStepException(Turn.steps step, Turn turn){
        super("Der Turn befindet sich zurzeit nicht im korrekten step. Er befindet sich in " + turn.getCurrentStep() + " Sie versuchen jedoch eine Aktion durchführen, die den step " + step + " verlangt .Als nächstes ist jedoch " + turn.getNextStep() + " dran"  );
    }
}
