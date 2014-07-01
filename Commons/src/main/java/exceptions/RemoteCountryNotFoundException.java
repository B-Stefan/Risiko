package exceptions;

/**
 * Created by Stefan on 30.06.14.
 */
public class RemoteCountryNotFoundException extends Exception {
    public RemoteCountryNotFoundException(){
        super ("Das Land konnte nicht gefunden werden, dies entsteht druch ein NetzwerkProblem");
    }
}
