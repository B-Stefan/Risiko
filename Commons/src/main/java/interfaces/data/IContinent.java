package interfaces.data;

import interfaces.IToStringRemote;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;
import java.util.UUID;

public interface IContinent extends Remote, Serializable, IToStringRemote {

    /**
     * Gibt den Namen des Kontinents zurück
     * @return
     * @throws RemoteException
     */
	public String getName() throws RemoteException;


    /**
     * Ermittelt den aktuellen Besitzter des Kontinents, prüft ob alle Länder den gleichen Player als owner haben
     * @return Gibt den Player zurück, der alle Länder auf diesem Kontinent besitzt, wenn der Kontinent geteilt wird wird null zurück gegeben.
     */
    public IPlayer getCurrentOwner() throws RemoteException;


    /**
     * Holt den Bonus des Kontinents
     * @return
     * @throws RemoteException
     */
    public int getBonus() throws RemoteException;

}
