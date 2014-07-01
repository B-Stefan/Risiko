package server.persistence.dataendpoints;

import exceptions.PersistenceEndpointIOException;
import server.persistence.PersistenceManager;
import server.persistence.objects.PersitenceObject;

import javax.management.InstanceNotFoundException;
import java.util.List;
import java.util.UUID;

/**
 * @author Jennifer Theloy, Stefan Bieliauskas
 *
 * Diese Klasse dient zur Vereinheitlichung aller Endpunkte zum Speichern von Spielen, Spielern, Ländern usw.
 *
 * @see server.persistence.PersistenceManager
 * @param <T> Enhält die Logik-Klasse z.b. IGame oder Player, die gepeichert werden soll
 */
public abstract class PersistenceEndpoint<T> {

    /**
     * Datenklasse, diese Klasse muss serelisierbar sein
     */
    protected final Class dataClass;

    /**
     * Logik-Klasse, die abgespeichert werden soll. Im Prinzip nicht notwendig diese nochmal ab zu speichern, jedoch trägt dies zur Wartbarkeit und Sicherheit bei der Ausführung bei.
     */
    protected final Class<T> sourceClass;

    /**
     * Manager, der alle Endpoints verwaltet
     */
    protected final PersistenceManager manager;

    /**
     * Diese Klasse dient zur Vereinheitlichung der verschiedenen Endpunkte
     * @param sourceClass Klasse, die abgespeichert werden soll
     * @param dataClass Klasse, die zur Serialisierung dient
     * @param manager Manager, der alle anderen Endpunkte zur Speicherung verwaltet
     */
    public PersistenceEndpoint(final Class<T> sourceClass, final Class<? extends PersitenceObject<T>> dataClass, final PersistenceManager manager){
        this.dataClass = dataClass;
        this.sourceClass = sourceClass;
        this.manager = manager;
    }

    /**
     * Speichert eine neue Instanz
     * @param newObject
     * @return True, wenn Speicherung erfolgreich
     * @throws PersistenceEndpointIOException
     */
    public abstract boolean save(T newObject) throws PersistenceEndpointIOException;

    /**
     * Löscht die angegebene Instanz
     * @param removeObject Objekt, das zu löschen ist
     * @return true, wenn gelöscht
     * @throws PersistenceEndpointIOException
     * @throws InstanceNotFoundException
     */
    public abstract boolean remove(T removeObject) throws PersistenceEndpointIOException, InstanceNotFoundException;

    /**
     * Gibt die sourceClass einer bestimmten UUID zurück
     * @param id UUID des Objects
     * @return Objekt, null wenn nicht gefunden
     * @throws PersistenceEndpointIOException
     */
    public abstract T get(UUID id) throws PersistenceEndpointIOException;

    /**
     * Gibt die sourceClass einer bestimmten UUID zurück, dabei wird die UUID als String übergeben
     * @param id UUID als String
     * @return Objekt oder null, wenn nicht gefunden
     * @throws PersistenceEndpointIOException
     */
    public abstract T get(String id) throws PersistenceEndpointIOException;

    /**
     * Gibt alle Instancen des sourceClass zurück als Liste
     * @return Liste aller Instanzen der sourceCLass
     * @throws PersistenceEndpointIOException
     */
    public abstract List<T> getAll() throws PersistenceEndpointIOException;
}
