package main.java.logic.exceptions;

/**
 * Created by Stefan on 01.05.14.
 */
public class TurnCompleteException extends Exception {
    public TurnCompleteException(){
        super("Der Zug hat keinen weiteren step mehr");
      }
}
