package exceptions;

public class NotEoughUnmovedArmiesException extends Exception{
	public NotEoughUnmovedArmiesException(int amount){
		super("Du kannst du noch " + amount + " Armee(n) verschieben. Die restlichen Armeen auf diesem Land wurden bereits verschoben");
	}
}
