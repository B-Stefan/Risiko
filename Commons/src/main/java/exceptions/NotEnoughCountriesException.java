package exceptions;


public class NotEnoughCountriesException extends Exception {
    public NotEnoughCountriesException(int minCount){
        super("Es müssen midnestends soviele Länder existieren wie Spieler angemeldet sind, es gibt jedoch nur "+ minCount);
    }
}
