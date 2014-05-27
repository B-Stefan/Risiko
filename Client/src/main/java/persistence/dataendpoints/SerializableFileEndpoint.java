package main.java.persistence.dataendpoints;

import main.java.persistence.PersistenceManager;
import main.java.persistence.objects.IPersitenceObject;

import javax.management.InstanceNotFoundException;
import java.io.*;

import java.util.*;
import java.lang.reflect.*;

/**
 * Created by Stefan on 04.05.14.
 */
public class SerializableFileEndpoint <T> extends PersistenceEndpoint<T> {

    private String fileName;
    private HashMap<UUID, IPersitenceObject> chachedObjects = new HashMap<UUID, IPersitenceObject>();



    public SerializableFileEndpoint(final Class<T> sourceClass,final Class dataClass){
        super(sourceClass,dataClass);
        this.fileName = this.sourceClass.getName();
    }



    private void readFile () throws IOException{
        HashMap<UUID, IPersitenceObject> fileData = null;
        ObjectInputStream reader = new ObjectInputStream(new FileInputStream(PersistenceManager.convertFileNameToPath(this.fileName)));
        try {
            chachedObjects = (HashMap<UUID, IPersitenceObject>) reader.readObject();
        }catch (ClassNotFoundException e){
            throw new RuntimeException(e);
        }
        catch (EOFException e){
            //File ist leer

        }
        if(fileData != null){
            this.chachedObjects = fileData;
        }
    }
    private void writeFile () throws  IOException {
        FileOutputStream fos  = new FileOutputStream(PersistenceManager.convertFileNameToPath(this.fileName),false);
        ObjectOutputStream  writer = new ObjectOutputStream(fos);
        writer.writeObject(this.chachedObjects);
        writer.close();
    }

    @Override
    public boolean save(T newObject) throws IOException {
        this.readFile();
        IPersitenceObject instanceToSave = this.convertToSerializableInstance(newObject);
        this.putObjectToHashmap(instanceToSave);
        this.writeFile();
        return true;
    }

    @Override
    public boolean remove(T removeObject) throws IOException, InstanceNotFoundException {
        this.readFile();
        IPersitenceObject instanceToRemove = this.convertToSerializableInstance(removeObject);
        if(this.chachedObjects.containsKey(instanceToRemove.getID())){
            this.chachedObjects.remove(instanceToRemove.getID());
            return true;
        }
        throw  new InstanceNotFoundException();
    }
    @Override
    public T get(UUID id) throws IOException {
        this.readFile();
        return this.convertToSourceType(this.chachedObjects.get(id));
    }

    @Override
    public List<T> getAll() throws IOException {
        this.readFile();
        List<T> list = new ArrayList<T>();
        for(Map.Entry<UUID,IPersitenceObject> entry : this.chachedObjects.entrySet()){
            T sourceInstance = this.convertToSourceType(entry.getValue());
            list.add(sourceInstance);
        }
        return list;
    }


    private void putObjectToHashmap (IPersitenceObject objectToStore){
        /**
         * Aus Cache entfernen, wenn vorhanden
         */
        if(this.chachedObjects.containsKey(objectToStore.getID())){
          this.chachedObjects.remove(objectToStore.getID());
        }
        this.chachedObjects.put(objectToStore.getID(),objectToStore);
    }

    private IPersitenceObject convertToSerializableInstance (T obj){
        IPersitenceObject instance = null;
        try {
            Constructor ctor = dataClass.getDeclaredConstructor(this.sourceClass);
            instance = (IPersitenceObject) ctor.newInstance(obj);
        }catch (InstantiationException | IllegalAccessException | InvocationTargetException | ClassCastException e){
            throw new RuntimeException(e);
        }
        catch (NoSuchMethodException e ){
            e.printStackTrace();
            //@todo Besseres Behandeln dieses Fehlers;
        }
        return instance;
    }
    private T convertToSourceType(IPersitenceObject obj){
        if (obj == null){
            return  null;
        }
        T instance = null;
        try {
            Constructor ctor = sourceClass.getDeclaredConstructor(this.dataClass);
            instance = (T) ctor.newInstance(obj);
        }catch (InstantiationException | IllegalAccessException | InvocationTargetException | ClassCastException e){
            throw new RuntimeException(e);
        }
        catch (NoSuchMethodException e ){
            e.printStackTrace();
            //@todo Besseres Behandeln dieses Fehlers;
        }
        return instance;
    }

}
