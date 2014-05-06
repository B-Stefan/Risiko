package main.java.logic.exceptions;

/**
 * Created by Stefan on 29.04.14.
 */
public class GameAllreadyStartedException extends Exception {

    public  GameAllreadyStartedException (){
        super("The game was already started");
    }
}
