package exceptions;

import java.io.IOException;


public class PersistenceEndpointIOException extends IOException {

    public PersistenceEndpointIOException(String msg){
        super(msg);
    }
    public PersistenceEndpointIOException(Exception e){
        super(e);
    }
}
