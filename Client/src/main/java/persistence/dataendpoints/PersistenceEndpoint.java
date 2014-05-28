package main.java.persistence.dataendpoints;

import main.java.persistence.PersistenceManager;
import main.java.persistence.objects.PersitenceObject;
import main.java.persistence.exceptions.*;

import javax.management.InstanceNotFoundException;
import java.util.List;
import java.util.UUID;

/**
 * @author Jennifer Theloy, Thu Nguyen, Stefan Bieliauskas
 */
public abstract class PersistenceEndpoint<T> {

    protected final Class dataClass;
    protected final Class<T> sourceClass;
    protected final PersistenceManager manager;
    public PersistenceEndpoint(final Class<T> sourceClass, final Class<? extends PersitenceObject<T>> dataClass, final PersistenceManager manager){
        this.dataClass = dataClass;
        this.sourceClass = sourceClass;
        this.manager = manager;
    }
    public abstract boolean save(T newObject) throws PersistenceEndpointIOException;
    public abstract boolean remove(T removeObject) throws PersistenceEndpointIOException, InstanceNotFoundException;
    public abstract T get(UUID id) throws PersistenceEndpointIOException;
    public abstract T get(String id) throws PersistenceEndpointIOException;
    public abstract List<T> getAll() throws PersistenceEndpointIOException;
}
