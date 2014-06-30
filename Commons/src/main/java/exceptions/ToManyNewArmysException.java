package exceptions;

import interfaces.ITurn;

import java.rmi.RemoteException;

/**
 * Created by Stefan on 01.05.14.
 */
public class ToManyNewArmysException extends Exception {
    public ToManyNewArmysException(ITurn turn) throws RemoteException{
        super("Du hast noch " + turn.getNewArmysSize() + " Einheiten, die du verteilen musst");
    }
}
