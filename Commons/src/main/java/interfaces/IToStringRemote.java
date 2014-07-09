package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;


/***
 * Interface was zum aufrufen der TOString methode durch den Client gedacht ist, anstatt toString sollte auf Client Seite immer ToStringRemote ausgeführt werden
 */
public interface IToStringRemote  extends Remote{
    public String toStringRemote() throws RemoteException;
}
