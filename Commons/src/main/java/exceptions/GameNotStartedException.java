package exceptions;

/**
 * Created by Stefan on 29.04.14.
 */
public class GameNotStartedException extends Exception {
    public GameNotStartedException(){
        super ("Das Spiel wurde noch nicht gestartet, bitte starte es zunächst");
    }
}
