package exceptions;

/**
 * Created by Stefan on 01.05.14.
 */
public class RoundCompleteException extends Exception {
    public RoundCompleteException(){
        super("In dieser Runde sind alle Züge gemacht wurden");
      }
}
