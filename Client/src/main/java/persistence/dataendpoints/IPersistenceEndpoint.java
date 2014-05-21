package main.java.persistence.dataendpoints;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * @author Jennifer Theloy, Thu Nguyen, Stefan Bieliauskas
 */
public interface IPersistenceEndpoint<T> {

    public boolean save(T newObject) throws IOException;
    public boolean remove(T newObject) throws IOException;
    public void get(UUID id) throws IOException;
    public List<T> getAll() throws IOException;
}
