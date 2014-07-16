package commons.exceptions;

/**
 * Created by Stefan on 12.07.14.
 */
public class FightNotWonException extends Exception {

    public FightNotWonException (){
        super("Der Kampf wurde noch nicht gewonnen");
    }
}
