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

import commons.exceptions.CountriesNotConnectedException;
import commons.exceptions.CountryNotInListException;
import commons.interfaces.data.ICountry;

import java.awt.Color;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.Map.Entry;

/**
 * @author Jennifer Theloy,  Stefan Bieliauskas
 *
 * Diese Klasse bildet ein einzelnes Land des Spiels ab
 */
public class Country extends UnicastRemoteObject implements ICountry {

    /**
     * Name des Lands
     */
    private final String name;

    /**
     * Uniqu ID für das Spiel
     */
    private final UUID id;


    /**
     * Farbe für die Hintergrundkarte
     */
    private final Color color;

    /**
     * Bildet die relation der Nachbarländer ab
     * Der Key ist dabei die ID des Landes
     */
    private final HashMap<UUID, Country> neighbors = new HashMap<UUID, Country>();

    /**
     * Kontinent für das Land
     */
    private final Continent continent;


    /**
     * Kürzel des Landes
     */
    private final String shortName;


    /**
     * Bestimmt den Spieler, der aktuell das Land besetzt hällt
     */
    private Player owner;
    
    /**
     * Liste aller Armeen im Besitzt des jeweilligen Spielers
     */
    private final ArrayList<Army> armyList = new ArrayList<Army>();


    /**
     * Erstellt ein Land
     * @param name Name des Landes
     * @param continent Kontinent des Landes
     *
     */
    public Country(final String name, final Continent continent, final Color color) throws RemoteException {
        this.name = name;
        this.shortName = this.name.substring(0, 3);
        this.id = UUID.nameUUIDFromBytes(name.getBytes()); // statische UUID bassierend auf dem Namen, da die Karte im Moment nicht dynamisch ist
        this.continent = continent;
        this.continent.addCountry(this);
        this.color = color;
    }

    /**
     * Verbindet 2 Länder bidirektional
     *
     * @param connectTo Das Land zu dem eine Verbindung hergestellt werden soll
     */
    public void connectTo(Country connectTo) throws RemoteException {
        if (!this.isConnected(connectTo)) {
            neighbors.put(connectTo.getId(), connectTo); //Hash map aktualisieren
            connectTo.connectTo(this); //Gegenverbindung setzten
        }
    }

    /**
     * Prüft, ob das Land mit dem aktuellen Land verbunden ist
     * @param connectTo Land das geprüft werden soll
     * @return True wenn das Land verbunden ist
     */
    public boolean isConnected(Country connectTo) throws RemoteException {
        return neighbors.containsValue(connectTo);
    }


    /**
     * Gibt den das Land mit der entsprechenden UUID zurück
     * @param id UUID des Landes was benötigt wird
     * @return Land das gesucht wurde, oder null
     */
    public Country getNeighbor (UUID id) throws RemoteException{
        return neighbors.get(id);
    }

    /**
     * Gibt das Land anhand seines Namens zurück. Dabei wird das erste Land, dass diesem Namen entsprecht zurückgegeben.
     * @param searchName - Name nach dem gesucht werden soll
     * @return Land das gesucht wurde oder null wenn nicht gefunden
     */
    public Country getNeighbor (String searchName) throws RemoteException{
        for (Entry<UUID, Country> entry : this.neighbors.entrySet()){
            if(entry.getValue().getName().equals(searchName)){
                return entry.getValue();
            }
        }
        return null;
    }

    /**
     * Gibt alle Nachbarn des Landes zurück
     * @return Nachbarn des Landes
     */
    public List<? extends ICountry> getNeighbors () throws RemoteException{
        ArrayList<Country> list = new ArrayList<Country>();
        list.addAll(this.neighbors.values());
        return list;
    }

    /**
     * Setzt den aktuellen Besitzer des Lands
     *
     * @param p Spieler, der als Owener gesetzt werden soll
     */
    public void setOwner(Player p) throws RemoteException{
    	this.owner = p;
    }
    public void setOwnerTakeOver(Player p) throws RemoteException, CountryNotInListException{
    	p.addCountryAfterTakeover(this);
    	this.owner.removeCountry(this);
    	this.owner = p;
    }


    /**
     * Ändert den Owner des Landes und entfehrnt gleichzeitig das Land aus der Liste des urspr�nglichen Owners
     * @param newOwner Der neue Besitzer des Landes
     * @throws CountryNotInListException
     */
    public void changeOwner(Player newOwner) throws CountryNotInListException, RemoteException{
       	this.owner.removeCountry(this);
        this.owner = newOwner;
    }


    /**
     * Fügt Armee a in die Liste der Armeen des Landes hinzu
     * @param a in die Liste der Armeen einzuf�gende Armee
     */
    public void addArmy(Army a) throws CountriesNotConnectedException, RemoteException{
        if(!armyList.contains(a)){
            armyList.add(a);
            a.setPosition(this);
        }
    }

    /**
     * Löscht die Armee auf dem Land
     * @param army Armee, die Gelöscht werden soll
     * @throws CountriesNotConnectedException
     */
    public void removeArmy(Army army) throws CountriesNotConnectedException, RemoteException{
        if(armyList.contains(army)){
            armyList.remove(army);
            army.setPosition(null);
        }
    }
    /**
     *
     * @return Anzahl der Armeen auf dem Land
     */
    public int getNumberOfArmys () throws RemoteException{
        return this.armyList.size();
    }

    /**
     * @return Den Spieler, der dieses Land gerade besetzt hat
     */
    public Player getOwner() throws RemoteException{
        return this.owner;
    }

    /**
     * @return Name des Lands
     */
    public String getName() throws RemoteException{
        return this.name;
    }

    /**
     * @return Die unique Id für das Land
     */
    public UUID getId() throws RemoteException{
        return this.id;
    }

    /**
     * @return Die Farbe des Countrys
     */
    public Color getColor() throws RemoteException{
        return this.color;
    }

    /**
     * Getter ArmyList des Spielers
     * @return armyList. Liste der Armeen des Spielers
     */
    public List<Army> getArmyList() throws RemoteException{
    	return this.armyList;
    }

    /**
     * Gibt die Anzahl der Armeen auf dem Land zurück
     * @return Die Anzahl der Armeen auf diesem Land
     */
    public int getArmySize(){
        return this.armyList.size();
    }

    /**
     * Gibt das Land als String zurück, Name Owner und Armeestärke
     * @return Klasse als String
     */
    @Override
    public String toString() {
        String postFix = " Owner: ";
        if (this.owner != null) {
            postFix += this.owner.toString();
        }
        try {
			postFix += " Armeen: " + this.getNumberOfArmys();
			return this.getName() + postFix;
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


    /**
     * Überschreibt die Vergleichsmethode, Vorbereitung auf Persistence
     * @param otherCountry Andere Country das verglichen werden soll
     * @return True wenn übereinstimmung
     */
    @Override
    public boolean equals(Object otherCountry) {
        if(otherCountry instanceof  ICountry){
            try {
                ICountry country = (ICountry) otherCountry;
				return country.getId().equals(this.getId());
			} catch (RemoteException e) {
				e.printStackTrace();
				return false;
			}
        }
        throw new RuntimeException("Die Equals methode wird nicht korrekt ausgeführt" + otherCountry);
    }
    
    /**
     * Getter für das Kürzel
     * @return Kürzel Die Abkürzung des Namens des Landes
     */
    public String getShortName()throws RemoteException{
    	return this.shortName;
    }
}
