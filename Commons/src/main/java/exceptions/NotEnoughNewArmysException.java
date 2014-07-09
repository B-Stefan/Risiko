package exceptions;
import interfaces.ITurn;


public class NotEnoughNewArmysException extends Exception {
    public NotEnoughNewArmysException(ITurn turn){
        super("Dir steht keine Einheit merhr zur Verfügung, die du verteilen kannst");
    }
}
