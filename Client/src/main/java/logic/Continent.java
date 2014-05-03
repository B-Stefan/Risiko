package main.java.logic;
import java.util.*;
import java.util.Map;


public class Continent{
	
	/**
	 * Name des Kontinents
	 */
	private final String name;

	/**
	 * Eine ArrayLsit mit den L�ndern, die sich auf dem Kontinent befinden Länder
     * @return Gibt die Liste der Länder zurück, die disem Kontinent zugeordnet wurden
	 */
	private HashMap<String,Country> countrys = new HashMap<String, Country>();

	/**
	 * der Bonus, der die Kontrolle des jeweiligen Kontinents einbringt für die neuen Armeen
	 */
	private int bonus;
	
    /**
     *
     * @param n Name des Kontinents
     */
	public Continent (final String n, int b){
		this.name = n;
		this.bonus = b;
	}

	/**
	 * 
	 * @return Gibt den Namen des KOntinents zur�ck
	 */
	
	public String getName(){
		return this.name;
	}
	


    /**
     * Ermittelt den aktuellen Besitzter des Kontinents, prüft ob alle Länder den gleichen Player als owner haben
     * @return Gibt den Player zurück, der alle Länder auf diesem Kontinent besitzt, wenn der Kontinent geteilt wird wird null zurück gegeben.
     */
    public Player getCurrentOwner (){
        boolean playerChange = false;
        Player ruler = null;

        for (Map.Entry<String,Country> entry : countrys.entrySet()){
            Country currentCountry = entry.getValue();

            if(ruler != null && ruler!= currentCountry.getOwner()){
                playerChange = true;
            }
            else if(ruler == null ){
                ruler = currentCountry.getOwner();
            }

        }
        if(!playerChange){
            return  ruler;
        }
        else {
            return null;
        }
    }
    
	/**
	 * 
	 * @param p Der Land, der dem Kontinent hinzugefügt werden soll
	 */
	public void addCountry(Country p){
		if(!countrys.containsKey(p.getId())){
            countrys.put(p.getId(), p);
        }
	}

    /**
     *
     * @param countryToDelete Land das gelöscht werden soll
     * @return
     */
    public boolean removeCountry (Country countryToDelete){
        if(countrys.containsKey(countryToDelete.getId())){
            countrys.remove(countryToDelete);
            return  true;
        }
        return  false;

    }
	
    public int getBonus(){
    	return this.bonus;
    }
	
}