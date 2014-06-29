package main.java.logic.exceptions;

public class NotEnoughCardsToExchangeException extends Exception {
	public NotEnoughCardsToExchangeException(){
		super("Du hast nicht genug Karten zum eintauschen. Du brauchst mindestens drei.");
	}
}
