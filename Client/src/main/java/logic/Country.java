package main.java.logic;

import java.util.*;
import java.util.HashMap;

public class Country {

    /**
     * Name des Lands
     */
    private final String name;

    private final int id;

    private final HashMap<Integer, Country> neighbors = new HashMap<Integer, Country>();

    /**
     * Bestimmt den Spieler, der aktuell das Land besetzt hï¿½llt
     */
    private Player owner;
    
    /**
     * Liste aller Armeen im Besitzt des jeweilligen Spielers
     */
    private List<Army> armyList = new ArrayList<Army>();


    public Country(final String n) {
        this.name = n;
        this.id = Integer.parseInt(this.name);
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
    public int getId() {
        return this.id;
    }
    
    /**
     * Fügt die Armee a in die Liste der Armeen des Spielers hinzu
     * @param a in die Liste der Armeen einzufügende Armee
     */
    public void addArmy(Army a){
    	armyList.add(a);
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
