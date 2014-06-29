package persistence;

/**
 * @author Jennifer Theloy, Thu Nguyen, Stefan Bieliauskas
 */

import interfaces.IGame;
import interfaces.data.IMap;
import interfaces.data.IPlayer;
import logic.data.Player;
import persistence.dataendpoints.AbstractFileEndpoint;
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

        if (type == IGame.class){
                return new SerializableFileEndpoint<IGame>(IGame.class,PersistenceGame.class, this);
        }
        else if (type == IPlayer.class ){
                return new SerializableFileEndpoint<IPlayer>(IPlayer.class,PersistencePlayer.class, this);
        }
        else if (type == IMap.class){
            return new SerializableFileEndpoint<IMap>(IMap.class,PersistenceMap.class, this);
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
    public PersistenceEndpoint<IGame> getGameHandler(){
        return (PersistenceEndpoint<IGame>)this.getHandler(IGame.class);
    }
    public PersistenceEndpoint<IPlayer> getPlayerHandler(){
        return (PersistenceEndpoint<IPlayer>)this.getHandler(Player.class);
    }
    public PersistenceEndpoint<IMap> getMapHandler(){
        return (PersistenceEndpoint<IMap>)this.getHandler(IMap.class);
    }



}
