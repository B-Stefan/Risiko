package exceptions;

public class InvalidPlayerException extends Exception{
	public InvalidPlayerException(){
		super("Der ausgewählte Spieler ist ungültig");
	}
}
