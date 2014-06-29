
import configuration.ServerConstants;
import configuration.ServerConts;
import persistence.PersistenceManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Diese Server Klasse startet den Server und stellt den GameManagerService über RMI bereit
 */
public class Server {


    private final PersistenceManager persistenceManager;
    public Server(){
        persistenceManager = new PersistenceManager();

        try {
            final GameManager gameManager = new GameManager(this.persistenceManager);
            final Registry registry = LocateRegistry.createRegistry(ServerConstants.DEFAULT_PORT);

            registry.rebind(ServerConstants.DEFAULT_SERVICE_Name, gameManager);

            System.out.println("Risiko-Server läuft...");
        } catch (final RemoteException e) {

            e.printStackTrace();
        }
    }
    public static void main(String[] args){
        //Start server
        new Server();
    }
}
