package main.java.logic.data;


import main.java.logic.data.Continent;
import main.java.logic.data.Country;
import main.java.logic.data.Player;

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

        this.continents.add(northAmerica);
        this.continents.add(southAmerica);
        this.continents.add(asia);
        this.continents.add(afrika);
        this.continents.add(australia);


        //Länder zuweisen
        this.countries.add(new Country("Alaska", northAmerica));
        this.countries.add(new Country("Nordwest-Territorium", northAmerica));
        this.countries.add(new Country("Alberta", northAmerica));
        this.countries.add(new Country("Ontario", northAmerica));
        this.countries.add(new Country("Quebec", northAmerica));
        this.countries.add(new Country("Weststaaten", northAmerica));
        this.countries.add(new Country("Oststaaten", northAmerica));
        this.countries.add(new Country("Mittelamerika", northAmerica));
        this.countries.add(new Country("Hawaii", northAmerica));
        this.countries.add(new Country("Nunavut", northAmerica));
        
        this.countries.add(new Country("Venezuela", southAmerica));
        this.countries.add(new Country("Brasilien", southAmerica));
        this.countries.add(new Country("Peru", southAmerica));
        this.countries.add(new Country("Falkland-Inseln", southAmerica));
        this.countries.add(new Country("Argentinien", southAmerica));
        
        this.countries.add(new Country("Grönland", europe));
        this.countries.add(new Country("Island", europe));
        this.countries.add(new Country("Großbritannien", europe));
        this.countries.add(new Country("Skandinavien", europe));
        this.countries.add(new Country("Mitteleuropa", europe));
        this.countries.add(new Country("Westeuropa", europe));
        this.countries.add(new Country("Südeuropa", europe));
        this.countries.add(new Country("Ukraine", europe));
        this.countries.add(new Country("Svalbard", europe));
        
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
        this.countries.add(new Country("Mittlerer-Osten", asia));
        
        this.countries.add(new Country("Ägypten", afrika));
        this.countries.add(new Country("Ostafrika", afrika));
        this.countries.add(new Country("Nordwestafrika", afrika));
        this.countries.add(new Country("Kongo", afrika));
        this.countries.add(new Country("Südafrika", afrika));
        this.countries.add(new Country("Madagaskar", afrika));
        this.countries.add(new Country("Kongo", afrika));
        
        this.countries.add(new Country("Indonesien", australia));
        this.countries.add(new Country("Neu-Guinea", australia));
        this.countries.add(new Country("Ostaustralien", australia));
        this.countries.add(new Country("Westaustralien", australia));
        this.countries.add(new Country("Neuseeland", australia));
        this.countries.add(new Country("Philippinen", australia));



        //Hinzufügen der Verbindungen

        
        //Australien
        Country ostaustralien  = this.getCountry("Ostaustralien");
        ostaustralien.connectTo(this.getCountry("Westaustralien"));
        ostaustralien.connectTo(this.getCountry("Neu-Guinea"));

        Country indonesien  = this.getCountry("Indonesien");
        indonesien.connectTo(this.getCountry("Siam"));
        indonesien.connectTo(this.getCountry("Westaustralien"));
        indonesien.connectTo(this.getCountry("Philippinen"));
        
        Country Neuguinea = this.getCountry("Neu-Guinea");
        Neuguinea.connectTo(this.getCountry("Westaustralien"));
        
        Country neuseeland = this.getCountry("Neuseeland");
        neuseeland.connectTo(this.getCountry("Ostaustralien"));


        //Asien
        Country China  = this.getCountry("China");
        China.connectTo(this.getCountry("Siam"));
        China.connectTo(this.getCountry("Indien"));
        China.connectTo(this.getCountry("Mongolei"));
        China.connectTo(this.getCountry("Afghanistan"));
        China.connectTo(this.getCountry("Sibirien"));
        China.connectTo(this.getCountry("Ural"));

        Country mongolei  = this.getCountry("Mongolei");
        mongolei.connectTo(this.getCountry("Japan"));
        mongolei.connectTo(this.getCountry("Irrutsk"));
        mongolei.connectTo(this.getCountry("Sibirien"));

        Country jakutsk  = this.getCountry("Jakutsk");
        jakutsk.connectTo(this.getCountry("Kamtschatka"));
        jakutsk.connectTo(this.getCountry("Sibirien"));
        jakutsk.connectTo(this.getCountry("Irrutsk"));
        
        Country japan = this.getCountry("Japan");
        japan.connectTo(this.getCountry("Hawaii"));
        japan.connectTo(this.getCountry("Philippinen"));
        japan.connectTo(this.getCountry("Kamtschatka"));
        
        Country irrutsk = this.getCountry("Irrutsk");
        irrutsk.connectTo(this.getCountry("Kamtschatka"));
        irrutsk.connectTo(this.getCountry("Sibirien"));
        
        Country Indien = this.getCountry("Indien");
        Indien.connectTo(this.getCountry("Siam"));
        Indien.connectTo(this.getCountry("Mittlerer-Osten"));
        Indien.connectTo(this.getCountry("Afghanistan"));
        
        Country ural = this.getCountry("Ural");
        ural.connectTo(this.getCountry("Sibirien"));
        ural.connectTo(this.getCountry("Afghanistan"));
        ural.connectTo(this.getCountry("Ukraine"));
        
        Country mittlererOsten = this.getCountry("Mittlerer-Osten");
        mittlererOsten.connectTo(this.getCountry("Afghanistan"));
        mittlererOsten.connectTo(this.getCountry("Ukraine"));
        mittlererOsten.connectTo(this.getCountry("Ägypten"));
        mittlererOsten.connectTo(this.getCountry("Südeuropa"));
        
        
        //Europa
        Country ukraine = this.getCountry("Ukraine");
        ukraine.connectTo(this.getCountry("Afghanistan"));
        ukraine.connectTo(this.getCountry("Südeuropa"));
        ukraine.connectTo(this.getCountry("Skandinavien"));
        ukraine.connectTo(this.getCountry("Mitteleuropa"));
        
        Country Skandinavien = this.getCountry("Skandinavien");
        Skandinavien.connectTo(this.getCountry("Svalbard"));
        Skandinavien.connectTo(this.getCountry("Island"));
        Skandinavien.connectTo(this.getCountry("Großbritannien"));
        Skandinavien.connectTo(this.getCountry("Mitteleuropa"));
        
        Country gb = this.getCountry("Großbritannien");
        gb.connectTo(this.getCountry("Island"));
        gb.connectTo(this.getCountry("Mitteleuropa"));
        gb.connectTo(this.getCountry("Westeuropa"));
        
        Country westEu = this.getCountry("Westeuropa");
        westEu.connectTo(this.getCountry("Nordwestafrika"));
        westEu.connectTo(this.getCountry("Südeuropa"));
        westEu.connectTo(this.getCountry("Mitteleuropa"));
        
        Country southEu = this.getCountry("Südeuropa");
        southEu.connectTo(this.getCountry("Ägypten"));
        southEu.connectTo(this.getCountry("Nordwestafrika"));
        
        //Afrika
        Country noWeAfrika = this.getCountry("Nordwestafrika");
        noWeAfrika.connectTo(this.getCountry("Ägypten"));
        noWeAfrika.connectTo(this.getCountry("Ostafrika"));
        noWeAfrika.connectTo(this.getCountry("Kongo"));
        
        Country ostAfrika = this.getCountry("Ostafrika");
        ostAfrika.connectTo(this.getCountry("Ägypten"));
        ostAfrika.connectTo(this.getCountry("Kongo"));
        ostAfrika.connectTo(this.getCountry("Südafrika"));
        ostAfrika.connectTo(this.getCountry("Madagaskar"));
        
        Country southAfrika = this.getCountry("Südafrika");
        southAfrika.connectTo(this.getCountry("Madagaskar"));
        southAfrika.connectTo(this.getCountry("Kongo"));
        southAfrika.connectTo(this.getCountry("Falkland-Inseln"));
        

        
        //Süd-Amerika
        Country brasil = this.getCountry("Brasilien");
        brasil.connectTo(this.getCountry("Nordwestafrika"));
        brasil.connectTo(this.getCountry("Venezuela"));
        brasil.connectTo(this.getCountry("Peru"));
        brasil.connectTo(this.getCountry("Argentinien"));
        
        Country venezuela = this.getCountry("Venezuela");
        venezuela.connectTo(this.getCountry("Peru"));
        venezuela.connectTo(this.getCountry("Mittelamerika"));
        
        Country Argentinien = this.getCountry("Argentinien");
        Argentinien.connectTo(this.getCountry("Peru"));
        Argentinien.connectTo(this.getCountry("Falkland-Inseln"));
        Argentinien.connectTo(this.getCountry("Neuseeland"));
        
        
        //Nord-Amerika
        Country mittelamerika = this.getCountry("Mittelamerika");
        mittelamerika.connectTo(this.getCountry("Oststaaten"));
        mittelamerika.connectTo(this.getCountry("Weststaaten"));

        Country Weststaaten = this.getCountry("Weststaaten");
        Weststaaten.connectTo(this.getCountry("Oststaaten"));
        Weststaaten.connectTo(this.getCountry("Hawaii"));
        Weststaaten.connectTo(this.getCountry("Alberta"));
        Weststaaten.connectTo(this.getCountry("Ontario"));
        
        Country Ontario = this.getCountry("Ontario");
        Ontario.connectTo(this.getCountry("Quebec"));
        Weststaaten.connectTo(this.getCountry("Oststaaten"));
        Weststaaten.connectTo(this.getCountry("Nunavut"));
        Weststaaten.connectTo(this.getCountry("Nordwest-Territorium"));
        Weststaaten.connectTo(this.getCountry("Alberta"));

        Country alaska = this.getCountry("Alaska");
        alaska.connectTo(this.getCountry("Kamtschatka"));
        alaska.connectTo(this.getCountry("Nordwest-Territorium"));
        alaska.connectTo(this.getCountry("Alberta"));
        
        Country Nordwest = this.getCountry("Nordwest-Territorium");
        Nordwest.connectTo(this.getCountry("Alberta"));
        Nordwest.connectTo(this.getCountry("Nunavut"));
        
        Country Nunavut = this.getCountry("Nunavut");
        Nunavut.connectTo(this.getCountry("Quebec"));
        Nunavut.connectTo(this.getCountry("Grönland"));
        
        Country groenland = this.getCountry("Grönland");
        groenland.connectTo(this.getCountry("Quebec"));
        groenland.connectTo(this.getCountry("Svalbard"));
        groenland.connectTo(this.getCountry("Island"));

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

    /**
     *
     * @return Alle Kontinente dieser Karte
     */
    public ArrayList<Continent> getContinents() {
        return continents;
    }

}

