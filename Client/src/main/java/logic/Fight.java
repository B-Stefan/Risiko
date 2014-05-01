package main.java.logic;
import java.util.*;

import main.java.logic.exceptions.CountriesNotConnectedException;
import main.java.logic.exceptions.CountryNotInListException;
import main.java.logic.exceptions.NotEnoughArmiesToAttackException;

public class Fight {
	private Player agressor;
	private Player defender;
	private Stack<Army> agressorsArmies = new Stack<Army>();
	private Stack<Army> defendersArmies = new Stack<Army>();
	private Country toBeConquertedCountry;
	private Country originCountry;
	
	private Fight(Player agg, Player def, Country from, Country to, Stack<Army> aggArmies, Stack<Army> defArmies){
		this.agressor = agg;
		this.defender = def;
		this.agressorsArmies = aggArmies;
		this.defendersArmies = defArmies;
		this.toBeConquertedCountry = to;
		this.originCountry = from;
	}
	
	public boolean enoughArmiesToAttack(){
		if(this.agressorsArmies.isEmpty()){
			return false;
		}
		return true;
	}
	
	public boolean enoughArmiesToDefend(){
		if(this.defendersArmies.isEmpty()){
			return false;
		}
		return true;
	}
	
	public void armyVsArmy() throws NotEnoughArmiesToAttackException, CountriesNotConnectedException, CountryNotInListException{
		if(!enoughArmiesToAttack()){
			throw new NotEnoughArmiesToAttackException();
		}else if (!enoughArmiesToDefend()){
			this.toBeConquertedCountry.changeOwner(this.agressor);
			Army toBeMovedArmy = this.agressorsArmies.pop();
			
		}else{
			Dice dice1 = new Dice();
			Dice dice2 = new Dice();
			dice1.throwDice();
			dice2.throwDice();
			if(dice1.getDiceNumber()>dice2.getDiceNumber()){
				Army destroyedArmy = this.defendersArmies.pop();
				this.toBeConquertedCountry.removeArmy(destroyedArmy);
				destroyedArmy = null;
				if(!enoughArmiesToDefend()){
					this.toBeConquertedCountry.setOwner(agressor);
				}
			}else if(dice2.getDiceNumber()>=dice1.getDiceNumber()){
				Army destroyedArmy = this.agressorsArmies.pop();
				destroyedArmy = null;
			}
		}
		
	}
	
	
}
