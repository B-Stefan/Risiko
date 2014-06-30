package exceptions;

public class InvalidFightException extends Exception {
	
	public InvalidFightException(){
		super("Ungültige Fight Situation");
	}
}
