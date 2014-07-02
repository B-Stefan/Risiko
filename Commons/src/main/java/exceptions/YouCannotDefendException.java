package exceptions;

public class YouCannotDefendException extends Exception {
	public YouCannotDefendException(){
		super("Du kannst nicht verteidigen, du bist der Angreifer");
	}
}
