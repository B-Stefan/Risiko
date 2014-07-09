package exceptions;


public class TooManyPlayerException extends Exception {
    public TooManyPlayerException(int count){
        super("Es dürfen nicht mehr als "  + count + " Spieler teilnehmen");
    }
}
