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

package interfaces;

import exceptions.CountryNotInListException;
import exceptions.GameNotFoundException;
import exceptions.PersistenceEndpointIOException;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;


public interface IGameManager extends Remote, Serializable, IToStringRemote {

    /**
     *
     * @return Gibt die Liste aller gespeicherten Spiele zurück
     * @throws exceptions.PersistenceEndpointIOException
     * @throws CountryNotInListException 
     */
    public List<IGame> getSavedGameList() throws PersistenceEndpointIOException, RemoteException;

    /**
     *
     * @return Gibt eine Liste aller aktuell laufenden Spiele zurück, laufend bedeutet zur Laufzeit des Server erzeugten
     * @throws exceptions.PersistenceEndpointIOException
     */
    public List<IGame> getRunningGameList() throws PersistenceEndpointIOException, RemoteException;


    /**
     * Erstellt und speichert dieses neue Spiel ab
     * @return Das neu erstellte Spiel
     * @throws PersistenceEndpointIOException Fehler beim Einlesen der Datei oder Speichern
     */
    public IGame addGame () throws PersistenceEndpointIOException, RemoteException;

    /**
     * Speichert ein Spiel ab
     * @param g  Spiel das gespeichert werden soll
     * @throws PersistenceEndpointIOException
     */
    public void saveGame(IGame g) throws PersistenceEndpointIOException, GameNotFoundException,RemoteException;


    /**
     * Speichert ein Spiel ab
     * @param index Index aus der Liste von @see #getGameList
     * @throws PersistenceEndpointIOException
     * @throws IndexOutOfBoundsException
     * @throws CountryNotInListException 
     */
    public void saveGame(int index)throws PersistenceEndpointIOException, IndexOutOfBoundsException, RemoteException;

    /**
     * Gibt den Client Manager für den GameManager zurück, dieser verwaltet alle geöfneten Clients, die den StartScreen anzeigen
     * @return Client Manager, der für den Start-Screen events bereitstellt
     * @throws RemoteException
     */
    public IClientManager getClientManager() throws RemoteException;

    }
