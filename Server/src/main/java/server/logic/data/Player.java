package server.logic.data;

import java.awt.Color;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

import exceptions.*;
import interfaces.IClient;
import interfaces.data.ICountry;
import interfaces.data.IPlayer;
import interfaces.data.Orders.IOrder;
import interfaces.data.cards.ICard;
import server.logic.data.cards.Card;
import java.util.UUID;

public class Player extends UnicastRemoteObject implements IPlayer{

    private final String name;
    private final List<Country> countries = new ArrayList<Country>();
    private final List<Card> deck = new ArrayList<Card>();
    private final UUID id;
    private IClient client;
    private IOrder order;
    private Color color;

    public Player(final String name) throws RemoteException{
        this.name = name;
        this.id = UUID.randomUUID();

    }
    
    public Player(final String name, final Color color)throws RemoteException {
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
    public List<? extends ICard> getCards()throws RemoteException{
    	return this.deck;
    }

    /**
     * Sortiert das eigene Deck und gibt es aus
     * @return
     */
    public List<Card> getCardsReal(){
        return this.deck;
    }
    /**
     * entfernt eine Karte aus dem eigenen Deck
     * @param c
     */
    public void removeCard(Card c)throws RemoteException{
    	this.deck.remove(c);
    }

    /**
     * Eine neue Karte wird zum Deck hinzugefügt
     * @param c
     */
    public void drawNewCard(Card c)throws RemoteException{
    	if(this.deck.size() <= 5){
    		this.deck.add((Card)c);
    	}
    }

    /**
     * Fügt ein Land der Liste des Spielers hinzu
     * @param c
     */
    public void addCountry(final Country c)throws RemoteException{
        c.setOwner(this);
    	countries.add(c);
    }
    /**
     * Getter für die Länderliste des Spielers
     * @return
     */
    public List<? extends ICountry> getCountries()throws RemoteException{
        return this.countries;
    }
    /**
     * Getter für die Länderliste des Spielers
     * @return
     */
    public List<Country> getCountriesReal(){
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
     * ToString methode, die Remote aufgerufen werden kann
     * @return
     * @throws RemoteException
     */
    public String toStringRemote() throws RemoteException{
        return this.toString();
    }

    /**
     * Gibt ein Land aus der Liste der Länder des Spielers aus
     * @param n
     * @return
     */
    public ICountry getCountry(String n) throws RemoteException{
    	for (Country c : countries){
    		if(c.getName().equals(n)){
                ICountry iCountry = c;
    			return iCountry;
    		}
    	}
    	return null;
    }
    /**
     * Gibt ein Land aus der Liste der Länder des Spielers aus
     * @param country
     * @return
     */
    public Country getCountry(ICountry country){
        for (Country c : countries){
            if(c.equals(country)){
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
    public void removeCountry(Country c) throws CountryNotInListException, RemoteException{
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

    /**
     * Gibt das Client objekt des SPielers zurück, wenn eins vohanden
     */
    public IClient getClient(){
        return this.client;
    }

    /**
     * Setz den aktuellen client für den Spieler
     */
    public void setClient(IClient client){
        if(this.client != null){
            try {
                this.client.receiveMessage("Dein Client wurde soeben von einem anderen Überschrieben");
            }catch (RemoteException e){
                this.client = null;
            }
        }
        this.client = client;
    }



}