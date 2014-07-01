package interfaces.data.cards;

import interfaces.IToStringRemote;
import interfaces.data.ICountry;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by Stefan on 29.06.14.
 */
public interface ICard extends Remote, Serializable, IToStringRemote {

	public ICountry getCountry()            throws RemoteException;
	
	public String getType()                 throws RemoteException;

}
