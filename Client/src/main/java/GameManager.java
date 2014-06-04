package main.java;
import main.java.logic.Game;
import main.java.persistence.PersistenceManager;
import main.java.persistence.dataendpoints.PersistenceEndpoint;
import main.java.persistence.exceptions.PersistenceEndpointIOException;

import java.util.*;
/**
 *
 */
public class GameManager {

    private List<Game> gameList = new ArrayList<Game>();
    private PersistenceEndpoint handler;
    public GameManager(PersistenceManager manager) throws PersistenceEndpointIOException{
        this.handler = manager.getGameHandler();
        this.gameList = this.handler.getAll();
    }
    public  List<Game> getGameList(){
        return this.gameList;
    }
    @Override
    public String toString(){
        return "GameManager";
    }
}
