package main.java.logic.data;

import main.java.logic.exceptions.CountriesNotConnectedException;

/**
 * @author Jennifer Theloy, Thu Nguyen, Stefan Bieliauskas
 *
 * Diese Klasse bildet eine einzelne Armee im Spiel ab
 */
public class Army {

    /**
     * Der Owner beschreibt wem diese Armee im Spiel zugeordnet wird.
     */
	private final Player owner;


    /**
     * Die Position spiegelt dabei immer die aktuelle Position dieser Armee da
     */
	private Country position;


    /**
     * Erstellt eine Armee für das Spiel, der Besitzer kann nachträglich nicht geändert werden
     * @param player Besitzer dieser Armee
     */
	public Army(final Player player){
		this.owner = player;
	}


	/**
	 * Setzt die Armee auf die neue Position.
     * Dabei wird gleichzeigt der die 1:1 Zuordnung zum Country beachtet, sodass eine Armee immer nur auf einem Country steht.
     * Auf der anderen Seite sind auch alle Armeen, die einem Country zugewiesen wurden auf dieser Position gesetzt.
     * Dies erhöht die Datenintegrität
     *
     *
	 * @param country Übergiebt die (neue) Position der Armee
	 *
     * @see Country#addArmy(Army)
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
     * Gibt den Besitzer der Armee wieder
     * @return  Besitzer der Armee
     */
    public Player getOwner(){
        return this.owner;
    }


    /**
	 * Getter für die Position
	 * @return position Aktuelle Position der Armee, kann null zurückgeben.
	 */
	public Country getPosition(){
		return this.position;
	}
	

}
