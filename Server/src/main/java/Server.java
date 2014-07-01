
import configuration.ServerConfiguration;
import persistence.PersistenceManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ExportException;

/**
 * Diese Server Klasse startet den Server und stellt den GameManagerService über RMI bereit
 */
public class Server {


    /**
     * Der Server startet die registry und fügt den GameManger der ServiceListe hinzu
     * Anschließend wird auf entsprchende Verbindungen gewartet
     */
    public Server(){
        final PersistenceManager persistenceManager = new PersistenceManager();

        try {
            final GameManager gameManager = new GameManager(persistenceManager);
            final Registry registry = LocateRegistry.createRegistry(ServerConfiguration.DEFAULT.PORT);

            registry.rebind(ServerConfiguration.DEFAULT.SERVICE_NAME, gameManager);

            System.out.println("Risiko-Server läuft...");
        } catch (final RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * Main Methdode zum starten des Servers
     *
     */
    public static void main(String[] args){
        //Start server
        new Server();
    }
}
