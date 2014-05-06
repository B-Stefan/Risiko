package main.java.logic.exceptions;


public class NotEnoughArmiesToDefendException extends Exception {
	
	public NotEnoughArmiesToDefendException(){
		super("Es stehen nicht genügend Einheiten zur Verteidigung zur Verfügung");
	}
}
