package exceptions;

import java.io.IOException;

/**
 * Created by Stefan on 28.05.14.
 */
public class PersistenceEndpointIOException extends IOException {

    public PersistenceEndpointIOException(String msg){
        super(msg);
    }
    public PersistenceEndpointIOException(Exception e){
        super(e);
    }
}