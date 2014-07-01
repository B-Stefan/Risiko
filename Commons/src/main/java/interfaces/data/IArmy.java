package interfaces.data;
import exceptions.CountriesNotConnectedException;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.UUID;

/**
 * Created by Stefan on 29.06.14.
 */
public interface IArmy extends Remote, Serializable {
    /**
     * Gibt den Besitzer der Armee wieder
     * @return  Besitzer der Armee
     */
    public IPlayer getOwner() throws RemoteException;

    /**
	 * Getter für die Position
	 * @return position Aktuelle Position der Armee, kann null zurückgeben.
	 */
	public ICountry getPosition() throws RemoteException;


}
