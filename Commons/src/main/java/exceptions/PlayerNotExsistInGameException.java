package exceptions;


import java.rmi.RemoteException;

import interfaces.data.IPlayer;

/**
 * Created by Stefan on 01.04.2014.
 */
public class PlayerNotExsistInGameException extends  Exception{
    public PlayerNotExsistInGameException(IPlayer player) throws RemoteException{
        super("Dier Spieler " + player.getName() + " exisitiert nicht in diesem Spiel");
    }
    public PlayerNotExsistInGameException(String name){
        super("Dier Spieler " + name+ " exisitiert nicht in diesem Spiel");
    }
}
