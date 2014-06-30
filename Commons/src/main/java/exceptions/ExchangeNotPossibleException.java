package exceptions;

public class ExchangeNotPossibleException extends Exception {
	public ExchangeNotPossibleException(){
		super("Du kannst Karten nur ganz am Anfang deines Zuges eintauschen, bevor du Einheiten platziert hast");
	}
}
