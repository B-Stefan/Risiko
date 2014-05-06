package main.java.persistence.objects;

import main.java.gui.CUI.utils.IO;
import main.java.persistence.IPersistenceObject;
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

/**
 * Created by Stefan on 04.05.14.
 */
public class PersistenceGame implements IPersistenceObject<Game> {

    private static String GAME_FILE_NAME = "game.data";
    private HashMap<UUID, Game> chachedGames = new HashMap<UUID, Game>();
    private ObjectOutputStream writer;
    private ObjectInputStream reader;


    private void connectFileSystem () throws IOException{
        if (this.writer == null ){
            PersistenceManager.createDir();
            FileOutputStream fos  = new FileOutputStream(PersistenceManager.convertFileNameToPath(PersistenceGame.GAME_FILE_NAME),false);
            this.writer = new ObjectOutputStream(fos);

            this.reader = new ObjectInputStream(new FileInputStream(PersistenceManager.convertFileNameToPath(PersistenceGame.GAME_FILE_NAME)));
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
    public boolean save(Game newObject) throws IOException{
        this.connectFileSystem();

        return false;
    }

    @Override
    public boolean remove(Game newObject) throws IOException {
        return false;
    }

    @Override
    public void get(UUID id) throws IOException {
        this.readFile();

    }

    @Override
    public List<Game> getAll() throws IOException {
        return null;
    }
}
