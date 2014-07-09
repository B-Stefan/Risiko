package exceptions;

public class AggessorNotThrowDiceException extends Exception{
    public AggessorNotThrowDiceException(){
        super("Der Angreifer muss zuerst wüfeln und hat dies noch nicht getan ");
    }
}
