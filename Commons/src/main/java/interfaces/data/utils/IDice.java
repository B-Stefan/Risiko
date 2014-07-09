package interfaces.data.utils;

import interfaces.IToStringRemote;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface für einen würfel
 */
public interface IDice extends Serializable, Remote, IToStringRemote {

    /**
     * Gibt die Gewürfelte Zahl aus
     * @return
     */
    public int getDiceNumber() throws RemoteException;


}
