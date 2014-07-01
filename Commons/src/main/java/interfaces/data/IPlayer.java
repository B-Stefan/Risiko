package interfaces.data;

import interfaces.IToStringRemote;
import interfaces.data.Orders.IOrder;
import interfaces.data.cards.*;

import java.awt.Color;
import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.*;

import exceptions.CountryNotInListException;

/**
 * Created by Stefan on 29.06.14.
 */
public interface IPlayer extends Remote, Serializable, IToStringRemote {

	/**
     * Getter für den Namen des Spielers
     * @return Name des Spielers
     */
    public String getName()throws RemoteException;
    /**
     * Sortiert das eigene Deck und gibt es aus
     * @return
     */
    public List<? extends ICard> getCards()throws RemoteException;

    /**
     * Getter für die Länderliste des Spielers
     * @return
     */
    public List<? extends ICountry> getCountries()throws RemoteException;
    
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
