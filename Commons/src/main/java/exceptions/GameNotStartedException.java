package exceptions;

public class GameNotStartedException extends Exception {
    public GameNotStartedException(){
        super ("Das Spiel wurde noch nicht gestartet, bitte starte es zunächst");
    }
}
