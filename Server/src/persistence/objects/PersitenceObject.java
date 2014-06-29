package persistence.objects;

import persistence.PersistenceManager;
import persistence.exceptions.PersistenceEndpointIOException;

import java.io.Serializable;
import java.util.UUID;

/**
 * Created by Stefan on 27.05.14.
 */
public abstract class PersitenceObject<T> implements Serializable {

    PersitenceObject(T obj, PersistenceManager manager) throws PersistenceEndpointIOException{}
    public abstract UUID getID();
    public abstract T convertToSourceObject(PersistenceManager manager) throws PersistenceEndpointIOException;

}