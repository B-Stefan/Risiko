package logic.data;

import exceptions.CountriesNotConnectedException;
import exceptions.CountryNotInListException;
import interfaces.data.*;

import java.awt.Color;
import java.util.*;
import java.util.Map.Entry;

/**
 * @author Jennifer Theloy, Thu Nguyen, Stefan Bieliauskas
 *
 * Diese Klasse bildet ein einzelnes Land des Spiels ab
 */
public class Country implements ICountry {

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
    private final HashMap<UUID, ICountry> neighbors = new HashMap<UUID, ICountry>();
    
    private final IContinent continent;

    /**
     * Bestimmt den Spieler, der aktuell das Land besetzt hällt
     */
    private IPlayer owner;
    
    /**
     * Liste aller Armeen im Besitzt des jeweilligen Spielers
     */
    private ArrayList<IArmy> armyList = new ArrayList<IArmy>();


    /**
     * Erstellt ein Land
     * @param name Name des Landes
     * @param continent Kontinent des Landes
     *
     */
    public Country(final String name, IContinent continent, Color color) {
        this.name = name;
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
    public void connectTo(ICountry connectTo) {
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
    public boolean isConnected(ICountry connectTo) {
        return neighbors.containsValue(connectTo);
    }


    /**
     * Gibt den das Land mit der entsprechenden UUID zurück
     * @param id UUID des Landes was benötigt wird
     * @return Land das gesucht wurde, oder null
     */
    public ICountry getNeighbor (UUID id){
        return neighbors.get(id);
    }

    /**
     * Gibt das Land anhand seines Namens zurück. Dabei wird das erste Land, dass diesem Namen entsprecht zurückgegeben.
     * @param searchName - Name nach dem gesucht werden soll
     * @return Land das gesucht wurde oder null wenn nicht gefunden
     */
    public ICountry getNeighbor (String searchName){
        for (Entry<UUID, ICountry> entry : this.neighbors.entrySet()){
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
    public ArrayList<ICountry> getNeighbors (){
        ArrayList<Country> list = new ArrayList<Country>();
        list.addAll(this.neighbors.values());
        return list;
    }

    /**
     * Setzt den aktuellen Besitzer des Lands
     *
     * @param p Spieler, der als Owener gesetzt werden soll
     */
    public void setOwner(IPlayer p) {
    	this.owner = p;
    }


    /**
     * Ändert den Owner des Landes und entfehrnt gleichzeitig das Land aus der Liste des urspr�nglichen Owners
     * @param newOwner
     * @throws CountryNotInListException
     */
    public void changeOwner(IPlayer newOwner) throws CountryNotInListException{
       	this.owner.removeCountry(this);
        this.owner = newOwner;
    }


    /**
     * F�gt die Armee a in die Liste der Armeen des Spielers hinzu
     * @param a in die Liste der Armeen einzuf�gende Armee
     */
    public void addArmy(IArmy a) throws CountriesNotConnectedException{
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
    public void removeArmy(IArmy army) throws CountriesNotConnectedException{
        if(armyList.contains(army)){
            armyList.remove(army);
            army.setPosition(null);
        }
    }

    /**
     *
     * @return Anzahl der Armeen auf dem Land
     */
    public int getNumberOfArmys (){
        return this.armyList.size();
    }
    /**
     * @return Den Spieler, der dieses Land gerade besetzt hat
     */
    public IPlayer getOwner() {
        return this.owner;
    }

    /**
     * @return Name des Lands
     */
    public String getName() {
        return this.name;
    }

    /**
     * @return Die unique Id für das Land
     */
    public UUID getId() {
        return this.id;
    }

    /**
     * @return Die Farbe des Countrys
     */
    public Color getColor() {
        return this.color;
    }

    /**
     * Getter ArmyList des Spielers
     * @return armyList. Liste der Armeen des Spielers
     */
    public ArrayList<IArmy> getArmyList(){
    	return this.armyList;
    }

    /**
     * Gibt das Land als String zurück, Name Owner und Armeestärke
     * @return
     */
    @Override
    public String toString() {
        String postFix = " Owner: ";
        if (this.owner != null) {
            postFix += this.owner.toString();
        }
        postFix += " Armeen: " + this.getNumberOfArmys();
        return this.getName() + postFix;
    }


    /**
     * Überschreibt die Vergleichsmethode, Vorbereitung auf Persistence
     * @param otherCountry
     * @return
     */
    @Override
    public boolean equals(Object otherCountry){
        if(otherCountry instanceof  Country){
            Country country = (Country) otherCountry;
            return country.getId() == this.getId();
        }
        return false;
    }
}
