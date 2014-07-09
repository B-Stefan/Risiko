package exceptions;


import interfaces.data.IArmy;

public class ArmyAlreadyMovedException extends Exception {
    public ArmyAlreadyMovedException(IArmy army){
        super("Die Einheit" + army + " wurde bereits bewegt ");
    }
}
