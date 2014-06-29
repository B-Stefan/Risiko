package main.java.logic.exceptions;
public class YouCannotDrawANewCardException extends Exception{
	public YouCannotDrawANewCardException(){
		super("Ein Spieler kann nur maximal drei Karten besitzen");
	}
}
