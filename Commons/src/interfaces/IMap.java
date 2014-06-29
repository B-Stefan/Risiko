package interfaces;

import interfaces.data.ICountry;
import interfaces.data.IPlayer;

import java.awt.*;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Stefan on 29.06.14.
 */
public interface IMap {
    /**
     * Gibt die Liste aller Countries zurück
     * @return Liste aller Counties
     */
    public ArrayList<ICountry> getCountries();

    /**
     * Berechnet den Benous, den ein Spieler an Einheiten bekommt f�r die komplette Einnahme des jeweilligen Kontinents
     * @param p der aktuelle Spieler
     * @return die Anzahl der Bonus Einheiten
     */
    public int getBonus(IPlayer p);


    /**
     * Vergleicht die Namen der L�nder mit �bergebenem String
     * @param n String (name des zu suchenden Landes)
     * @return das zu suchende Land
     */
    public ICountry getCountry(String n);

    /**
     * Vergleicht die Farbe mit der übergebenen Farbe
     * @param col Color (Farbe des zu suchenden Landes)
     * @return das zu suchende Land
     */
    public ICountry getCountry(Color col);

    /**
     *
     * @return Alle Kontinente dieser Karte
     */
    public ArrayList<IContinent> getContinents();

    /**
     * Getter für die ID
     */
    public UUID getId();

}
