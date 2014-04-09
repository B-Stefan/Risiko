package main.java.logic;

import main.resources.*;

import java.util.*;

/**
 * Created by Stefan on 01.04.2014.
 */
public class Map implements IMapGUI {
    private final ArrayList<Country> countries = new ArrayList<Country>();

    public Map() {
        this.countries.add(new Country("Alaska"));
        this.countries.add(new Country("Nordwest-Territorium"));
        this.countries.add(new Country("Alberta"));
        this.countries.add(new Country("Ontario"));
        this.countries.add(new Country("Quebec"));
        this.countries.add(new Country("Weststaaten"));
        this.countries.add(new Country("Oststaaten"));
        this.countries.add(new Country("Mittelamerika"));
        this.countries.add(new Country("Venezuela"));
        this.countries.add(new Country("Brasilien"));
        this.countries.add(new Country("Peru"));
        this.countries.add(new Country("Argentinien"));
        this.countries.add(new Country("Gr�nland"));
        this.countries.add(new Country("Island"));
        this.countries.add(new Country("Grobritannien"));
        this.countries.add(new Country("Skandinavien"));
        this.countries.add(new Country("Mitteleuropa"));
        this.countries.add(new Country("Westeuropa"));
        this.countries.add(new Country("S�deuropa"));
        this.countries.add(new Country("Ukraine"));
        this.countries.add(new Country("Afghanistan"));
        this.countries.add(new Country("Ural"));
        this.countries.add(new Country("Sibirien"));
        this.countries.add(new Country("Irrutsk"));
        this.countries.add(new Country("Jakuten"));
        this.countries.add(new Country("Kamtschatka"));
        this.countries.add(new Country("Mongolei"));
        this.countries.add(new Country("Japan"));
        this.countries.add(new Country("China"));
        this.countries.add(new Country("Indien"));
        this.countries.add(new Country("Siam"));
        this.countries.add(new Country("Mittelerer-Osten"));
        this.countries.add(new Country("�gypten"));
        this.countries.add(new Country("Ostafrika"));
        this.countries.add(new Country("Nordwestafrika"));
        this.countries.add(new Country("Kongo"));
        this.countries.add(new Country("S�dafrika"));
        this.countries.add(new Country("Madagaskar"));
        this.countries.add(new Country("Kongo"));
        this.countries.add(new Country("Indonesien"));
        this.countries.add(new Country("Neu-Guinea"));
        this.countries.add(new Country("Ostaustralien"));
        this.countries.add(new Country("Westaustralien"));


        //Hinzufügen der Verbindungen

    }

    public Map(final ArrayList<Country> countries) {
        this.countries.addAll(countries);
    }

    public ArrayList<Country> getCountries() {
        return this.countries;
    }


}
