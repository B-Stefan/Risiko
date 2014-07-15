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

package server.persistence.objects;
import commons.exceptions.*;
import commons.interfaces.IGame;
import commons.interfaces.data.IPlayer;
import Client.logic.Game;
import Client.logic.data.Map;
import Client.logic.data.Player;
import Client.logic.data.orders.OrderManager;
import server.persistence.PersistenceManager;
import server.persistence.dataendpoints.PersistenceEndpoint;

import java.rmi.RemoteException;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Data-Object Klasse, die zur Umwandlung eines Logic-Objects in ein Persistierbares Object.
 * Dies dient dazu unnötige Informationen von benötigten Informationen zu trennen.
 * (normalisierung, der Daten )
 *
 * z.B. Die Klasse Country enhält ein Attribut Owner, genauso wie der Player eine Liste der Länder besitzt.
 * Die Zuordnung von Player zu Country ist also doppelt und kann somit verlustfrei reduziert werden.
 *
 * @see server.persistence.objects.PersitenceObject
 */
public class PersistenceGame extends PersitenceObject<Game> {

    /**
     * Liste aller Spieler mit ihren IDS
     */
    public final List<String> players = new ArrayList<String>();
    /**
     * ID des Spiels
     */
    private final UUID id;

    /**
     * Name des Spiels
     */
    private final String name;

    /**
     * Aktueller Spielstatus
     */
    private final IGame.gameStates gameState;

    /**
     * ID Der Karte
     */
    private final UUID map;

    /**
     * Ersstellt ein instanz, die Serialisierbar ist (vernüftig sereialisierbar)
     * @param game Das Game das gespeichert werden soll
     * @param manager Der Manager, der zur zum speichern, laden verwnendet werden soll
     * @throws PersistenceEndpointIOException
     */
    public PersistenceGame(Game game, PersistenceManager manager) throws PersistenceEndpointIOException{
        super(game, manager);
        try {
            this.id =  game.getId();
            this.map = game.getMap().getId();
            this.gameState = game.getCurrentGameState();
            this.name = game.getName();
            PersistenceEndpoint<Player> playerHandler = manager.getPlayerHandler();
            for(IPlayer p : game.getPlayers()){
                Player player;
                try {
                    player = (Player) p;
                }catch (ClassCastException e){
                    throw new RuntimeException(e);
                }
                this.players.add(p.getId().toString());
                playerHandler.save(player);
            }
            manager.getMapHandler().save(game.getMap());
        }catch (RemoteException e){
            //Kann nicht auftreten, da keine Netzwerkkommunikation stattfindet
            throw new RuntimeException(e);
        }

    }


    @Override
    public UUID getID() {
        return this.id;
    }

    /**
     * Verwandelt ein gespeichert Spiel wieder in ein richtes Spiel
     * @param manager Der Manager, der zur zum speichern, laden verwnendet werden soll
     * @return Logik-Objekt, in dieem Fall ein Spiel
     * @throws PersistenceEndpointIOException
     */
    @Override
    public Game convertToSourceObject(PersistenceManager manager) throws PersistenceEndpointIOException{
        try {
            Map storedMap = manager.getMapHandler().get(this.map);
            Game newGame = new Game(manager.getGameHandler(),storedMap, this.id);
            newGame.setName(this.name);
            List<Player> players= new ArrayList<Player>();
            PersistenceEndpoint<Player> playerHandler =  manager.getPlayerHandler();

            /**
             * Spieler wiederherstellen
             */
            for(String uuid : this.players) {
                players.add(playerHandler.get(uuid));
            }
            newGame.addPlayers(players);
            newGame.setCurrentGameState(this.gameState);

            /**
             * Order für Spieler
             */
            try{
                OrderManager.createOrdersForPlayers(newGame.getPlayers(),newGame,newGame.getMap());
            }catch (PlayerAlreadyHasAnOrderException e){
                throw  new PersistenceEndpointIOException(e);
            }


            /**
             * Runde setzten, wenn es Spieler gibt
             */
            if (newGame.getPlayers().size() > 0 ) {
                try {
                    newGame.setNextRound();
                }catch (ToManyNewArmysException | RoundNotCompleteException | GameNotStartedException | GameIsCompletedException | RemoteException e){
                    throw new PersistenceEndpointIOException(e);
                }
            }

            return newGame;
        }catch (RemoteException e){
            //Kann nicht auftreten, da keine Kommunikation üerber netzwerk stattfindet
            throw new RuntimeException(e);
        }
    }
}
