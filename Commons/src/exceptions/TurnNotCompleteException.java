package exceptions;


import interfaces.ITurn;

import java.rmi.RemoteException;

/**
 * Created by Stefan on 01.05.14.
 */
public class TurnNotCompleteException extends Exception {
    public TurnNotCompleteException(ITurn turn) throws RemoteException{
        super("Der Zug wurde noch nicht komplett beendet es müssen noch folgende Steps durchgeführt werden: " + turn.getAllowedSteps());
      }
}
