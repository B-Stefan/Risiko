package exceptions;
import interfaces.ITurn;

import java.rmi.Remote;
import java.rmi.RemoteException;



public class TurnNotInCorrectStepException extends Exception {
    public TurnNotInCorrectStepException(ITurn.steps step, ITurn turn) throws RemoteException{
        super("Der Turn befindet sich zurzeit nicht im korrekten step. Er befindet sich in " + turn.getCurrentStep() + " Sie versuchen jedoch eine Aktion durchführen, die den step " + step + " verlangt .Als nächstes ist jedoch " + turn.getNextStep() + " dran"  );
    }
}
