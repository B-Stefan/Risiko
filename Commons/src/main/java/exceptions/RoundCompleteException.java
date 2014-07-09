package exceptions;


public class RoundCompleteException extends Exception {
    public RoundCompleteException(){
        super("In dieser Runde sind alle Züge gemacht wurden");
      }
}
