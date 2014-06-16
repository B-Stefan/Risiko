package main.java.logic.exceptions;

/**
 * Created by Stefan on 11.06.14.
 */
public class UserCanceledException extends Exception {
    public UserCanceledException (){
        super("Der Benutzer hat die Aktion abgebrochen");
    }
}
