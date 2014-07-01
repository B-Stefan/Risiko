package server;

import exceptions.ClientNotFoundException;
import interfaces.IClient;
import interfaces.IFight;
import interfaces.data.IPlayer;
import server.logic.data.Player;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stefan on 01.07.14.
 */
public class ClientManager implements Runnable  {

    private final List<IClient> clients = new ArrayList<IClient>();
    public ClientManager(){

    }
    public void addClient(IClient client){
        this.clients.add(client);
    }
    public void removeClient(IClient client){
        this.clients.remove(client);
    }
    public void broadcastMessage(String msg) {
        for(IClient client:this.clients){
            try {
                client.receiveMessage(msg);
            }catch (RemoteException e){
                //Remove if Problem
                this.clients.remove(client);
            }
        }
    }

    /**
     * Gibt dem player die Anweisung das Fight menü zu öffnen
     * @param fight Der fight um den es geht
     * @param player Der Spieler bei dem das Fenster aufgemacht werden soll
     * @throws ClientNotFoundException
     * @throws RemoteException
     */
    public void broadcastFight(IFight fight, IPlayer player) throws ClientNotFoundException,RemoteException{
        Player realPlayer;
        try {
            realPlayer = (Player) fight.getDefender();
        }catch (RemoteException | ClassCastException e){
            throw new RuntimeException(e);
        }
        IClient client = realPlayer.getClient();
        if(client != null){
            client.receiveFightEvent(fight);
        }
        else {
            throw new ClientNotFoundException(player);
        }

    }
    public void broadcastUIUpdate(IClient.UIUpdateTypes type) {
        for(IClient client : this.clients){
            try{
                client.receiveUIUpdateEvent();
            }catch (RemoteException e){
                this.clients.remove(client);
            }
        }
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used to create a thread, starting the thread
     * causes the object's <code>run</code> method to be called in that separately executing thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {

    }
}
