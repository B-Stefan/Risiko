package interfaces.data;
import exceptions.CountriesNotConnectedException;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.UUID;

/**
 * Für eine einzelne Armee geminsame Methoden für Client und Server werden in diesem Interface beschrieben
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
