package server;

import exceptions.ClientNotFoundException;
import interfaces.IClient;
import interfaces.IFight;
import server.logic.Fight;
import server.logic.data.Player;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

/**
 * Klasse die alle angemeldeten Clients verwaltet um Clients Rückmeldung zu geben (Broadcast)
 */
public  class ClientManager implements Runnable  {

    /**
     * Creates a new Thread and start the Clint Manager, to Broadcast all messages
     * @param manager
     * @return
     */
    public  static Thread startWatchBroadcast(ClientManager manager){
        Thread t = new Thread(manager);
        t.start();
        t.setName("ClientManager-Watch");
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
    private final List<IFight> fightsToBroadcast= new ArrayList<IFight>();

    /**
     * Liste der ausstehenden Nachrichten, die noch nicht an die Clients gesendet wurden
     *
     */
    private final List<String> messagesToBroadcast= new ArrayList<String>();

    /**
     * Manager, der Clients verwaltet und Methoden zum broadcast bereitstellt
     */
    public ClientManager(){

    }

    /**
     * Fügt einen Client der Liste hinzu
     * @param client - Hinzuzufügender Client
     */
    public synchronized void addClient(IClient client){
        this.clients.add(client);
    }

    /**
     * Löscht einen Client
     * @param client Client der zu löschen ist
     */
    public synchronized void removeClient(IClient client){
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
     * fügt der Liste der ausstehenden Update ein Update hinzu
     * @param type
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
     * @param fight
     */
    public synchronized void broadcastFight(Fight fight) {
        if(!updatesUIToBroadcast.contains(fight)){
            fightsToBroadcast.add(fight);
        }
    }

    /**
     * Fügt der Liste der ausstehenden Nachrichten den String hinzu
     * @param msg
     */
    public synchronized void broadcastMessage(String msg) {
            messagesToBroadcast.add(msg);

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
            }
            try{
                    Thread.sleep(1000);

            }catch (InterruptedException e){
                throw new RuntimeException(e);
            }
        }


    }
}
