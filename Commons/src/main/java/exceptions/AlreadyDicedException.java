package exceptions;

public class AlreadyDicedException extends Exception {
	
	public AlreadyDicedException(){
		super("Du hast bereits gewürfelt, warte bis der Verteidiger würfelt");
	}
}
