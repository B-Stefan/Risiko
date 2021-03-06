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

package server.logic.data;

import commons.interfaces.data.*;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.Map;


/**
 * @author Jennifer Theloy,  Stefan Bieliauskas
 *
 * Dise Klasse bildet ein einzelnes Kontinent ab
 */
public class Continent extends UnicastRemoteObject implements IContinent{

	/**
	 * Name des Kontinents
	 */
	private final String name;

	/**
	 * Eine ArrayLsit mit den Ländern, die sich auf dem Kontinent befinden Länder
	 */
	private final HashMap<UUID,Country> countrys = new HashMap<UUID, Country>();

	/**
	 * der Bonus, der die Kontrolle des jeweiligen Kontinents einbringt für die neuen Armeen
	 */
	private final int bonus;

    /**
     * Erstellt ein Kontinent
     * @param name Name des Kontinents
     * @param bonus Bonus, der der Owner pro Runde an Armeen erhält
     */
	public Continent (final String name, int bonus) throws RemoteException{
		this.name = name;
		this.bonus = bonus;
	}

	/**
	 * Gibt den namen des Kontinents zurück
	 * @return Gibt den Namen des KOntinents zurück
	 */

	public String getName() throws RemoteException{
		return this.name;
	}


    /**
     * Ermittelt den aktuellen Besitzter des Kontinents, prüft ob alle Länder den gleichen Player als owner haben
     * @return Gibt den Player zurück, der alle Länder auf diesem Kontinent besitzt, wenn der Kontinent geteilt wird wird null zurück gegeben.
     */
    public Player getCurrentOwner () throws RemoteException{
        boolean playerChange = false;
        Player ruler = null;

        for (Map.Entry<UUID,Country> entry : countrys.entrySet()){
            Country currentCountry = entry.getValue();

            if(ruler != null && ruler!= currentCountry.getOwner()){
                playerChange = true;
            }
            else if(ruler == null ){
                ruler = currentCountry.getOwner();
            }

        }
        if(!playerChange){
            return  ruler;
        }
        else {
            return null;
        }
    }

	/**
	 * Fügt dem Kontinent ein Land hinzu
	 * @param player Der Land, der dem Kontinent hinzugefügt werden soll
	 */
	public void addCountry(Country player) throws RemoteException{
		if(!countrys.containsKey(player.getId())){
            countrys.put(player.getId(), player);
        }
	}

    /**
     * Löscht aus dem Kontinent ein Land
     * @param countryToDelete Land das gelöscht werden soll
     * @return True wenn gelöscht
     */
    public boolean removeCountry (Country countryToDelete) throws RemoteException{
        if(countrys.containsKey(countryToDelete.getId())){
            countrys.remove(countryToDelete);
            return  true;
        }
        return  false;

    }
    /**
     * @return bonus Der Bonus, den der Besitz des kompletten Continents einbringt
     */
    public int getBonus() throws RemoteException{
    	return this.bonus;
    }
    
    @Override
    public String toString() {
        try {
			return this.getName();
		} catch (RemoteException e) {
			e.printStackTrace();
			return "";
		}
    }

    /**
     * ToString methode, die Remote aufgerufen werden kann
     * @return Klasse als String
     * @throws RemoteException
     */
    public String toStringRemote() throws RemoteException{
        return this.toString();
    }

}