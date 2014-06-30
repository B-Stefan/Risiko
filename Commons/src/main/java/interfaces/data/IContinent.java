package interfaces.data;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;
import java.util.UUID;

public interface IContinent extends Remote, Serializable {

	public String getName() throws RemoteException;


    /**
     * Ermittelt den aktuellen Besitzter des Kontinents, prüft ob alle Länder den gleichen Player als owner haben
     * @return Gibt den Player zurück, der alle Länder auf diesem Kontinent besitzt, wenn der Kontinent geteilt wird wird null zurück gegeben.
     */
    public IPlayer getCurrentOwner() throws RemoteException;

	/**
	 * Fügt dem Kontinent ein Land hinzu
	 * @param player Der Land, der dem Kontinent hinzugefügt werden soll
	 */
	public void addCountry(ICountry player) throws RemoteException;

    /**
     * Löscht aus dem Kontinent ein Land
     * @param countryToDelete Land das gelöscht werden soll
     * @return
     */
    public boolean removeCountry (ICountry countryToDelete) throws RemoteException;

    public int getBonus() throws RemoteException;

}
