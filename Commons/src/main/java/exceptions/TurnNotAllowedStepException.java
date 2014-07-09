package exceptions;


import interfaces.ITurn;

import java.rmi.RemoteException;


public class TurnNotAllowedStepException extends Exception {
    public TurnNotAllowedStepException(ITurn.steps step, ITurn turn) throws RemoteException{
        super("Der step " + step + " ist beim Turn " + turn + " nicht erlaubt. Sie dürfen noch " + turn.getAllowedSteps() + " und sind aktuell im Step" + turn.getCurrentStep() );
    }
}
