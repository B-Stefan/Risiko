package main.java.logic.exceptions;
import main.java.logic.*;

/**
 * Created by Stefan on 01.04.2014.
 */
public class PlayerNotExsistInGameException extends  Exception{
    public PlayerNotExsistInGameException(Player player){
        super("Dier Spieler " + player.getName() + " exisitiert nicht in diesem Spiel");
    }
}
