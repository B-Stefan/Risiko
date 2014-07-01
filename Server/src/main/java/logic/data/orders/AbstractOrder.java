package logic.data.orders;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import interfaces.data.IPlayer;
import logic.data.Player;

/**
 * Created by Stefan on 30.06.14.
 */
public class AbstractOrder extends UnicastRemoteObject{

    /**
     * Der Spieler, dem die Order zugewiesen ist
     */
    protected final Player agent;



    protected AbstractOrder(final Player agent) throws RemoteException{
        this.agent = agent;
    }

    /**
     * Getter für den Agent
     * @return Player
     */

    public Player getAgent() {
        return this.agent;
    }
    /**
     * ToString methode, die Remote aufgerufen werden kann
     * @return
     * @throws RemoteException
     */
    public String toStringRemote() throws RemoteException{

        return this.toString();
    }
}
