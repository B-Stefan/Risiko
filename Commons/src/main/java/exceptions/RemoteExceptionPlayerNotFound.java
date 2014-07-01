package exceptions;

import java.rmi.RemoteException;

/**
 * Created by Stefan on 01.07.14.
 */
public class RemoteExceptionPlayerNotFound extends RemoteException{

    public RemoteExceptionPlayerNotFound(){
        super("Player wurder falsch über das Netzwerk übertragen und konnte nicht gefunden werden");
    }
}
