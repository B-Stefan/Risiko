package exceptions;

import interfaces.ITurn;

/**
 * Created by Stefan on 01.05.14.
 */
public class ToManyNewArmysException extends Exception {
    public ToManyNewArmysException(ITurn turn){
        super("Du hast noch " + turn.getNewArmysSize() + " Einheiten, die du verteilen musst");
    }
}
