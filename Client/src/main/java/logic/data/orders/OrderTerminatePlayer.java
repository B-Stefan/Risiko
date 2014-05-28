package main.java.logic.data.orders;

import main.java.logic.data.Player;

/**
 * Created by Stefan on 31.03.2014.
 * @author Stefan Bieliauskas
 * Diese Klasse ist fÃ¼r die Erfassung der AuftrÃ¤ge ausgelegt,
 * die darauf abzielen einen anderen Spieler zu vernichten.
 *
 *
 * @see IOrder
 */
public class OrderTerminatePlayer implements IOrder{
	/**
	 * Der Spieler, der von dem Spieler, dem die Order zugeiwesen ist, vernichtet werden soll
	 */
	private Player victim;
	/**
	 * Der Spieler, dem die Order zugewiesen ist
	 */
	private Player agent;


	
	/**
	 * Zuerst wird der Agent gesetzt, damit ausgeschlossen werden kann, dass das Victim und der Player identisch sind
	 * @param victim Opfer des Auftrags
	 * @param ag Der Spieler, dem die Order zugewiesen werden soll
	 */
	 public OrderTerminatePlayer(Player victim, Player ag)  {

         this.victim = victim;
         this.agent = ag;

	 }

	 
	 /**
	  * Setzt den Spieler, dem die Order zugewiesen ist und setzt zudem beim Spieler den Auftrag
	  */
	public void setAgent(Player ag)  {
        this.agent = ag;
	}
	
	/**
	 * prüft, ob das Victim vernichtet wurde. Dieser wurde vernichtet, wenn er keine Länder mehr besitzt. 
	 */
	
    @Override
    public boolean isCompleted() {
    	if (this.victim.getCountries().isEmpty()) {
            return true;
        } else {
        	return false;
        }
    }
    
    /**
     * Gibt den zu vernichtenden Spieler wieder
     * @return Player
     */
    
	public Player getVictim() {
		return victim;
	}
	/**
	 * Setter für Victim
	 * @param victim
	 */

	public void setVictim(Player victim) {
		this.victim = victim;
	}


	/**
	 * Getter für den Agent
	 * @return Player
	 */
	public Player getAgent() {
		return this.agent;
	}

    @Override
    public String toString(){
        //@todo beschreibung verbessern!
        return this.agent + " hat die Aufgabe den Spieler " + this.getVictim() + " zu vernichten"  ;
    }
}
