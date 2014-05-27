package main.java.persistence.dataendpoints;

import javax.management.InstanceNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * @author Jennifer Theloy, Thu Nguyen, Stefan Bieliauskas
 */
public abstract class PersistenceEndpoint<T> {

    protected Class dataClass;
    protected Class<T> sourceClass;

    public PersistenceEndpoint(final Class<T> sourceClass, final Class dataClass){
        this.dataClass = dataClass;
        this.sourceClass = sourceClass;
    }
    public abstract boolean save(T newObject) throws IOException;
    public abstract boolean remove(T removeObject) throws IOException, InstanceNotFoundException;
    public abstract T get(UUID id) throws IOException;
    public abstract List<T> getAll() throws IOException;
}
