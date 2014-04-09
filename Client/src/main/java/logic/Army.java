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
	 * Setter für die Position
	 * @param p übergiebt die (neue) Position der Armee
	 */
	public void setPosition(Country p){
		this.position = p;
	}
	/**
	 * Getter für die Position
	 * @return position
	 */
	public Country getPosition(){
		return this.position;
	}
}
