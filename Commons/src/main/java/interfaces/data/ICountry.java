package interfaces.data;
import interfaces.IToStringRemote;
import interfaces.data.IArmy;
import interfaces.data.IPlayer;

import java.awt.Color;
import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import exceptions.CountriesNotConnectedException;
import exceptions.CountryNotInListException;


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


}
