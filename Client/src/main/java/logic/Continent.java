package main.java.logic;
import java.util.*;
	

public class Continent{
	
	/**
	 * Name des Kontinents
	 */
	private String name;
	
	/**
	 * Eine ArrayLsit mit den Ländern, die sich auf dem Kontinent befinden Länder
	 */
	private List<Country> countryList = new ArrayList<Country>();
	
	
	public Continent (String n){
		this.name = n;
	}
	/**
	 * 
	 * @return Gibt den Namen des KOntinents zurück
	 */
	public String getName(){
		return this.name;
	}
	
	/**
	 * 
	 * @param p Der Spieler, der in die Liste hinzugefügt werden soll
	 */
	public void addPlayer(Country p){
		countryList.add(p);
	}
	
	
}