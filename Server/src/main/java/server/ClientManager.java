package server;

import commons.exceptions.ClientNotFoundException;
import commons.interfaces.IClient;
import commons.interfaces.IClientManager;
import commons.interfaces.IFight;
import server.logic.Fight;
import server.logic.data.Player;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

/**
 * Klasse die alle angemeldeten Clients verwaltet um Clients Rückmeldung zu geben (Broadcast)
 */
public  class ClientManager extends UnicastRemoteObject implements Runnable, IClientManager {

    /**
     * Creates a new Thread and start the Clint Manager, to Broadcast all messages
     * @param manager
     * @return
     */
    public  static Thread startWatchBroadcast(ClientManager manager,String name){
        Thread t = new Thread(manager);
        t.start();
        t.setName(name);
        return t;
    }

    /**
     * Liste der Clients
     */
    private final List<IClient> clients = new ArrayList<IClient>();

    /**
     * Liste der ausstehenden UI, Updates, die noch nicht an die Clients gesendet wurden
     */
    private final List<IClient.UIUpdateTypes> updatesUIToBroadcast= new ArrayList<IClient.UIUpdateTypes>();


    /**
     * Liste der ausstehenden Fights, die noch nicht an die Clients gesendet wurden
     */
    private final List<Fight> fightsToBroadcast= new ArrayList<Fight>();


    /**
     * Liste der ausstehenden Fights, die geschlossen werden sollen und noch nicht gesendet wurden
     */
    private final List<Fight> fightsToCloseToBroadcast= new ArrayList<Fight>();

    /**
     * Liste der ausstehenden Nachrichten, die noch nicht an die Clients gesendet wurden
     *
     */
    private final List<String> messagesToBroadcast= new ArrayList<String>();

    /**
     * Manager, der Clients verwaltet und Methoden zum broadcast bereitstellt
     */
    public ClientManager() throws RemoteException{

    }

    /**
     * Fügt einen client der Liste hinzu
     * @param client - Hinzuzufügender client
     */
    public synchronized void addClient(IClient client) throws RemoteException{
        this.clients.add(client);
    }

    /**
     * Löscht einen client
     * @param client client der zu löschen ist
     */
    public synchronized void removeClient(IClient client) throws RemoteException{
        this.clients.remove(client);
    }

    /**
     * Meldet alle ausstehenden Nachrichten den Clients
     */
    private synchronized void broadcastMessageToClients() {
        if(messagesToBroadcast.isEmpty()){
            return;
        }
        for(IClient client:this.clients){
            try {
                client.receiveMessage(messagesToBroadcast);
            }catch (RemoteException e){
                //Remove if Problem
                this.clients.remove(client);
            }
        }
    }

    /**
     * Gibt dem player die Anweisung das Fight menü zu öffnen
     * @throws ClientNotFoundException
     * @throws RemoteException
     */
    private synchronized void broadcastFightToClients() throws ClientNotFoundException,RemoteException{
        Player realPlayer;
        for(IFight fight : this.fightsToBroadcast){
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
                throw new ClientNotFoundException(realPlayer);
            }
        }
        this.fightsToBroadcast.clear();


    }

    /**
     * Meldet alle UI updates an alle Clients
     */
    private synchronized void broadcastUIUpdateToClients() {
        if(this.updatesUIToBroadcast.isEmpty()){
            return;
        }
        for(IClient client : this.clients){
            try{

                client.receiveUIUpdateEvent();
            }catch (RemoteException e){
                this.clients.remove(client);
            }
        }
        this.updatesUIToBroadcast.clear();
    }



    /**
     * Meldet alle UI updates an alle Clients
     */
    private synchronized void broadcastFightsToCloseToClients() {
        if(this.fightsToCloseToBroadcast.isEmpty()){
            return;
        }
        for(IFight fight : this.fightsToCloseToBroadcast){
            for(IClient client : this.clients){
                try{

                    client.receiveFightCloseEvent(fight);
                }catch (RemoteException e){
                    this.clients.remove(client);
                }
            }
        }

        this.fightsToCloseToBroadcast.clear();
    }

    /**
     * fügt der Liste der ausstehenden Update ein Update hinzu
     * @param type Beschreibt einen bestimmten bereich,der geupdatet werden soll
     */
    public synchronized void broadcastUIUpdate(IClient.UIUpdateTypes type) {
        if(!updatesUIToBroadcast.contains(type) && !updatesUIToBroadcast.contains(IClient.UIUpdateTypes.ALL)){
            if(type == IClient.UIUpdateTypes.ALL){
                updatesUIToBroadcast.clear();
            }
            updatesUIToBroadcast.add(type);
        }
    }



    /**
     * Fügt der Liste der ausstehenden Fights den Fight hinzu
     * @param fight Fight der an die Clients gegeben werden soll
     */
    public synchronized void broadcastFight(Fight fight) {
        if(!updatesUIToBroadcast.contains(fight)){
            fightsToBroadcast.add(fight);
        }
    }


    /**
     * Fügt der Liste der ausstehenden Nachrichten den String hinzu
     * @param msg MSg die an die Clients gegeben werden soll
     */
    public synchronized void broadcastMessage(String msg) {
            messagesToBroadcast.add(msg);

    }

    /**
     * Fügt der Liste der ausstehenden Nachrichten den String hinzu
     * @param fight
     */
    public synchronized void broadcastFightToClose(Fight fight) {
        fightsToCloseToBroadcast.add(fight);

    }


    /**

     * Startet den ClientManager Thread, sodass dieser jede Sekunde schaut ob ein broadcast notwendig ist und diesen ggf. ausführt.
     * So kann in der Logik z.b. in einer Schleife auch immer wieder UI-Updates gebroadcastet werden und die Clients bekommen nur 1 Update
     * The general contract of the method <code>run</code> is that it may take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public  void run() {
        while (!Thread.currentThread().isInterrupted()){
            synchronized (new Object()){
                try{
                    this.broadcastFightToClients();
                }catch (ClientNotFoundException | RemoteException e){
                    e.printStackTrace();
                }
                this.broadcastMessageToClients();
                this.broadcastUIUpdateToClients();
                this.broadcastFightsToCloseToClients();
            }
            try{
                    Thread.sleep(1000);

            }catch (InterruptedException e){
                throw new RuntimeException(e);
            }
        }


    }
}
