package main.java.logic.exceptions;

/**
 * Created by Stefan on 29.06.14.
 */
public class AggessorNotThrowDiceException extends Exception{
    public AggessorNotThrowDiceException(){
        super("Der Angreifer muss zuerst wüfeln und hat dies noch nicht getan ");
    }
}
