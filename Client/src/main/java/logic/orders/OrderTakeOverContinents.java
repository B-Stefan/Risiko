package main.java.logic.orders;
import java.util.*;

import main.java.logic.*;
import main.java.logic.exceptions.PlayerAlreadyHasAnOrderException;
import main.resources.*;

public class OrderTakeOverContinents implements IOrder {
	private Continent continentOne;
	private Continent continentTwo;
	private List<Continent> continents = new ArrayList<Continent>();
	private Player agent;
	
	public OrderTakeOverContinents(Continent one, Continent two, Player pl, ArrayList<Continent> continents) throws PlayerAlreadyHasAnOrderException{
		this.continentOne = one;
		this.continentTwo = two;
		this.continents = continents;
		setAgent(pl);
	}
	public OrderTakeOverContinents(Continent one, Continent two, Player pl) throws PlayerAlreadyHasAnOrderException{
		this.continentOne = one;
		this.continentTwo = two;
		this.continents = null;
		setAgent(pl);
	}
	
	private boolean thirdContinent(){
		for(Continent c : this.continents){
			if(c != this.continentOne && c != this.continentTwo && c != null){
				if(c.getCurrentOwner() == this.agent){
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	public boolean isCompleted() {
		if (this.continents == null){
			if(this.agent == this.continentOne.getCurrentOwner() && this.agent == this.continentTwo.getCurrentOwner()){
				return true;
			}
		}else{
			if(thirdContinent() && this.agent == this.continentOne.getCurrentOwner() && this.agent == this.continentTwo.getCurrentOwner()){
				return true;
			}
		}
		return false;
	}

	public void setAgent(Player ag) throws PlayerAlreadyHasAnOrderException {
		if(ag.getOrder() == null){
			this.agent = ag;
			this.agent.setOrder(this);
		}else{
			throw new PlayerAlreadyHasAnOrderException(ag);
		}
		
	}
}
