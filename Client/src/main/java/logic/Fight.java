package main.java.logic;
import java.util.*;

import main.java.logic.exceptions.*;


public class Fight {
	private Player agressor;
	private Player defender;
	private Stack<Army> agressorsArmies = new Stack<Army>();
	private Stack<Army> defendersArmies = new Stack<Army>();
	private Country toBeConquertedCountry;
	private Country originCountry;
	
	public Fight(Country from, Country to, Stack<Army> aggArmies, Stack<Army> defArmies){
		this.agressor = from.getOwner();
		this.defender = to.getOwner();
		this.agressorsArmies = aggArmies;
		this.defendersArmies = defArmies;
		this.toBeConquertedCountry = to;
		this.originCountry = from;
	}
	
	public Fight(Country from, Country to, List<Army> aggArmies, List<Army> defArmies){
		this.agressor = from.getOwner();
		this.defender = to.getOwner();
		this.agressorsArmies = listToStack(aggArmies);
		this.defendersArmies = listToStack(defArmies);
		this.toBeConquertedCountry = to;
		this.originCountry = from;
	}
	
	private Stack<Army> listToStack(List<Army> aL){
		Stack<Army> stack = new Stack<Army>();
		stack.addAll(aL);
		return stack;
	}
	
	private boolean enoughArmiesToAttack(){
		if(this.agressorsArmies.isEmpty()){
			return false;
		}
		return true;
	}
	
	private boolean enoughArmiesToDefend(){
		if(this.defendersArmies.isEmpty()){
			return false;
		}
		return true;
	}
	
	private void battle(Player loser)throws InvalidPlayerException, CountriesNotConnectedException{
		if(loser==this.agressor){
			Army destroyedArmy = this.agressorsArmies.pop();
			this.originCountry.removeArmy(destroyedArmy);
		}else if (loser==this.defender){
			Army destroyedArmy = this.defendersArmies.pop();
			this.toBeConquertedCountry.removeArmy(destroyedArmy);
		}else{
			throw new InvalidPlayerException();
		}
	}
	
	private Stack<Dice> dice(int amountOfDice, Stack<Army> dicer) throws InvalidAmountOfArmiesException{
		int size = dicer.size();
		if(size==0){
			throw new InvalidAmountOfArmiesException(size);
		}else if(size>amountOfDice){
			size = amountOfDice;
		}
		Stack<Dice> dD = new Stack<Dice>();
		while(size>0){
			dD.add(new Dice());
			size--;
		}
		Collections.sort(dD);
		Collections.reverse(dD);
		return dD;
	}
	
	private void takeOver() throws CountriesNotConnectedException{
		for(Army a : this.agressorsArmies){
			a.setPosition(this.toBeConquertedCountry);
		}
	}
	
	public void armyVsArmy() throws NotEnoughArmiesToAttackException, NotEnoughArmiesToDefendException, InvalidAmountOfArmiesException, InvalidPlayerException, CountriesNotConnectedException{
		if(!enoughArmiesToAttack()){
			throw new NotEnoughArmiesToAttackException();
		}else if (!enoughArmiesToDefend()){
			throw new NotEnoughArmiesToDefendException();
		}else{
			Stack<Dice> agressorsDice = dice(3, this.agressorsArmies);
			Stack<Dice> defendersDice = dice(2, this.defendersArmies);
			while(!defendersDice.isEmpty()){
				if(agressorsDice.pop().higherDice(defendersDice.pop())){
					battle(this.defender);
					if(!enoughArmiesToDefend()){
						takeOver();
					}
				}else if(!agressorsDice.get(0).higherDice(defendersDice.pop())){
					battle(this.agressor);
				}
			}
		}
	}	
}
