package exceptions;


import interfaces.data.IArmy;

/**
 * Created by Stefan on 01.05.14.
 */
public class ArmyAlreadyMovedException extends Exception {
    public ArmyAlreadyMovedException(IArmy army){
        super("Die Einheit" + army + " wurde bereits bewegt ");
    }
}
