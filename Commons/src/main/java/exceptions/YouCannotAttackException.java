package exceptions;

public class YouCannotAttackException extends Exception {
	public YouCannotAttackException(){
		super("Du kannst nicht angreifen, du bist der Verteidiger");
	}
}
