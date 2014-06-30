package interfaces.data;
import exceptions.CountriesNotConnectedException;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by Stefan on 29.06.14.
 */
public interface IArmy extends Remote, Serializable {
	public void setPosition(ICountry country)  throws CountriesNotConnectedException, RemoteException;

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
