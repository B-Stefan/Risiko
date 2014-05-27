package main.java.logic.exceptions;

public class InvalidAmountOfArmiesException extends Exception {
	
	public InvalidAmountOfArmiesException(int size, String text){
		super("" + size + " Ist eine ungültige Anzahl an Armeen. Erlaubt sind zwischen " + text + " Armeen." );
	}
}
