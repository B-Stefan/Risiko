package main.java.logic.exceptions;

/**
 * Created by Stefan on 01.04.2014.
 */
public class NotEnoughCountriesException extends Exception {
    public NotEnoughCountriesException(int count){
        super("Es müssen midnestends soviele Länder existieren wie Spieler angemeldet sind, es gibt jedoch nur "+ count);
    }
}
