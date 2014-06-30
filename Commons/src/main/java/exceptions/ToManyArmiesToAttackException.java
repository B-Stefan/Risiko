package exceptions;

public class ToManyArmiesToAttackException extends Exception {
	public ToManyArmiesToAttackException(){
		super("Sie greifen mit zu vielen Ameen an es dürfen maximal");
	}
}
