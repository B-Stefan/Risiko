package main.java.logic;
        import main.resources.*
/**
 * Created by Stefan on 01.04.2014.
 */
public class Map implements IMapGUI {
    private ArrayList<Country> countries;

    public Map(){
        this.countries.Add(new Country("Bali"));
        this.countries.Add(new Country("Brasilien"));
        this.countries.Add(new Country("Australien"));
    }
    public Map(final ArrayList<Country> countries){
        this.countries.AddAll(countries);
    }
    public  ArrayList<Country> getCountries(){
        return this.countries;
    }

}
