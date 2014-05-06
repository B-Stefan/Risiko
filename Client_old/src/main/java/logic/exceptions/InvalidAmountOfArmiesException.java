package main.java.logic.exceptions;

public class InvalidAmountOfArmiesException extends Exception {
	
	public InvalidAmountOfArmiesException(int size){
		super("" + size + "Ist eine ungültige Anzahl an Armeen.");
	}
}
