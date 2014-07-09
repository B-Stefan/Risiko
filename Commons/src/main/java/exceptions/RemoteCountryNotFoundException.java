package exceptions;


public class RemoteCountryNotFoundException extends Exception {
    public RemoteCountryNotFoundException(){
        super ("Das Land konnte nicht gefunden werden, dies entsteht druch ein NetzwerkProblem");
    }
}
