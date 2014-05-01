package main.java.logic;
import java.util.*;
import main.java.logic.exceptions.*;
/**
 * Created by Stefan on 01.04.2014.
 */

public class Player {

    private String name;
    private ArrayList<Country> countries = new ArrayList<Country>();

    public Player(String name) {
        this.name = name;
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
    
}
