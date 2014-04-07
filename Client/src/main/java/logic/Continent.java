package main.java.logic;
import java.util.*;
	

public class Continent{
	
	/**
	 * Name des Kontinents
	 */
	private final String name;

	/**
	 * Eine ArrayLsit mit den L�ndern, die sich auf dem Kontinent befinden L�nder
     * @return Gibt die Liste der Länder zurück, die disem Kontinent zugeordnet wurden
	 */
	private List<Country> countryList = new ArrayList<Country>();

    /**
     *
     * @param n Name des Kontinents
     */
	public Continent (final String n){
		this.name = n;
	}
	/**
	 * 
	 * @return Gibt den Namen des KOntinents zur�ck
	 */
	public String getName(){
		return this.name;
	}
	
	/**
	 * 
	 * @param p Der Spieler, der in die Liste hinzugef�gt werden soll
	 */
	public void addPlayer(Country p){
		countryList.add(p);
	}
	
	
}