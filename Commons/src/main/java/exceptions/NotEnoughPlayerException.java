package exceptions;


public class NotEnoughPlayerException extends Exception {
    public NotEnoughPlayerException(int minCount){
        super("Es müssen midnestends "  + minCount + " Spieler teilnehmen");
    }
}
