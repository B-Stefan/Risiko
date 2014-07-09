package exceptions;

public class GameIsCompletedException extends Exception {
    public GameIsCompletedException(){
        super ("Ein Spieler hat das Spiel erfolgreich beendet ");
    }
}
