package main.java.logic.exceptions;
import main.java.logic.Turn;

/**
 * Created by Stefan on 01.05.14.
 */
public class ToManyNewArmysException extends Exception {
    public ToManyNewArmysException(Turn turn){
        super("Du hast noch " + turn.getNewArmysSize() + " Einheiten, die du verteilen musst");
    }
}
