package main.java.logic;
import main.java.persistence.PersistenceManager;
import main.java.persistence.dataendpoints.PersistenceEndpoint;
import main.java.persistence.exceptions.PersistenceEndpointIOException;

import java.util.*;
/**
 * Created by Stefan on 28.05.14.
 */
public class GameManager {

    private List<Game> gameList = new ArrayList<Game>();
    private PersistenceEndpoint handler;
    public GameManager(PersistenceManager manager) throws PersistenceEndpointIOException{
        this.handler = manager.getGameHandler();
        gameList = this.handler.getAll();
    }
    public  List<Game> getGameList(){return this.gameList;}
    public static void main(String[]args){
        PersistenceManager pers = new PersistenceManager();
        GameManager man;
        try {
             man = new GameManager(pers);
        }catch (PersistenceEndpointIOException e){
            e.printStackTrace();
            return;
        }
        for(Game game : man.getGameList()){
            System.out.println(game.toString());
        }

    }
}
