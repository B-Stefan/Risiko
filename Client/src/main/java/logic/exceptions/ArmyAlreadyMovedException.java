package main.java.logic.exceptions;
import main.java.logic.data.Army;

/**
 * Created by Stefan on 01.05.14.
 */
public class ArmyAlreadyMovedException extends Exception {
    public ArmyAlreadyMovedException(Army army){
        super("Die Einheit" + army + " wurde bereits bewegt ");
    }
}
