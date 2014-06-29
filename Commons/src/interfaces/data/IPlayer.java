package interfaces.data;

import interfaces.data.Orders.IOrder;
import interfaces.data.cards.*;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;
import java.util.UUID;

import exceptions.CountryNotInListException;

/**
 * Created by Stefan on 29.06.14.
 */
public interface IPlayer {

	/**
     * Getter für den Namen des Spielers
     * @return Name des Spielers
     */
    public String getName();
    /**
     * Sortiert das eigene Deck und gibt es aus
     * @return
     */
    public Stack<ICard> getCards();
    /**
     * entfernt eine Karte aus dem eigenen Deck
     * @param c
     */
    public void removeCard(ICard c);
    /**
     * Eine neue Karte wird zum Deck hinzugefügt
     * @param c
     */
    public void drawNewCard(ICard c);

    /**
     * Fügt ein Land der Liste des Spielers hinzu
     * @param c
     */
    public void addCountry(final ICountry c);
    /**
     * Getter für die Länderliste des Spielers
     * @return
     */
    public ArrayList<ICountry> getCountries();
    
    /**
     * Setter für die Order des Spielers
     * @param order
     */
    public void setOrder(IOrder order);
    
    /**
     * Getter für die Order des Spielers
     * @return
     */
    public IOrder getOrder();
    /**
     * toString Methode überschrieben, gibt jetzt den Namen des Spielers aus
     * @return
     */
    @Override
    public String toString();
    /**
     * Gibt ein Land aus der Liste der Länder des Spielers aus
     * @param n
     * @return
     */
    public ICountry getCountry(String n);
    
    /**
     * Entfernt ein land aus der Liste der Länder des Spielers
     * @param c
     * @throws CountryNotInListException
     */
    public void removeCountry(ICountry c) throws CountryNotInListException;

    /**
     * Getter für die ID des Spielers
     * @return
     */
    public UUID getId ();
    /**
     * Getter für die Farbe des Spielers
     * @return
     */
    
    public Color getColor();
    /**
     * Setter für die Farbe des Spielers
     * @param col
     */
    public void setColor(Color col);


}
