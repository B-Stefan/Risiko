package main.java.logic;

import main.java.logic.exceptions.CountriesNotConnectedException;

public class Army {

	private Player owner;
	private Country position;
	
	public Army(Player o, Country p){
		this.owner = o;
		this.position = p;
	}
	public Army(Player o){
		this.owner = o;
	}
	
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
	 * @param country �bergiebt die (neue) Position der Armee
	 */
	public void setPosition(Country country)  throws CountriesNotConnectedException{

        //Armee sitzt bereits auf der Position
        if(country == this.position){
            return;
        }
        //Neue Posituon auf dem Spiefeld
        if(country != null && this.position != country) {

            //Positionswechsel auf der Karte
            if(this.position != null ){
                //Prüfen, ob Länder verbunden sind
                if(!this.position.isConnected(country)){
                    throw new CountriesNotConnectedException(this.position, country);
                };
                this.position.removeArmy(this);
            }
            this.position = country;
            country.addArmy(this);
        }
        //Einheit von Position entfernt
        else {
            if(this.position != null  ){
                this.position.removeArmy(this);
            }
            this.position = country;
        }
	}
	/**
	 * Getter f�r die Position
	 * @return position
	 */
	public Country getPosition(){
		return this.position;
	}
	

}
