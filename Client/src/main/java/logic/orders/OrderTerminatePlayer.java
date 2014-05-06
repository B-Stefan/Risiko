package main.java.logic.orders;

import main.java.logic.Player;
import main.java.logic.exceptions.PlayerAlreadyHasAnOrderException;
import main.resources.IOrder;

import java.util.*;

/**
 * Created by Stefan on 31.03.2014.
 * @author Stefan Bieliauskas
 * Diese Klasse ist fÃ¼r die Erfassung der AuftrÃ¤ge ausgelegt,
 * die darauf abzielen einen anderen Spieler zu vernichten.
 *
 *
 * @see main.resources.IOrder
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
	 * Liste aller Spieler (Zur Ermittlung des Opfers)
	 */
	private ArrayList<Player> players = new ArrayList<Player>();
	
	/**
	 * Zuerst wird der Agent gesetzt, damit ausgeschlossen werden kann, dass das Victim und der Player identisch sind
	 * @param players Liste aller Spieler
	 * @param ag Der Spieler, dem die Order zugewiesen werden soll
	 * @throws PlayerAlreadyHasAnOrderException
	 */
	 public OrderTerminatePlayer(ArrayList<Player> players, Player ag) throws PlayerAlreadyHasAnOrderException {
		 this.players = players;
		 shufflePlayersLists();
		 setAgent(ag);
		 findVictim();
	 }
	 
	 /**
	  * Nimmt sich den ersten Spieler aus der zuvor im Constructor geshuffelten Liste der Spieler
	  * Überprüft danach, ob Victim und Agent identisch sind. Wenn sie identisch sind, dann setzt er das Victim mit dem zweiten Spieler aus der Liste
	  */
	 public void findVictim() { 
		 Player victim = this.players.get(0);
		 if (victim == this.agent){
			 victim = this.players.get(1);
		 } 
		 setVictim(victim);
	 }
	 
	 /**
	  * Setzt den Spieler, dem die Order zugewiesen ist und setzt zudem beim Spieler den Auftrag
	  */
	public void setAgent(Player ag) throws PlayerAlreadyHasAnOrderException {
		if(ag.getOrder() == null){
			this.agent = ag;
			this.agent.setOrder(this);
		}else{
			throw new PlayerAlreadyHasAnOrderException(ag);
		}		
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
	 * Shuffelt die Liste der Spieler (Damit die Reihenfolge zufällig ist)
	 */
	public void shufflePlayersLists(){
		Collections.shuffle(this.players);
	}
	/**
	 * Getter für den Agent
	 * @return Player
	 */

	public Player getAgent() {
		return this.agent;
	}
}
