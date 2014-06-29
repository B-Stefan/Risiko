package exceptions;


import interfaces.data.IPlayer;

/**
 * Created by Stefan on 01.04.2014.
 */
public class PlayerNotExsistInGameException extends  Exception{
    public PlayerNotExsistInGameException(IPlayer player){
        super("Dier Spieler " + player.getName() + " exisitiert nicht in diesem Spiel");
    }
}
