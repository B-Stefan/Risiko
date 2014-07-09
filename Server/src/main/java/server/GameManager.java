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

package server;

import exceptions.CountryNotInListException;
import exceptions.GameNotFoundException;
import exceptions.PersistenceEndpointIOException;
import interfaces.IGame;
import interfaces.IGameManager;
import server.logic.Game;
import server.persistence.PersistenceManager;
import server.persistence.dataendpoints.PersistenceEndpoint;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
/**
 * Verwaltet eine Anzahl an Games und stellt Methdoen zum Seichern und Anzeigen der gespeicherten Spiele zur Verfügung
 */
public class GameManager extends UnicastRemoteObject implements IGameManager {

    /**
     * Handler, der die Interaktion zum File-System oder DB-System verwaltet
     */
    private final PersistenceEndpoint handler;

    /**
     * Diese Liste beinhaltet alle Spiele, bei denen aktuell Spieler angemeldet sind
     */
    private final Map<UUID,Game> runningGames = new HashMap<UUID,Game>();

    /**
     * Verwaltet mehrere Spiele
     * @param manager - Manager, der die
     */
    public GameManager(final PersistenceManager manager) throws RemoteException{
        this.handler = manager.getGameHandler();
    }
    /**
     *
     * @return Gibt die Liste aller gespeicherten Spiele zurück
     * @throws PersistenceEndpointIOException
     * @throws CountryNotInListException 
     */
    public List<IGame> getSavedGameList() throws PersistenceEndpointIOException, RemoteException{
        return this.handler.getAll();
    }

    /**
     *
     * @return Gibt die Liste aller gespeicherten Spiele zurück
     * @throws PersistenceEndpointIOException
     */
    public List<IGame> getRunningGameList() throws PersistenceEndpointIOException, RemoteException{
        List<IGame> runningGames = new ArrayList<IGame>();
        for(Map.Entry<UUID,Game> entry : this.runningGames.entrySet()){
            runningGames.add(entry.getValue());
        }
        return runningGames;
    }


    /**
     * Erstellt und speichert dieses neue Spiel ab
     * @return Das neu erstellte Spiel
     * @throws PersistenceEndpointIOException Fehler beim Einlesen der Datei oder Speichern
     */
    public Game addGame () throws PersistenceEndpointIOException, RemoteException{
        Game newGame = new Game(handler);
        this.runningGames.put(newGame.getId(),newGame);
        return newGame;
    }

    /**
     * Speichert ein Spiel ab
     * @param g  Spiel das gespeichert werden soll
     * @throws PersistenceEndpointIOException
     */
    public void saveGame(IGame g) throws PersistenceEndpointIOException,GameNotFoundException, RemoteException{
        Game gameToSave = runningGames.get(g.getId());
        if(gameToSave == null){
            throw new GameNotFoundException();
        }
        else {
            this.handler.save(gameToSave);
        }
    }
    /**
     * Speichert ein Spiel ab
     * @param index Index aus der Liste von @see #getGameList
     * @throws PersistenceEndpointIOException
     * @throws IndexOutOfBoundsException
     * @throws CountryNotInListException 
     */
    public void saveGame(int index)throws PersistenceEndpointIOException, IndexOutOfBoundsException, RemoteException{
        List<Game> gameList = this.handler.getAll();
        Game gameToSave =  gameList.get(index);
        this.handler.save(gameToSave);
    }
    @Override
    public String toString(){
        return "GameManager";
    }

    /**
     * ToString methode, die Remote aufgerufen werden kann
     * @return
     * @throws RemoteException
     */
    public String toStringRemote() throws RemoteException{
        return this.toString();
    }

}
