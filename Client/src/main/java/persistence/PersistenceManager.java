package main.java.persistence;

/**
 * @author Jennifer Theloy, Thu Nguyen, Stefan Bieliauskas
 */

import main.java.logic.Game;
import main.java.logic.data.Player;
import main.java.persistence.dataendpoints.PersistenceEndpoint;
import main.java.persistence.dataendpoints.SerializableFileEndpoint;
import main.java.persistence.objects.*;

import java.util.HashMap;

/**
 *
 */
public class PersistenceManager {

    private HashMap<Class,PersistenceEndpoint> endpoints = new HashMap<Class,PersistenceEndpoint>();

    private PersistenceEndpoint<?> createHandler (Class type){

        if (type == Game.class){
                return new SerializableFileEndpoint<Game>(Game.class,PersistenceGame.class, this);
        }
        else if (type == Player.class){
                return new SerializableFileEndpoint<Player>(Player.class,PersistencePlayer.class, this);
        }
        //@todo add more types
        return null;
    }
    private PersistenceEndpoint<?>  getHandler(Class type) {
        if(!endpoints.containsKey(type)){
            endpoints.put(type,this.createHandler(type));
        }
        return endpoints.get(type);
    }
    public PersistenceEndpoint<Game> getGameHandler(){
        return (PersistenceEndpoint<Game>)this.getHandler(Game.class);
    }
    public PersistenceEndpoint<Player> getPlayerHandler(){
        return (PersistenceEndpoint<Player>)this.getHandler(Player.class);
    }



}
