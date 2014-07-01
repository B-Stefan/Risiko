package exceptions;

import interfaces.data.IPlayer;

import java.rmi.RemoteException;

/**
 * Created by Stefan on 01.07.14.
 */
public class ClientNotFoundException extends Exception {
    public ClientNotFoundException(IPlayer player) throws RemoteException{
        super("Zu dem angegeben Spieler " +player.getName() + "konnte kein entsprechender Client gefunden werden");
    }
}
