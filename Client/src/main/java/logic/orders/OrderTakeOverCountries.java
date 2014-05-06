package main.java.logic.orders;

import main.java.logic.Country;
import main.java.logic.Player;
import main.java.logic.exceptions.PlayerAlreadyHasAnOrderException;

public class OrderTakeOverCountries implements IOrder {
	private boolean withTwoArmies;
	private Player agent;
	
	public OrderTakeOverCountries(boolean twoArmies, Player ag){
		this.withTwoArmies = twoArmies;
		this.agent = ag;
	}

	@Override
	public boolean isCompleted() {
		if (withTwoArmies){
			if(this.agent.getCountries().size()>=18){
				for(Country c : this.agent.getCountries()){
					if(c.getArmyList().size()<2){
						return false;
					}
				}
				return true;
			}
		} else {
			if(this.agent.getCountries().size()>=24){
				return true;
			}
		}
		return false;
	}

	@Override
	public void setAgent(Player ag)  {
			this.agent = ag;
	}
    @Override
    public String toString(){
        //@todo beschreibung verbessern!
        return this.agent + " hat die Aufgabe andere Länder zu erobern" ;
    }
}
