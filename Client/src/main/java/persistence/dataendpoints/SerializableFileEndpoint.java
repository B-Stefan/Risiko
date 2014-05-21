package main.java.persistence.dataendpoints;

import main.java.logic.Game;
import main.java.persistence.PersistenceManager;

import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.io.Serializable;
import java.lang.reflect.*;

/**
 * Created by Stefan on 04.05.14.
 */
public class SerializableFileEndpoint <T> implements IPersistenceEndpoint<T>, Serializable{

    private static String GAME_FILE_NAME = "game.data";
    private HashMap<UUID, Game> chachedGames = new HashMap<UUID, Game>();
    private ObjectOutputStream writer;
    private ObjectInputStream reader;
    private Class dataClass;

    public SerializableFileEndpoint (Class cls){
        this.dataClass = cls;
    }
    private void connectFileSystem () throws IOException{
        if (this.writer == null ){
            PersistenceManager.createDir();
            FileOutputStream fos  = new FileOutputStream(PersistenceManager.convertFileNameToPath(SerializableFileEndpoint.GAME_FILE_NAME),false);
            this.writer = new ObjectOutputStream(fos);
            this.reader = new ObjectInputStream(new FileInputStream(PersistenceManager.convertFileNameToPath(SerializableFileEndpoint.GAME_FILE_NAME)));
        }

    }

    private void readFile () throws IOException{
        this.connectFileSystem();
        try {
            Object obj = this.reader.readObject();
        }catch (ClassNotFoundException e){
            throw new RuntimeException(e);
        }
    }
    @Override
    public boolean save(T newObject) throws IOException{
        this.connectFileSystem();
        Constructor[] ctor = this.dataClass.getConstructors();
        Object instanceToSave;
        try {
            instanceToSave = ctor[0].newInstance(new Object[]{newObject});
        }catch (Exception e ){
            throw new RuntimeException(e);
        }
        return false;
    }

    @Override
    public boolean remove(T newObject) throws IOException {
        return false;
    }

    @Override
    public void get(UUID id) throws IOException {
        this.readFile();

    }

    @Override
    public List<T> getAll() throws IOException {
        return null;
    }
}
