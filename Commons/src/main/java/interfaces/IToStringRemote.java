package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by Stefan on 30.06.14.
 */
public interface IToStringRemote  extends Remote{
    public String toStringRemote() throws RemoteException;
}
