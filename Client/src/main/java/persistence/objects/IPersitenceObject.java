package main.java.persistence.objects;

import java.io.Serializable;
import java.util.UUID;

/**
 * Created by Stefan on 27.05.14.
 */
public interface IPersitenceObject extends Serializable {

    public UUID getID();


}
