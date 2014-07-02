package interfaces;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Created by Stefan on 01.07.14.
 */
public interface IClient extends Remote,Serializable{

    public static enum UIUpdateTypes {
        PLAYER,
        COUNtRY,
        FIGHT,
        ALL
    }
    public void receiveMessage(List<String> msg) throws RemoteException;
    public void receiveMessage(String msg) throws RemoteException;
    public void receiveFightEvent(IFight fight) throws RemoteException;
    public void receiveUIUpdateEvent() throws RemoteException;
}
