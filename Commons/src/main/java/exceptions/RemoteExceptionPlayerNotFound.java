package exceptions;

import java.rmi.RemoteException;


public class RemoteExceptionPlayerNotFound extends RemoteException{

    public RemoteExceptionPlayerNotFound(){
        super("Player wurder falsch über das Netzwerk übertragen und konnte nicht gefunden werden");
    }
}
