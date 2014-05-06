package main.java.logic.orders;

import main.java.logic.Player;
import main.java.logic.exceptions.PlayerAlreadyHasAnOrderException;

import java.util.*;

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
	private Player victim;
	private Player agent;
	
	 public OrderTerminatePlayer(Player victim, Player ag)  {
         this.setVictim(victim);
		 this.setAgent(ag);
	 }

	public void setAgent(Player ag)  {
		this.agent = ag;
	}
	
    @Override
    public boolean isCompleted() {
    	if (this.victim.getCountries().isEmpty()) {
            return true;
        } else {
        	return false;
        }
    }

    
	public Player getVictim() {
		return victim;
	}

	public void setVictim(Player victim) {
		this.victim = victim;
	}


	public Player getAgent() {
		return this.agent;
	}

    @Override
    public String toString(){
        //@todo beschreibung verbessern!
        return this.agent + " hat die Aufgabe den Spieler " + this.getVictim() + " zu vernichten"  ;
    }
}
