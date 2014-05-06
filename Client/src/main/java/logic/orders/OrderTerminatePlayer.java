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
	private Player victim;
	private Player agent;
	private ArrayList<Player> players = new ArrayList<Player>();
	
	 public OrderTerminatePlayer(ArrayList<Player> players, Player ag) throws PlayerAlreadyHasAnOrderException {
		 this.players = players;
		 shufflePlayersLists();
		 setAgent(ag);
		 findVictim();
	 }
	 
	 public void findVictim() { 
		 Player victim = this.players.get(0);
		 if (victim == this.agent){
			 victim = this.players.get(1);
		 } 
		 setVictim(victim);
	 }
	 

	public void setAgent(Player ag) throws PlayerAlreadyHasAnOrderException {
		if(ag.getOrder() == null){
			this.agent = ag;
			this.agent.setOrder(this);
		}else{
			throw new PlayerAlreadyHasAnOrderException(ag);
		}		
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
	
	public void shufflePlayersLists(){
		Collections.shuffle(this.players);
	}

	public Player getAgent() {
		return this.agent;
	}
}
