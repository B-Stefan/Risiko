package main.java.persistence.dataendpoints;

import main.java.persistence.exceptions.*;
import main.java.persistence.PersistenceManager;
import main.java.persistence.objects.PersitenceObject;

import javax.management.InstanceNotFoundException;
import java.io.*;

import java.util.*;
import java.lang.reflect.*;

/**
 * Created by Stefan on 04.05.14.
 */
public class SerializableFileEndpoint <T> extends PersistenceEndpoint<T> {


    public static final String DEFAULT_PATH  = "data/";
    private static Boolean isDirCreated = false;

    public static void createDir (){
        if(!SerializableFileEndpoint.isDirCreated){
            File dir = new File(DEFAULT_PATH);
            dir.mkdirs();
            SerializableFileEndpoint.isDirCreated = true;
        }
    }
    public static String convertFileNameToPath(final String fileName){

        return DEFAULT_PATH + fileName.replace("/","");     //Falls file name mit "/filename" angegbeen wurde"
    }

    private String fileName;
    private HashMap<UUID, PersitenceObject> chachedObjects = new HashMap<UUID, PersitenceObject>();



    public SerializableFileEndpoint(final Class<T> sourceClass,final Class<? extends PersitenceObject<T>> dataClass, final PersistenceManager manager){
        super(sourceClass,dataClass, manager);
        this.fileName = this.sourceClass.getName();
    }



    private void readFile (){
        SerializableFileEndpoint.createDir();
        HashMap<UUID, PersitenceObject> fileData = null;
        try {
            ObjectInputStream reader = new ObjectInputStream(new FileInputStream(SerializableFileEndpoint.convertFileNameToPath(this.fileName)));
            chachedObjects = (HashMap<UUID, PersitenceObject>) reader.readObject();
        }catch (ClassNotFoundException e){
            throw new RuntimeException(e);
        }
        catch (EOFException e){
            //File ist leer

        }
        catch (InvalidClassException e){

        }
        catch (FileNotFoundException e){
            this.writeFile(); // File erstellen, wenn nicht vorhanden
        }
        catch (IOException e){
            //@Todo Besseres Exception Handling
            e.printStackTrace();
        }
        if(fileData != null){
            this.chachedObjects = fileData;
        }
    }
    private void writeFile ()  {
        try {
            FileOutputStream fos  = new FileOutputStream(SerializableFileEndpoint.convertFileNameToPath(this.fileName),false);
            ObjectOutputStream  writer = new ObjectOutputStream(fos);
            writer.writeObject(this.chachedObjects);
            writer.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }

    }

    @Override
    public boolean save(T newObject) throws PersistenceEndpointIOException{
        this.readFile();
        PersitenceObject instanceToSave = this.convertToSerializableInstance(newObject);
        this.putObjectToHashmap(instanceToSave);
        this.writeFile();
        return true;
    }

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
    @Override
    public T get(UUID id) throws PersistenceEndpointIOException{
        this.readFile();
        return this.convertToSourceType(this.chachedObjects.get(id));
    }
    @Override
    public T get(String id) throws PersistenceEndpointIOException {
        return this.get(UUID.fromString(id));
    }

    @Override
    public List<T> getAll() throws PersistenceEndpointIOException{
        this.readFile();
        List<T> list = new ArrayList<T>();
        for(Map.Entry<UUID,PersitenceObject> entry : this.chachedObjects.entrySet()){
            T sourceInstance = this.convertToSourceType(entry.getValue());
            list.add(sourceInstance);
        }
        return list;
    }


    private void putObjectToHashmap (PersitenceObject objectToStore){
        /**
         * Aus Cache entfernen, wenn vorhanden
         */
        if(this.chachedObjects.containsKey(objectToStore.getID())){
          this.chachedObjects.remove(objectToStore.getID());
        }
        this.chachedObjects.put(objectToStore.getID(),objectToStore);
    }

    private PersitenceObject convertToSerializableInstance (T obj){
        if(obj == null){
            return null;
        }

        PersitenceObject instance = null;
        try {
            Constructor ctor = dataClass.getDeclaredConstructor(this.sourceClass);
            instance = (PersitenceObject) ctor.newInstance(obj);
        }catch (InstantiationException | IllegalAccessException | InvocationTargetException | ClassCastException e){
            throw new RuntimeException(e);
        }
        catch (NoSuchMethodException e ){
            e.printStackTrace();
            //@todo Besseres Behandeln dieses Fehlers;
        }
        return instance;
    }
    private T convertToSourceType(PersitenceObject obj) throws PersistenceEndpointIOException{
        if(obj == null){
            return null;
        }
        return (T) obj.convertToSourceObject(this.manager);
    }

}
