package logic.data;
import interfaces.data.*;
import interfaces.data.Orders.IOrder;
import interfaces.data.cards.*;

import java.awt.Color;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

import exceptions.*;
import logic.data.cards.Card;
import logic.data.orders.*;

import java.util.UUID;

public class Player extends UnicastRemoteObject implements IPlayer{

    private String name;
    private ArrayList<ICountry> countries = new ArrayList<ICountry>();
    private IOrder order;
    private UUID id;
    private Color color;
    private Stack<ICard> Deck = new Stack<ICard>();

    public Player(String name) throws RemoteException{
        this.name = name;
        this.id = UUID.randomUUID();

    }
    
    public Player(String name, Color color)throws RemoteException {
        this(name);
        this.color = color;
    }

    /**
     * Getter für den Namen des Spielers
     * @return Name des Spielers
     */
    public String getName()throws RemoteException {
        return this.name;
    }
    /**
     * Sortiert das eigene Deck und gibt es aus
     * @return
     */
    public Stack<ICard> getCards()throws RemoteException{
    	Collections.sort(this.Deck);
    	return this.Deck;
    }
    /**
     * entfernt eine Karte aus dem eigenen Deck
     * @param c
     */
    public void removeCard(ICard c)throws RemoteException{
    	this.Deck.remove(c);
    }
    /**
     * Eine neue Karte wird zum Deck hinzugefügt
     * @param c
     */
    public void drawNewCard(ICard c)throws RemoteException{
    	if(this.Deck.size() <= 5){
    		this.Deck.add(c);
    	}
    }

    /**
     * Fügt ein Land der Liste des Spielers hinzu
     * @param c
     */
    public void addCountry(final ICountry c)throws RemoteException{
        c.setOwner(this);
    	countries.add(c);
    }
    /**
     * Getter für die Länderliste des Spielers
     * @return
     */
    public ArrayList<ICountry> getCountries()throws RemoteException{
    	return this.countries;    	
    }
    
    /**
     * Setter für die Order des Spielers
     * @param order
     */
    public void setOrder(IOrder order)throws RemoteException{
    	this.order = order;
    }
    
    /**
     * Getter für die Order des Spielers
     * @return
     */
    public IOrder getOrder()throws RemoteException{
    	return this.order;
    }
    /**
     * toString Methode überschrieben, gibt jetzt den Namen des Spielers aus
     * @return
     */
    @Override
    public String toString(){
        try {
			return this.getName();
		} catch (RemoteException e) {
			e.printStackTrace();
			return "";
		}
    }
    /**
     * Gibt ein Land aus der Liste der Länder des Spielers aus
     * @param n
     * @return
     */
    public ICountry getCountry(String n) throws RemoteException{
    	for (ICountry c : countries){
    		if(c.getName().equals(n)){
    			return c;
    		}
    	}
    	return null;
    }
    
    /**
     * Entfernt ein land aus der Liste der Länder des Spielers
     * @param c
     * @throws CountryNotInListException
     */
    public void removeCountry(ICountry c) throws CountryNotInListException, RemoteException{
    	if (countries.contains(c)){
    		this.countries.remove(c);
    	}else{
    		throw new CountryNotInListException();
    	}
    }

    /**
     * Getter für die ID des Spielers
     * @return
     */
    public UUID getId ()throws RemoteException {
        return this.id;
    }
    /**
     * Getter für die Farbe des Spielers
     * @return
     */
    
    public Color getColor()throws RemoteException{
    	return this.color;
    }
    /**
     * Setter für die Farbe des Spielers
     * @param col
     */
    public void setColor(Color col)throws RemoteException{
        this.color = col;
    }


}
