package main.java.logic;

import main.resources.*;

import java.util.*;

/**
 * Created by Stefan on 01.04.2014.
 */
public class Map implements IMapGUI {
    private final ArrayList<Country> countries = new ArrayList<Country>();

    public Map() {
        this.countries.add(new Country("Bali"));
        this.countries.add(new Country("Brasilien"));
        this.countries.add(new Country("Australien"));
        this.countries.add(new Country("Niederlande"));
        this.countries.add(new Country("Ukraine"));
        this.countries.add(new Country("Russland"));

    }

    public Map(final ArrayList<Country> countries) {
        this.countries.addAll(countries);
    }

    public ArrayList<Country> getCountries() {
        return this.countries;
    }


}
