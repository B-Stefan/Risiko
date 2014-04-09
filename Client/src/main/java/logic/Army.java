package main.java.logic;

public class Army {

	private Player owner;
	private Country position;
	
	/**
	 * Setzt Spieler p als Besitzer des Landes
	 * @param o der Spieler, der Besitzer der Armee wird
	 */
	public void setOwner(Player o){
		this.owner = o;
	}
	/**
	 * Gibt den Besitzer der Armee wieder
	 * @return owner: besitzer der Armee 
	 */
	public Player getOwner(){
		return this.owner;
	}
	/**
	 * Setter f�r die Position
	 * @param p �bergiebt die (neue) Position der Armee
	 */
	public void setPosition(Country p){
		this.position = p;
	}
	/**
	 * Getter f�r die Position
	 * @return position
	 */
	public Country getPosition(){
		return this.position;
	}
}
