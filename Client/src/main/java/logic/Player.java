package main.java.logic;
import java.util.*;
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
}
