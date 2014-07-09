package exceptions;


public class UserCanceledException extends Exception {
    public UserCanceledException (){
        super("Der Benutzer hat die Aktion abgebrochen");
    }
}
