package exceptions;

import interfaces.data.IPlayer;

import java.rmi.RemoteException;

public class ClientNotFoundException extends Exception {
    public ClientNotFoundException(IPlayer player) throws RemoteException{
        super("Zu dem angegeben Spieler " +player.getName() + "konnte kein entsprechender Client gefunden werden");
    }
}
