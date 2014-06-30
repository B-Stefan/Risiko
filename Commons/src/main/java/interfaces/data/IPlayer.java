package interfaces.data;

import interfaces.data.Orders.IOrder;
import interfaces.data.cards.*;

import java.awt.Color;
import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;
import java.util.UUID;

import exceptions.CountryNotInListException;

/**
 * Created by Stefan on 29.06.14.
 */
public interface IPlayer extends Remote, Serializable {

	/**
     * Getter für den Namen des Spielers
     * @return Name des Spielers
     */
    public String getName()throws RemoteException;
    /**
     * Sortiert das eigene Deck und gibt es aus
     * @return
     */
    public Stack<ICard> getCards()throws RemoteException;
    /**
     * entfernt eine Karte aus dem eigenen Deck
     * @param c
     */
    public void removeCard(ICard c)throws RemoteException;
    /**
     * Eine neue Karte wird zum Deck hinzugefügt
     * @param c
     */
    public void drawNewCard(ICard c)throws RemoteException;

    /**
     * Fügt ein Land der Liste des Spielers hinzu
     * @param c
     */
    public void addCountry(final ICountry c)throws RemoteException;
    /**
     * Getter für die Länderliste des Spielers
     * @return
     */
    public ArrayList<ICountry> getCountries()throws RemoteException;
    
    /**
     * Setter für die Order des Spielers
     * @param order
     */
    public void setOrder(IOrder order)throws RemoteException;
    
    /**
     * Getter für die Order des Spielers
     * @return
     */
    public IOrder getOrder()throws RemoteException;
    /**
     * Gibt ein Land aus der Liste der Länder des Spielers aus
     * @param n
     * @return
     */
    public ICountry getCountry(String n)throws RemoteException;
    
    /**
     * Entfernt ein land aus der Liste der Länder des Spielers
     * @param c
     * @throws CountryNotInListException
     */
    public void removeCountry(ICountry c) throws CountryNotInListException, RemoteException;

    /**
     * Getter für die ID des Spielers
     * @return
     */
    public UUID getId ()throws RemoteException;
    /**
     * Getter für die Farbe des Spielers
     * @return
     */
    
    public Color getColor()throws RemoteException;
    /**
     * Setter für die Farbe des Spielers
     * @param col
     */
    public void setColor(Color col)throws RemoteException;


}
