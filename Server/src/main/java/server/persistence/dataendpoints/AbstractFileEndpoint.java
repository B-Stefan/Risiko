package server.persistence.dataendpoints;

import exceptions.PersistenceEndpointIOException;
import server.persistence.PersistenceManager;
import server.persistence.objects.PersitenceObject;

import javax.management.InstanceNotFoundException;
import java.io.*;

import java.util.*;
import java.lang.reflect.*;

/**
 * Diese Klasse dient zur Speicherung von Spielen, dabei wird das Spiel Dateibasiert abgespeochert.
 *
 * Das System ist darauf augelegt weitere Möglichkeiten der Speicherung zu implementieren
 *
 * @see server.persistence.dataendpoints.PersistenceEndpoint
 * @param <T> - Logik-Klasse, die sereialsiiert werden soll  @see PersistenceManager
 *
 */
public abstract class AbstractFileEndpoint<T> extends PersistenceEndpoint<T> {


    /**
     * Pfad, der zur Speicherung der Dateien verwendet wird
     */
    public static final String DEFAULT_PATH  = "data/";

    /**
     * Beinhaltet, ob der Ordner bereits erstellt, bzw. besteht
     */
    private static Boolean isDirCreated = false;

    /**
     * Erstellt den Ordner in der Default_PATH
     */
    public static void createDir (){
        if(!AbstractFileEndpoint.isDirCreated){
            File dir = new File(DEFAULT_PATH);
            dir.mkdirs();
            AbstractFileEndpoint.isDirCreated = true;
        }
    }

    /**
     * Konvertiert einen Dateinamen in eine qualifizierten Pfad
     * @param fileName Name des files
     * @return qualifizierten Pfad
     */
    public static String convertFileNameToPath(final String fileName){

        return DEFAULT_PATH + fileName.replace("/","");     //Falls file name mit "/filename" angegbeen wurde"
    }

    /**
     * Dateiname für diesen Endpoint
     */
    protected final String fileName;

    /**
     * Hashmap für alle Objekt, die in der Datei gespeichert wurden
     */
    protected HashMap<UUID, PersitenceObject<T>> chachedObjects = new HashMap<UUID, PersitenceObject<T>>();

    /**
     * Beinhaltet alle Objekte, die bereits einmal geladen wurden.
     */
    protected HashMap<UUID, T> chachedSourceObjects = new HashMap<UUID,T>();


    /**
     * Erstellt einen Endpoint für eine bestimmte Logic-Klasse.
     * z.B.: new AbstractFileEndpoint<IGame>(IGame.class, PersistenceGame.class, manager);
     * Diese Klasse dient dann zur Veraltung aller Spiele und bietet die einfache Möglichkeit ein Spiel zu Speichern oder zu laden.
     * Die Serialisierung geschieht dabei vollkommen automamtisiert.
     * @param sourceClass - Klasse der Spiellogic, die gespeichert werden soll
     * @param dataClass  - Zwischenklasse, die dazu dient die Eigenschaften fest zu legen die abgespeichert werden sollen.
     * @param manager - Der Manager, der zur Verwaltung aller PersistenceEndpoints gedacht ist.
     */
    public AbstractFileEndpoint(final Class<T> sourceClass, final Class<? extends PersitenceObject<T>> dataClass, final PersistenceManager manager){
        super(sourceClass,dataClass, manager);
        this.fileName = this.sourceClass.getName();
    }


    /**
     * Liest den File und packt alle Objekte des Files in die Liste chacedObjects
     */
    protected abstract void readFile ();

    /**
     * Schreibt die Liste chachedObjetcts in die Datei
     */
    protected abstract void writeFile ();

    /**
     * Speichert ein Object vom Typ T in der Datei
     * @param newObject Das Objekt, das gespeichert werden soll
     * @return true, wenn die Instance gepseichert wurde
     * @throws PersistenceEndpointIOException Sollte ein Fehler auftreten, beim lesen oder schreiben der Datei wird diese Exception ausgelöst
     */
    @Override
    public boolean save(T newObject) throws PersistenceEndpointIOException{
        this.readFile();
        PersitenceObject instanceToSave = this.convertToSerializableInstance(newObject);
        this.putObjectToHashmap(instanceToSave);
        this.writeFile();
        return true;
    }

    /**
     * Löscht das Objekt aus der Datei
     * @param removeObject Objekt, das zu löschen ist
     * @return True, wenn das Objekt gelöscht wurde
     * @throws PersistenceEndpointIOException Sollte ein Fehler auftreten, beim Lesen oder Schreiben der Datei wird diese Exception ausgelöst
     * @throws InstanceNotFoundException Wenn der Parameter #removeObjct nicht gefunden wurde, wird diese Exception ausgelöst
     */
    @Override
    public boolean remove(T removeObject) throws PersistenceEndpointIOException, InstanceNotFoundException {
        this.readFile();
        PersitenceObject instanceToRemove = this.convertToSerializableInstance(removeObject);
        if(this.chachedObjects.containsKey(instanceToRemove.getID())){
            this.chachedObjects.remove(instanceToRemove.getID());
            return true;
        }
        throw  new InstanceNotFoundException();
    }

    /**
     * Läd und Konvertiert die UUID in ein sourceObject
     * @param id Die ID des Spiels
     * @return
     * @throws PersistenceEndpointIOException
     */
    @Override
    public T get(UUID id) throws PersistenceEndpointIOException{
        PersitenceObject<T> result = this.chachedObjects.get(id);
        if(result == null){
            this.readFile();
        }
        return this.convertToSourceType(result!=null ? result : this.chachedObjects.get(id));
    }

    /**
     * Läd und Konvertiert eine UUID als String in ein sourceObject
     * @param id String, der eine UUID enthält
     * @return
     * @throws PersistenceEndpointIOException
     */
    @Override
    public T get(String id) throws PersistenceEndpointIOException {
        return this.get(UUID.fromString(id));
    }

    /**
     * Gibt alle SourceObjects aus, die in der Datei gespeichert wurden
     * @return Liste aller  SourceObjects
     * @throws PersistenceEndpointIOException
     */
    @Override
    public List<T> getAll() throws PersistenceEndpointIOException{
        this.readFile();
        List<T> list = new ArrayList<T>();
        for(Map.Entry<UUID,PersitenceObject<T>> entry : this.chachedObjects.entrySet()){
            T sourceInstance = this.convertToSourceType(entry.getValue());
            list.add(sourceInstance);
        }
        return list;
    }


    /**
     * Speichert ein sourceObject in der chachedObjects Liste
     * @param objectToStore
     */
    private void putObjectToHashmap (PersitenceObject<T> objectToStore){
        /**
         * Aus Cache entfernen, wenn vorhanden
         */
        if(this.chachedObjects.containsKey(objectToStore.getID())){
          this.chachedObjects.remove(objectToStore.getID());
        }
        this.chachedObjects.put(objectToStore.getID(),objectToStore);
    }

    /**
     * Konvertiert ein SourceObject in ein serialisierbares Object
     * @param obj
     * @return
     */
    private PersitenceObject<T> convertToSerializableInstance (T obj){
        if(obj == null){
            return null;
        }
        PersitenceObject<T> instance = null;
        try {
            Constructor ctor = dataClass.getDeclaredConstructor(this.sourceClass,PersistenceManager.class);
            instance = (PersitenceObject<T>) ctor.newInstance(obj,manager);
        }catch (InstantiationException | IllegalAccessException | InvocationTargetException | ClassCastException e){
            throw new RuntimeException(e);
        }
        catch (NoSuchMethodException e ){
            e.printStackTrace();
            throw new RuntimeException("Die Klasse " + this.dataClass.getName()  + " enthält keinen Konstruktor für " + this.sourceClass.getName());
        }
        return instance;
    }

    /**
     * Konvertiert ein serialisiertes Objekt in ein SourceObject
     * @param obj
     * @return
     * @throws PersistenceEndpointIOException
     */
    private T convertToSourceType(PersitenceObject obj) throws PersistenceEndpointIOException{
        if(obj == null){
            return null;
        }
        T cacheResult = this.chachedSourceObjects.get(obj.getID());
        if(cacheResult == null ){
            T result = (T) obj.convertToSourceObject(this.manager);
            this.chachedSourceObjects.put(obj.getID(),result);
            return result;
        }
        else {
            return cacheResult;
        }

    }

}
