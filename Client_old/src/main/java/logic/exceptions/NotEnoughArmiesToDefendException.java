package main.java.logic.exceptions;


public class NotEnoughArmiesToDefendException extends Exception {
	
	public NotEnoughArmiesToDefendException(){
		super("Land ist unbesetzt");
	}
}
