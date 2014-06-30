package main;
import configuration.ServerConfiguration;
import interfaces.IGameManager;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Diese Klasse dient zum bereitstellen der Server Services
 */
public class ServerEngine {

    /**
     * Serverconfiguration
     */
    private final ServerConfiguration serverConf;

    /**
     * Erstellt eine ServerEngine mit entsprechender Konfiguration
     * @param conf Serverconfiguration
     */
    public ServerEngine(ServerConfiguration conf){
        this.serverConf = conf;
    }

    /**
     * Versucht die Verbindung zum Server aufzubauen und den GameService zu errecihen
     * @return IGameManager
     * @throws RemoteException
     * @throws NotBoundException
     */
    public IGameManager getGameMangerService () throws RemoteException, NotBoundException{
        final Registry registry = LocateRegistry.getRegistry(serverConf.SERVER_HOST,serverConf.PORT);
        final IGameManager gameManager = (IGameManager) registry.lookup(serverConf.SERVICE_NAME);
        return gameManager;
    }


}
