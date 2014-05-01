package main.java.logic;

import main.java.logic.exceptions.CountriesNotConnectedException;
import main.java.logic.exceptions.CountryNotInListException;

import java.util.*;

public class Country {

    /**
     * Name des Lands
     */
    private final String name;

    private final String id;

    private final HashMap<String, Country> neighbors = new HashMap<String, Country>();
    
    private final Continent position;

    /**
     * Bestimmt den Spieler, der aktuell das Land besetzt hï¿½llt
     */
    private Player owner;
    
    /**
     * Liste aller Armeen im Besitzt des jeweilligen Spielers
     */
    private List<Army> armyList = new ArrayList<Army>();


    public Country(final String n, Continent c) {
        this.name = n;
        this.id = UUID.randomUUID().toString();
        this.position = c;
    }

    /**
     * Verbindet 2 LÃ¤nder bidirektional
     *
     * @param connectTo Das Land zu dem eine Verbindung hergestellt werden soll
     */
    public void connectTo(Country connectTo) {
        if (!this.isConnected(connectTo)) {
            neighbors.put(connectTo.getId(), connectTo); //Hash map aktualisieren
            connectTo.connectTo(this); //Gegenverbindung setzten
        }
    }

    public boolean isConnected(Country countryToCheck) {
        return neighbors.containsKey(countryToCheck);
    }


    /**
     * setzt den aktuellen Besitzer des Lands
     *
     * @param p Spieler, der als Owener gesetzt werden soll
     */
    public void setOwner(Player p) {

    	this.owner = p;
    }
    /**
     * ändert den Owner des Landes und entfehrnt gleichzeitig das Land aus der Liste des ursprünglichen Owners
     * @param newOwner
     * @throws CountryNotInListException
     */
    public void changeOwner(Player newOwner) throws CountryNotInListException{
       	this.owner.removeCountry(this);
        this.owner = newOwner;
    }

    /**
     * @return Den Spieler, der dieses Land gerade besetzt hat
     */
    public Player getOwner() {
        return this.owner;
    }

    /**
     * @return Name des Lands
     */
    public String getName() {
        return this.name;
    }

    /**
     * @return Die unique Id fÃ¼r das Land
     */
    public String getId() {
        return this.id;
    }
    
    /**
     * Fï¿½gt die Armee a in die Liste der Armeen des Spielers hinzu
     * @param a in die Liste der Armeen einzufï¿½gende Armee
     */
    public void addArmy(Army a) throws CountriesNotConnectedException{
        if(!armyList.contains(a)){
            armyList.add(a);
            a.setPosition(this);
        }
    }
    public void removeArmy(Army a) throws CountriesNotConnectedException{
        if(armyList.contains(a)){
            armyList.remove(a);
            a.setPosition(null);
        }
    }
    /**
     * Getter ArmyList des Spielers
     * @return armyList. Liste der Armeen des Spielers
     */
    public List<Army> getArmyList(){
    	return this.armyList;
    }

    @Override
    public String toString() {
        String ownerString = " @ ";
        if (this.owner != null) {
            ownerString += this.owner.ToString();
        }
        return this.getName() + ownerString;
    }
}
