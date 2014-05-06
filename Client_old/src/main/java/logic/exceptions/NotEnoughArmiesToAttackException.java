package main.java.logic.exceptions;

public class NotEnoughArmiesToAttackException extends Exception {
	public NotEnoughArmiesToAttackException(){
		super("Es sind nicht genug Einheiten zum angreifen da");
	}
}
