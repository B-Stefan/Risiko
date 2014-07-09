package exceptions;


public class RoundNotCompleteException extends Exception {
    public RoundNotCompleteException(){
        super("Diese Runde ist nocht nicht beendet wechsel mit cd in den Zug und mit gebe dir mit state den Status aus.");
      }
}
