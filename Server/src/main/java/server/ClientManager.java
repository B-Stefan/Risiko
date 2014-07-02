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
 * Created by Stefan on 01.07.14.
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


    private final List<IClient> clients = new ArrayList<IClient>();
    private final List<IClient.UIUpdateTypes> updatesUIToBroadcast= new ArrayList<IClient.UIUpdateTypes>();
    private final List<IFight> fightsToBroadcast= new ArrayList<IFight>();
    private final List<String> messagesToBroadcast= new ArrayList<String>();
    public ClientManager(){

    }
    public synchronized void addClient(IClient client){
        this.clients.add(client);
    }
    public synchronized void removeClient(IClient client){
        this.clients.remove(client);
    }
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





    public synchronized void broadcastUIUpdate(IClient.UIUpdateTypes type) {
        if(!updatesUIToBroadcast.contains(type) && !updatesUIToBroadcast.contains(IClient.UIUpdateTypes.ALL)){
            if(type == IClient.UIUpdateTypes.ALL){
                updatesUIToBroadcast.clear();
            }
            updatesUIToBroadcast.add(type);
        }
    }

    public synchronized void broadcastFight(Fight fight) {
        if(!updatesUIToBroadcast.contains(fight)){
            fightsToBroadcast.add(fight);
        }
    }

    public synchronized void broadcastMessage(String msg) {
            messagesToBroadcast.add(msg);

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
