package exceptions;

public class GameAllreadyStartedException extends Exception {

    public  GameAllreadyStartedException (){
        super("The game was already started");
    }
}
