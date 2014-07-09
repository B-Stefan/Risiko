package interfaces;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;


/**
 * Interface was für den Bradcast vom Server zum Client benötigt wird
 */
public interface IClient extends Remote,Serializable{

    public static enum UIUpdateTypes {
        PLAYER,
        COUNtRY,
        FIGHT,
        ALL
    }

    /**
     *
     * @param msg Liste von Nachrichten, die angezeigt werden sollen
     * @throws RemoteException
     */
    public void receiveMessage(List<String> msg) throws RemoteException;

    /**
     * Wird vom Server aufgerufen, wenn ein Clien einen fight startet, sodass dieser sich auch auf dem anderen Client öffnet
     * @param fight Der Fight der geöffnet werden soll
     * @throws RemoteException
     */
    public void receiveFightEvent(IFight fight) throws RemoteException;

    /**
     * Wenn ein Update des UI notwendig ist
     * @throws RemoteException
     */
    public void receiveUIUpdateEvent() throws RemoteException;
}
