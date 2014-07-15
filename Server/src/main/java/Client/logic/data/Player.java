/*
 * RISIKO-JAVA - Game, Copyright 2014  Jennifer Theloy, Stefan Bieliauskas  -  All Rights Reserved.
 * Hochschule Bremen - University of Applied Sciences
 *
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 * Contact:
 *     Jennifer Theloy: jTheloy@stud.hs-bremen.de
 *     Stefan Bieliauskas: sBieliauskas@stud.hs-bremen.de
 *
 * Web:
 *     https://github.com/B-Stefan/Risiko
 *
 */

package Client.logic.data;

import java.awt.Color;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

import commons.exceptions.*;
import commons.interfaces.IClient;
import commons.interfaces.data.ICountry;
import commons.interfaces.data.IPlayer;
import commons.interfaces.data.Orders.IOrder;
import commons.interfaces.data.cards.ICard;
import Client.logic.data.cards.Card;

import java.util.UUID;

public class Player extends UnicastRemoteObject implements IPlayer{
	/**
	 * Name des Spielers
	 */
    private final String name;
    /**
     * Liste der Länder in Besitz des Spielers
     */
    private final List<Country> countries = new ArrayList<Country>();
    /**
     * Liste der Karte in Besitz des Spielers
     */
    private final List<Card> deck = new ArrayList<Card>();
    /**
     * UUID
     */
    private final UUID id;
    /**
     * Zum Spieler gehörender client
     */
    private IClient client;
    /**
     * Dem Spieler zugewiesene Order
     */
    private IOrder order;
    /**
     * Dem Spieler zugewiesene Farbe
     */
    private Color color;
    /**
     * Constructor
     * @param name Name des Spielers
     * @throws RemoteException
     */
    public Player(final String name) throws RemoteException{
        this.name = name;
        this.id = UUID.randomUUID();

    }
    /**
     * Constructor
     * @param name Name des Spielers
     * @param color Farbe des Spielers
     * @throws RemoteException
     */
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
     * Gibt das Deck des Spielers aus
     * @return Alle Karten des Spielers
     */
    public List<? extends ICard> getCards()throws RemoteException{
    	return this.deck;
    }

    /**
     * Gibt das Deck des Spielers aus
     * @return Alle Karten des Spielers
     */
    public List<Card> getCardsReal(){
        return this.deck;
    }
    /**
     * entfernt eine Karte aus dem eigenen Deck
     * @param c Die zu entfehrnende Karte
     */
    public void removeCard(Card c)throws RemoteException{
    	this.deck.remove(c);
    }

    /**
     * Eine neue Karte wird zum Deck hinzugefügt
     * @param c Die gezogene Karte
     */
    public void drawNewCard(Card c)throws RemoteException{
    	if(this.deck.size() <= 5){
    		this.deck.add(c);
    	}
    }

    /**
     * Fügt ein Land der Liste des Spielers hinzu
     * @param c Das zu hinzuzufügende Land
     * @throws RemoteException
     */
    public void addCountry(final Country c)throws RemoteException{
        c.setOwner(this);
    	countries.add(c);
    }
    /**
     * Funktion zum hinzufügen eines Landes zu der Länderliste nach einem Takeover
     * @param c Das zu hinzufügende Land
     * @throws RemoteException
     */
    public void addCountryAfterTakeover(final Country c)throws RemoteException{
    	countries.add(c);
    }

    /**
     * Getter für die Länderliste des Spielers
     * @return Alle Länder eines Spielers
     */
    public List<? extends ICountry> getCountries()throws RemoteException{
        return this.countries;
    }
    /**
     * Getter für die Länderliste des Spielers
     * @return Alle Länder eines Spielers
     */
    public List<Country> getCountriesReal(){
        return this.countries;
    }
    
    /**
     * Setter für die Order des Spielers
     * @param order Die Order des Spielers
     */
    public void setOrder(IOrder order)throws RemoteException{
    	this.order = order;
    }
    
    /**
     * Getter für die Order des Spielers
     * @return Die Order des Spielers
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
     * @param n Name des Landes
     * @return Das Land als ICountry
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
     * @param country Das zu suchende Land als ICountry
     * @return Das Land als Country
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
     * @param c Zu entfehrnendes Land
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
     * @return UUID des Spielers
     */
    public UUID getId ()throws RemoteException {
        return this.id;
    }
    /**
     * Getter für die Farbe des Spielers
     * @return Farbe des Spielers
     */
    
    public Color getColor()throws RemoteException{
    	return this.color;
    }
    /**
     * Setter für die Farbe des Spielers
     * @param col Farbe des Spielers
     */
    public void setColor(Color col)throws RemoteException{
        this.color = col;
    }

    /**
     * Gibt das client objekt des SPielers zurück, wenn eins vohanden
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

    /**
     * Check player by id
     * @return
     */
    @Override
    public boolean equals(Object otherObject){
        if(otherObject instanceof IPlayer){
            IPlayer otherPlayer = (IPlayer) otherObject;
            boolean check;
            try {
                check = otherPlayer.getId().equals(this.getId());
            }catch (RemoteException e){
                e.printStackTrace();
                return false;
            }
            return check;
        }
        return false;
    }


}
