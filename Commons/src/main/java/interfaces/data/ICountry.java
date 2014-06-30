package interfaces.data;
import interfaces.data.IArmy;
import interfaces.data.IPlayer;

import java.awt.Color;
import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.UUID;

import exceptions.CountriesNotConnectedException;
import exceptions.CountryNotInListException;


public interface ICountry extends Remote, Serializable {

    /**
     * Verbindet 2 Länder bidirektional
     *
     * @param connectTo Das Land zu dem eine Verbindung hergestellt werden soll
     */
    public void connectTo(ICountry connectTo) throws RemoteException;
    /**
     * Prüft, ob das Land mit dem aktuellen Land verbunden ist
     * @param countryToCheck Land das geprüft werden soll
     * @return True wenn das Land verbunden ist
     */
    public boolean isConnected(ICountry countryToCheck) throws RemoteException;
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
    public ArrayList<ICountry> getNeighbors () throws RemoteException;
    /**
     * Setzt den aktuellen Besitzer des Lands
     *
     * @param p Spieler, der als Owener gesetzt werden soll
     */
    public void setOwner(IPlayer p) throws RemoteException;
    /**
     * Ändert den Owner des Landes und entfehrnt gleichzeitig das Land aus der Liste des urspr�nglichen Owners
     * @param newOwner
     * @throws CountryNotInListException
     */
    public void changeOwner(IPlayer newOwner) throws CountryNotInListException, RemoteException;
    /**
     *Fügt die Armee a in die Liste des Spielers hin zu
     * @param a in die Liste der Armeen einzuf�gende Armee
     */
    public void addArmy(IArmy a) throws CountriesNotConnectedException, RemoteException;
    /**
     * Löscht die Armee auf dem Land
     * @param army Armee, die Gelöscht werden soll
     * @throws CountriesNotConnectedException
     */
    public void removeArmy(IArmy army) throws CountriesNotConnectedException, RemoteException;
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
     * Getter ArmyList des Spielers
     * @return armyList. Liste der Armeen des Spielers
     */
    public ArrayList<IArmy> getArmyList() throws RemoteException;
    
    
}
