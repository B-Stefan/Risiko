package persistence;

/**
 * @author Jennifer Theloy, Thu Nguyen, Stefan Bieliauskas
 */

import logic.Game;
import logic.data.Map;
import logic.data.Player;
import persistence.dataendpoints.PersistenceEndpoint;
import persistence.dataendpoints.SerializableFileEndpoint;
import persistence.objects.*;

import java.util.HashMap;

/**
 * Der PeristenceManger dient zur Verwaltung aller Endpunkte, an denen Spielklassen, nachfolgend sourceClass bezeichnet, gespeichert werden können.
 * Dabei kann der PersistenceManager mehrere dieser Endpunkte bereitstellen.
 *
 * Die Endpunkte bestimmen dann genau wie und wo die Daten gespeichert werden. Dies kann Dateibassiert geschehen oder aber in einer Datenbank, genauso wäre eine Aufspaltung zwischen teilweise Datenbank und teilweise Dateibassiert möglich.
 *
 * Die Klassen im Paket objects dienen zur Serialisierung der Spielklassen, sodass z.B. nicht alle Armeen als einzlene Klasse gespeichert werden sondern nur ein Spieler mit seinen Ländern und die Anzahl der Armeen auf diesem Land.
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
        else if (type == Map.class){
            return new SerializableFileEndpoint<Map>(Map.class,PersistenceMap.class, this);
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
    public PersistenceEndpoint<Map> getMapHandler(){
        return (PersistenceEndpoint<Map>)this.getHandler(Map.class);
    }



}
