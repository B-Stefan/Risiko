package main;
import configuration.ServerConts;
import interfaces.IGameManager;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 *
 */
public class ServerEngine {

    public static IGameManager getGameMangerService () throws RemoteException, NotBoundException{
        final Registry registry = LocateRegistry.getRegistry(ServerConts.DEFAULT_SERVER, ServerConts.DEFAULT_PORT);
        final IGameManager gameManager = (IGameManager) registry.lookup(ServerConts.DEFAULT_SERVICE_Name);
        return gameManager;
    }
}
