package exceptions;


import java.rmi.RemoteException;

import interfaces.data.IPlayer;


public class PlayerNotExsistInGameException extends  Exception{
    public PlayerNotExsistInGameException(IPlayer player) throws RemoteException{
        super("Dier Spieler " + player.getName() + " exisitiert nicht in diesem Spiel");
    }
    public PlayerNotExsistInGameException(String name){
        super("Dier Spieler " + name+ " exisitiert nicht in diesem Spiel");
    }
}
