package exceptions;


public class TurnCompleteException extends Exception {
    public TurnCompleteException(){
        super("Der Zug hat keinen weiteren step mehr");
      }
}
