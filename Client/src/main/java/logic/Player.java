package main.java.logic;
import java.util.*;
import main.java.logic.exceptions.*;
import main.java.logic.orders.*;
import java.util.UUID;

public class Player {

    private String name;
    private ArrayList<Country> countries = new ArrayList<Country>();
    private IOrder order;
    private UUID id;

    public Player(String name) {
        this.name = name;
        this.id = UUID.randomUUID();
    }

    public String getName() {
        return this.name;
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
    
}
