package main.java.logic;

import main.resources.*;

import java.util.*;

/**
 * Created by Stefan on 01.04.2014.
 */
public class Map  {
    private final ArrayList<Country> countries = new ArrayList<Country>();
    private final ArrayList<Continent> continents = new ArrayList<Continent>();

    public Map() {
    	this.continents.add(new Continent("Nord Amerika", 5));
    	this.continents.add(new Continent("S�d Amerika", 2));
    	this.continents.add(new Continent("Europa", 5));
    	this.continents.add(new Continent("Afrika", 3));
    	this.continents.add(new Continent("Asien", 7));
    	this.continents.add(new Continent("Australien", 2));
    	
        this.countries.add(new Country("Alaska", continents.get(0)));
        this.countries.add(new Country("Nordwest-Territorium", continents.get(0)));
        this.countries.add(new Country("Alberta", continents.get(0)));
        this.countries.add(new Country("Ontario", continents.get(0)));
        this.countries.add(new Country("Quebec", continents.get(0)));
        this.countries.add(new Country("Weststaaten", continents.get(0)));
        this.countries.add(new Country("Oststaaten", continents.get(0)));
        this.countries.add(new Country("Mittelamerika", continents.get(0)));
        this.countries.add(new Country("Venezuela", continents.get(1)));
        this.countries.add(new Country("Brasilien", continents.get(1)));
        this.countries.add(new Country("Peru", continents.get(1)));
        this.countries.add(new Country("Argentinien", continents.get(1)));
        this.countries.add(new Country("Gr�nland", continents.get(2)));
        this.countries.add(new Country("Island", continents.get(2)));
        this.countries.add(new Country("Grobritannien", continents.get(2)));
        this.countries.add(new Country("Skandinavien", continents.get(2)));
        this.countries.add(new Country("Mitteleuropa", continents.get(2)));
        this.countries.add(new Country("Westeuropa", continents.get(2)));
        this.countries.add(new Country("S�deuropa", continents.get(2)));
        this.countries.add(new Country("Ukraine", continents.get(2)));
        this.countries.add(new Country("Afghanistan", continents.get(4)));
        this.countries.add(new Country("Ural", continents.get(4)));
        this.countries.add(new Country("Sibirien", continents.get(4)));
        this.countries.add(new Country("Irrutsk", continents.get(4)));
        this.countries.add(new Country("Jakuten", continents.get(4)));
        this.countries.add(new Country("Kamtschatka", continents.get(4)));
        this.countries.add(new Country("Mongolei", continents.get(4)));
        this.countries.add(new Country("Japan", continents.get(4)));
        this.countries.add(new Country("China", continents.get(4)));
        this.countries.add(new Country("Indien", continents.get(4)));
        this.countries.add(new Country("Siam", continents.get(4)));
        this.countries.add(new Country("Mittelerer-Osten", continents.get(4)));
        this.countries.add(new Country("�gypten", continents.get(3)));
        this.countries.add(new Country("Ostafrika", continents.get(3)));
        this.countries.add(new Country("Nordwestafrika", continents.get(3)));
        this.countries.add(new Country("Kongo", continents.get(3)));
        this.countries.add(new Country("S�dafrika", continents.get(3)));
        this.countries.add(new Country("Madagaskar", continents.get(3)));
        this.countries.add(new Country("Kongo", continents.get(3)));
        this.countries.add(new Country("Indonesien", continents.get(5)));
        this.countries.add(new Country("Neu-Guinea", continents.get(5)));
        this.countries.add(new Country("Ostaustralien", continents.get(5)));
        this.countries.add(new Country("Westaustralien", continents.get(5)));


        //Hinzufügen der Verbindungen

    }

    public Map(final ArrayList<Country> countries) {
        this.countries.addAll(countries);
    }

    public ArrayList<Country> getCountries() {
        return this.countries;
    }
    
    /**
     * Berechnet den Benous, den ein Spieler an Einheiten bekommt f�r die komplette Einnahme des jeweilligen Kontinents
     * @param p der aktuelle Spieler
     * @return die Anzahl der Bonus Einheiten
     */
    public int getBonus(Player p){
    	int bonus = 0;
    	for (Continent c : this.continents){
    		if(c.getCurrentOwner()==p){bonus += c.getBonus();}
    	}    	
    	return bonus;
    }
    
}

