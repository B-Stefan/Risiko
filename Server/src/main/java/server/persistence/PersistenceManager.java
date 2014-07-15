/*
 * RISIKO-JAVA - Game, Copyright 2014  Jennifer Theloy, Stefan Bieliauskas  -  All Rights Reserved.
 * Hochschule Bremen - University of Applied Sciences
 *
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 * Contact:
 *     Jennifer Theloy: jTheloy@stud.hs-bremen.de
 *     Stefan Bieliauskas: sBieliauskas@stud.hs-bremen.de
 *
 * Web:
 *     https://github.com/B-Stefan/Risiko
 *
 */

package server.persistence;

import com.sun.jdi.ClassNotPreparedException;
import interfaces.IGame;
import interfaces.data.IMap;
import interfaces.data.IPlayer;
import javafx.print.PageLayout;
import server.logic.Game;
import server.logic.data.Map;
import server.logic.data.Player;
import server.persistence.dataendpoints.PersistenceEndpoint;
import server.persistence.dataendpoints.SerializableFileEndpoint;
import server.persistence.objects.*;

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

    /**
     * Bereits erzeugte endpoints werden hier abgelegt
     */
    private final HashMap<Class,PersistenceEndpoint> endpoints = new HashMap<Class,PersistenceEndpoint>();

    /**
     * Erstellt für eine Logik-Klasse ein PersistensEndpoint
     * @see server.persistence.dataendpoints.PersistenceEndpoint
     * @param type Logik-Klasse
     * @return PerisistensEndpoint<type>
     */
    private PersistenceEndpoint<?> createHandler (Class type){

        if (type == Game.class){
                return new SerializableFileEndpoint<Game>(Game.class,PersistenceGame.class, this);
        }
        else if (type == Player.class ){
                return new SerializableFileEndpoint<Player>(Player.class,PersistencePlayer.class, this);
        }
        else if (type == Map.class){
            return new SerializableFileEndpoint<Map>(Map.class,PersistenceMap.class, this);
        }
        throw new RuntimeException(new ClassNotFoundException("Für die Klasse " + type + " konnte keine Speicherklasse gefudnen werden "));
    }
    /**
     * Erstellt bzw. nimmt den gechachten Handler für die angebene Klasse
     * @param type Logik-Klasse
     * @return Handler s
     */
    private PersistenceEndpoint<?>  getHandler(Class type) {
        if(!endpoints.containsKey(type)){
            endpoints.put(type,this.createHandler(type));
        }
        return endpoints.get(type);
    }

    /**
     * Gibt einen PersistenceEndpoint für die Logik-Klasse Game zurück
     * @return PersistenceEndpoint für Game
     */
    @SuppressWarnings("unchecked")
    public PersistenceEndpoint<Game> getGameHandler(){
        return (PersistenceEndpoint<Game>)this.getHandler(Game.class);
    }
    /**
     * Gibt einen PersistenceEndpoint für die Logik-Klasse Player zurück
     * @return PersistenceEndpoint für Player
     */
    @SuppressWarnings("unchecked")
    public PersistenceEndpoint<Player> getPlayerHandler(){
        return (PersistenceEndpoint<Player>)this.getHandler(Player.class);
    }
    /**
     * Gibt einen PersistenceEndpoint für die Logik-Klasse Map zurück
     * @return PersistenceEndpoint für Map
     */
    @SuppressWarnings("unchecked")
    public PersistenceEndpoint<Map> getMapHandler(){
        return (PersistenceEndpoint<Map>)this.getHandler(Map.class);
    }



}
