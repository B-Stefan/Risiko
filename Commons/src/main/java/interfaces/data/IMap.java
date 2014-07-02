package interfaces.data;

import interfaces.IToStringRemote;

import java.awt.*;
import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.*;
import java.util.List;

/**
 * Created by Stefan on 29.06.14.
 */
public interface IMap extends Remote, Serializable, IToStringRemote {
    /**
     * Gibt die Liste aller Countries zurück
     * @return Liste aller Counties
     */
    public List<? extends ICountry> getCountries() throws RemoteException;

    /**
     * Berechnet den Benous, den ein Spieler an Einheiten bekommt f�r die komplette Einnahme des jeweilligen Kontinents
     * @param p der aktuelle Spieler
     * @return die Anzahl der Bonus Einheiten
     */
    public int getBonus(IPlayer p)throws RemoteException;


    /**
     * Vergleicht die Namen der L�nder mit �bergebenem String
     * @param n String (name des zu suchenden Landes)
     * @return das zu suchende Land
     */
    public ICountry getCountry(String n) throws RemoteException;

    /**
     * Vergleicht die Farbe mit der übergebenen Farbe
     * @param col Color (Farbe des zu suchenden Landes)
     * @return das zu suchende Land
     */
    public ICountry getCountry(Color col)throws RemoteException;

    /**
     *
     * @return Alle Kontinente dieser Karte
     */
    public List<? extends IContinent> getContinents()throws RemoteException;

    /**
     * Getter für die ID
     */
    public UUID getId()throws RemoteException;

}