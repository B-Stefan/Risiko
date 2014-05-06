package main.java.persistence.objects;

import main.java.persistence.IPersistenceObject;
import main.java.logic.Game;
import main.java.persistence.PersistenceManager;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Created by Stefan on 04.05.14.
 */
public class PersistenceGame implements IPersistenceObject<Game> {

    private static String GAME_FILE_NAME = "game.data";
    private HashMap<UUID, Game> chachedGames = new HashMap<UUID, Game>();
    private ObjectOutputStream reader;


    private void connectFileSystem () throws IOException{
        if (this.reader == null ){
            PersistenceManager.createDir();
            FileOutputStream fos  = new FileOutputStream(PersistenceManager.convertFileNameToPath(PersistenceGame.GAME_FILE_NAME),false);
            this.reader = new ObjectOutputStream(fos);
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

    }

    @Override
    public List<Game> getAll() throws IOException {
        return null;
    }
}
