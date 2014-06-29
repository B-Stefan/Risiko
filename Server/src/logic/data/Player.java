package logic.data;
import java.awt.Color;
import java.util.*;

import exceptions.*;
import logic.data.cards.Card;
import logic.data.orders.*;

import java.util.UUID;

public class Player {

    private String name;
    private ArrayList<Country> countries = new ArrayList<Country>();
    private IOrder order;
    private UUID id;
    private Color color;
    private Stack<Card> Deck = new Stack<Card>();

    public Player(String name) {
        this.name = name;
        this.id = UUID.randomUUID();

    }
    public Player(String name, Color color) {
        this(name);
        this.color = color;
    }

    public String getName() {
        return this.name;
    }
    /**
     * Sortiert das eigene Deck und gibt es aus
     * @return
     */
    public Stack<Card> getCards(){
    	Collections.sort(this.Deck);
    	return this.Deck;
    }
    public void removeCard(Card c){
    	this.Deck.remove(c);
    }
    
    public void drawNewCard(Card c){
    	if(this.Deck.size() <= 5){
    		this.Deck.add(c);
    	}
    }
    
    public String ToString() {
        return getName();
    }
    
    public void addCountry(final Country c){
        c.setOwner(this);
    	countries.add(c);
    }
    public ArrayList<Country> getCountries(){
    	return this.countries;    	
    }
    
    public void setOrder(IOrder order){
    	this.order = order;
    }

    public IOrder getOrder(){
    	return this.order;
    }

    @Override
    public String toString(){
        return this.getName();
    }
    public Country getCountry(String n){
    	for (Country c : countries){
    		if(c.getName().equals(n)){
    			return c;
    		}
    	}
    	return null;
    }
    public void removeCountry(Country c) throws CountryNotInListException{
    	if (countries.contains(c)){
    		this.countries.remove(c);
    	}else{
    		throw new CountryNotInListException();
    	}
    }

    public UUID getId () {
        return this.id;
    }
    
    public Color getColor(){
    	return this.color;
    }
    public void setColor(Color col){
        this.color = col;
    }


}
