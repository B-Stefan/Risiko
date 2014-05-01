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
	
	private Stack<Dice> defendersDiceList() throws InvalidAmountOfArmiesException{
		int size = this.defendersArmies.size();
		if(size>=2){
			Stack<Dice> dD = new Stack<Dice>();
			dD.add(new Dice());
			dD.add(new Dice());
			Collections.sort(dD);
			Collections.reverse(dD);
			return dD;
		}else if (size==1){
			Stack<Dice> dD = new Stack<Dice>();
			dD.add(new Dice());
			return dD;
		}else{
			throw new InvalidAmountOfArmiesException(size);
		}
	}
	
	private Stack<Dice> agressorsDiceList() throws InvalidAmountOfArmiesException{
		int size = this.agressorsArmies.size();
		if(size>=3){
			Stack<Dice> aD = new Stack<Dice>();
			aD.add(new Dice());
			aD.add(new Dice());
			aD.add(new Dice());
			Collections.sort(aD);
			Collections.reverse(aD);
			return aD;
		}else if(size==2){
			Stack<Dice> aD = new Stack<Dice>();
			aD.add(new Dice());
			aD.add(new Dice());
			Collections.sort(aD);
			Collections.reverse(aD);
			return aD;
		}else if (size==1){
			Stack<Dice> aD = new Stack<Dice>();
			aD.add(new Dice());
			return aD;
		}else{
			throw new InvalidAmountOfArmiesException(size);
		}
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
			Stack<Dice> agressorsDice = agressorsDiceList();
			Stack<Dice> defendersDice = defendersDiceList();
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
