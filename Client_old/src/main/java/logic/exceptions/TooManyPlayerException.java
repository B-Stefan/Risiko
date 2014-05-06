package main.java.logic.exceptions;

/**
 * Created by Stefan on 01.04.2014.
 */
public class TooManyPlayerException extends Exception {
    public TooManyPlayerException(int count){
        super("Es dürfen nicht mehr als "  + count + " Spieler teilnehmen");
    }
}
