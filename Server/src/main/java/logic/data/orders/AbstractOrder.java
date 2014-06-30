package logic.data.orders;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import interfaces.data.IPlayer;

/**
 * Created by Stefan on 30.06.14.
 */
public class AbstractOrder extends UnicastRemoteObject{

    /**
     * Der Spieler, dem die Order zugewiesen ist
     */
    protected final IPlayer agent;



    protected AbstractOrder(final IPlayer agend) throws RemoteException{
        this.agent = agend;
    }

    /**
     * Getter für den Agent
     * @return Player
     */

    public IPlayer getAgent() {
        return this.agent;
    }
}
