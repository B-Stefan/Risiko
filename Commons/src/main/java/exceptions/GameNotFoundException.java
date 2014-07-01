package exceptions;

/**
 * Created by Stefan on 30.06.14.
 */
public class GameNotFoundException extends Exception{
    public GameNotFoundException(){
        super("Das Spiel konnte auf dem Server nicht gefunden werden");
    }
}
