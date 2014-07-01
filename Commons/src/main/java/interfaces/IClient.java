package interfaces;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by Stefan on 01.07.14.
 */
public interface IClient extends Remote,Serializable{

    public void updateGUI() throws RemoteException;
}
