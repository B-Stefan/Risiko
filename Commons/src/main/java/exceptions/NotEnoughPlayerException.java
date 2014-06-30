package exceptions;

/**
 * Created by Stefan on 01.04.2014.
 */
public class NotEnoughPlayerException extends Exception {
    public NotEnoughPlayerException(int minCount){
        super("Es müssen midnestends "  + minCount + " Spieler teilnehmen");
    }
}
