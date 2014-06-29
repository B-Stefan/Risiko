package main;
import configuration.ServerConstants;
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
        final Registry registry = LocateRegistry.getRegistry(ServerConstants.DEFAULT_SERVER, ServerConstants.DEFAULT_PORT);
        final IGameManager gameManager = (IGameManager) registry.lookup(ServerConstants.DEFAULT_SERVICE_Name);
        return gameManager;
    }
}
