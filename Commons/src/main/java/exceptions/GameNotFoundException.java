package exceptions;

public class GameNotFoundException extends Exception{
    public GameNotFoundException(){
        super("Das Spiel konnte auf dem Server nicht gefunden werden");
    }
}
