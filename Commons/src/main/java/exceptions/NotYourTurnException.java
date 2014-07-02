package exceptions;

public class NotYourTurnException extends Exception {
	public NotYourTurnException(){
		super("Du bist nicht am Zug");
	}
}
