package main.java.logic.exceptions;

public class InvalidPlayerException extends Exception{
	public InvalidPlayerException(){
		super("Der ausgew�hlte Spieler ist ung�ltig");
	}
}
