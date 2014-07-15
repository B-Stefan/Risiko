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

import commons.exceptions.PersistenceEndpointIOException;
import server.logic.data.Army;
import server.logic.data.Country;
import server.logic.data.Map;
import server.logic.data.Player;
import commons.exceptions.CountriesNotConnectedException;
import server.persistence.PersistenceManager;

import java.awt.Color;
import java.rmi.RemoteException;
import java.util.HashMap;
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
public class PersistencePlayer extends PersitenceObject<Player> {

    private final UUID id;
    private final String name;
    private final Color color;
    private final HashMap<UUID,Integer> countries = new HashMap<UUID,Integer>();
    public PersistencePlayer(Player player, PersistenceManager manager) throws PersistenceEndpointIOException{
        super(player, manager);
        try{
	        this.name = player.getName();
	        this.color = player.getColor();
	        this.id =  player.getId();
	        for (Country c : player.getCountriesReal()){
	            countries.put(c.getId(),c.getNumberOfArmys());
	        }

        }catch (RemoteException e){
        	throw new PersistenceEndpointIOException(e);
        }
    }

    @Override
    public UUID getID() {
        return this.id;
    }

    @Override
    public Player convertToSourceObject(PersistenceManager manager) throws PersistenceEndpointIOException{

        Map defaultMap = manager.getMapHandler().get(Map.DEFAULT_MAP_UUID);
        if(defaultMap == null){
            throw new PersistenceEndpointIOException("Die Karte "+Map.DEFAULT_MAP_UUID+ " konnte nicht gefunden werden ");
        }
        try{
	        //Erstellen einer Liste aller Countries um einfach darin zu suchen, die Map hat bereits eine GetCountry(String) die auf dem Namen bassiert
	        HashMap<UUID,Country> allCountries = new HashMap<UUID,Country>();
	        for(Country country: defaultMap.getCountriesReal()){
	            allCountries.put(country.getId(),country);
	        }
	
	        //Erstellen der Player instanz
	        Player newInstance = new Player(this.name, this.color);
	
	        // Zuordnen der Länder und Einheiten
	        for(java.util.Map.Entry<UUID,Integer>  entry : this.countries.entrySet()){
	            Country mapCountry = allCountries.get(entry.getKey());
	
	            //wenn nicht gefunden
	            if (mapCountry == null){
	                throw new PersistenceEndpointIOException("Die UUID" + entry.getKey().toString() + " konnte nicht in der Karte" + defaultMap.getId().toString() + " gefunden werden");
	            }
	
	            mapCountry.setOwner(newInstance);
	            newInstance.addCountry(mapCountry);
	
	            //Armeen erzeugen
	            for(int i = 0; i!= entry.getValue(); i++){
	                try {
	                    Army newArmy = new Army(newInstance);
	                    mapCountry.addArmy(newArmy);
	                }catch (CountriesNotConnectedException e){
	                    //Diese Ecception kann nicht aufterten, da die Armee noch keine Position hat, wenn doch trotzdem weiter druchreichen
	                    throw  new PersistenceEndpointIOException(e);
	                }
	            }
	        }
	        return newInstance;
        }catch (RemoteException e){
        	throw new PersistenceEndpointIOException(e);
        }
    }
}
