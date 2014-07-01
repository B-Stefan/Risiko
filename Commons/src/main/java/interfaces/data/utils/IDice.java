package interfaces.data.utils;

import interfaces.IToStringRemote;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by Stefan on 29.06.14.
 */
public interface IDice extends Serializable, Remote, IToStringRemote {

    /**
     * Gibt die Gewürfelte Zahl aus
     * @return
     */
    public int getDiceNumber() throws RemoteException;
    /**
     *  throw the dice
     */
    public void throwDice()  throws RemoteException;
	public boolean isDiceHigherOrEqual(IDice iDice)  throws RemoteException;

}
