import interfaces.IClient;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stefan on 01.07.14.
 */
public class ClientManager {

    private final List<IClient> clients = new ArrayList<IClient>();
    public ClientManager(){

    }
    public void addClient(IClient client){
        this.clients.add(client);
    }
    public void removeClient(IClient client){
        this.clients.remove(client);
    }
    public void broadcastMessage() throws RemoteException{
        for(IClient client:this.clients){
            client.updateGUI();
        }
    }

}
