package main.java.logic;

import main.resources.*;

import java.util.*;

/**
 * @author Jennifer Theloy, Thu Nguyen, Stefan Bieliauskas
 *
 * Dient zur Verwaltung der Karte für ein Spiel
 */
public class Map  {


    /**
     * Beinhaltet alle Länder, für die Karte
     */
    private final ArrayList<Country> countries = new ArrayList<Country>();

    /**
     * Beinhaltet alle Kontinente für die Karte
     */
    private final ArrayList<Continent> continents = new ArrayList<Continent>();

    /**
     * Erstellt eine neue Standard-Karte
     */
    public Map() {

        //Kontinente erzeugen
        Continent northAmerica  = new Continent("Nord Amerika", 5);
        Continent southAmerica  = new Continent("Süd Amerika", 2);
        Continent europe        = new Continent("Europa", 5);
        Continent asia          = new Continent("Asien", 7);
        Continent afrika        = new Continent("Afrika", 3);
        Continent australia     = new Continent("Australien", 2);


        //Länder zuweisen
        this.countries.add(new Country("Alaska", northAmerica));
        this.countries.add(new Country("Nordwest-Territorium", northAmerica));
        this.countries.add(new Country("Alberta", northAmerica));
        this.countries.add(new Country("Ontario", northAmerica));
        this.countries.add(new Country("Quebec", northAmerica));
        this.countries.add(new Country("Weststaaten", northAmerica));
        this.countries.add(new Country("Oststaaten", northAmerica));
        this.countries.add(new Country("Mittelamerika", northAmerica));
        this.countries.add(new Country("Venezuela", southAmerica));
        this.countries.add(new Country("Brasilien", southAmerica));
        this.countries.add(new Country("Peru", southAmerica));
        this.countries.add(new Country("Argentinien", southAmerica));
        this.countries.add(new Country("Gr�nland", europe));
        this.countries.add(new Country("Island", europe));
        this.countries.add(new Country("Grobritannien", europe));
        this.countries.add(new Country("Skandinavien", europe));
        this.countries.add(new Country("Mitteleuropa", europe));
        this.countries.add(new Country("Westeuropa", europe));
        this.countries.add(new Country("S�deuropa", europe));
        this.countries.add(new Country("Ukraine", europe));
        this.countries.add(new Country("Afghanistan", asia));
        this.countries.add(new Country("Ural", asia));
        this.countries.add(new Country("Sibirien", asia));
        this.countries.add(new Country("Irrutsk", asia));
        this.countries.add(new Country("Jakutsk", asia));
        this.countries.add(new Country("Kamtschatka", asia));
        this.countries.add(new Country("Mongolei", asia));
        this.countries.add(new Country("Japan", asia));
        this.countries.add(new Country("China", asia));
        this.countries.add(new Country("Indien", asia));
        this.countries.add(new Country("Siam", asia));
        this.countries.add(new Country("Mittelerer-Osten", asia));
        this.countries.add(new Country("�gypten", afrika));
        this.countries.add(new Country("Ostafrika", afrika));
        this.countries.add(new Country("Nordwestafrika", afrika));
        this.countries.add(new Country("Kongo", afrika));
        this.countries.add(new Country("S�dafrika", afrika));
        this.countries.add(new Country("Madagaskar", afrika));
        this.countries.add(new Country("Kongo", afrika));
        this.countries.add(new Country("Indonesien", australia));
        this.countries.add(new Country("Neu-Guinea", australia));
        this.countries.add(new Country("Ostaustralien", australia));
        this.countries.add(new Country("Westaustralien", australia));



        //Hinzufügen der Verbindungen

        //Australien
        Country ostaustralien  = this.getCountry("Ostaustralien");
        ostaustralien.connectTo(this.getCountry("Westaustralien"));
        ostaustralien.connectTo(this.getCountry("Neu-Guinea"));

        Country indonesien  = this.getCountry("Indonesien");
        indonesien.connectTo(this.getCountry("Siam"));
        indonesien.connectTo(this.getCountry("Westaustralien"));


        //Asien
        Country China  = this.getCountry("China");
        China.connectTo(this.getCountry("Siam"));
        China.connectTo(this.getCountry("Indien"));
        China.connectTo(this.getCountry("Mongolei"));
        China.connectTo(this.getCountry("Afghanistan"));

        Country mongolei  = this.getCountry("Mongolei");
        mongolei.connectTo(this.getCountry("Japan"));
        mongolei.connectTo(this.getCountry("Irrutsk"));
        mongolei.connectTo(this.getCountry("Sibirien"));

        Country jakutsk  = this.getCountry("Jakutsk");
        jakutsk.connectTo(this.getCountry("Kamtschatka"));
        jakutsk.connectTo(this.getCountry("Sibirien"));
        jakutsk.connectTo(this.getCountry("Irrutsk"));





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
    
    /**
     * Vergleicht die Namen der L�nder mit �bergebenem String
     * @param n String (name des zu suchenden Landes)
     * @return das zu suchende Land
     */
    public Country getCountry(String n){
    	for (Country c : countries){
    		if(c.getName().equals(n)){
    			return c;
    		}
    	}
    	return null;
    }
}

