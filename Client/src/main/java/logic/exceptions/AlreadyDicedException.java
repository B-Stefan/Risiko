package main.java.logic.exceptions;

public class AlreadyDicedException extends Exception {
	
	public AlreadyDicedException(){
		super("Du hast bereits gewürfelt");
	}
}
