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

package commons.interfaces.data;
import commons.interfaces.IToStringRemote;

import java.awt.Color;
import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.UUID;


public interface ICountry extends Remote, Serializable, IToStringRemote {

    /**
     * Gibt den das Land mit der entsprechenden UUID zurück
     * @param id UUID des Landes was benötigt wird
     * @return Land das gesucht wurde, oder null
     */
    public ICountry getNeighbor (UUID id) throws RemoteException;
    /**
     * Gibt das Land anhand seines Namens zurück. Dabei wird das erste Land, dass diesem Namen entsprecht zurückgegeben.
     * @param searchName - Name nach dem gesucht werden soll
     * @return Land das gesucht wurde oder null wenn nicht gefunden
     */
    public ICountry getNeighbor (String searchName) throws RemoteException;
    /**
     * Gibt alle Nachbarn des Landes zurück
     * @return Nachbarn des Landes
     */
    public List<? extends ICountry> getNeighbors () throws RemoteException;
    /**
    *
    * @return Anzahl der Armeen auf dem Land
    */
    public int getNumberOfArmys() throws RemoteException;
    /**
     * @return Den Spieler, der dieses Land gerade besetzt hat
     */
    public IPlayer getOwner() throws RemoteException;
    /**
     * @return Name des Lands
     */
    public String getName() throws RemoteException;
    /**
     * @return Die unique Id für das Land
     */
    public UUID getId() throws RemoteException;
    /**
     * @return Die Farbe des Countrys
     */
    public Color getColor() throws RemoteException;

    /**
     * @return DIe Anzahl der Armeen auf dem Land
     */
    public int getArmySize() throws RemoteException;
    
    /**
     * Getter für das Kürzel
     * @return Kürzel
     */
    public String getShortName() throws RemoteException;

}
