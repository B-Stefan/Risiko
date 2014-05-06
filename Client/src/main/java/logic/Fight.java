package main.java.logic;
import java.util.*;

import main.java.logic.exceptions.*;
import main.java.logic.utils.Dice;


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
		if(this.agressorsArmies.size() == 1){
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
	
	private ArrayList<Dice> dice(int amountOfDice, Stack<Army> dicer) throws InvalidAmountOfArmiesException{
		int size = dicer.size();
		if(size==0){
			throw new InvalidAmountOfArmiesException(size);
		}else if(size>amountOfDice){
			size = amountOfDice;
		}
		ArrayList<Dice> dD = new ArrayList<Dice>();
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
        this.toBeConquertedCountry.setOwner(agressor);
	}
	
	public void armyVsArmy(int numerOfAttacArmys) throws NotEnoughArmiesToAttackException, NotEnoughArmiesToDefendException,ToManyArmiesToAttackException, InvalidAmountOfArmiesException, InvalidPlayerException, CountriesNotConnectedException{
		if(!enoughArmiesToAttack()){
			throw new NotEnoughArmiesToAttackException();
		}else if (!enoughArmiesToDefend()) {
            throw new NotEnoughArmiesToDefendException();

        }else if (numerOfAttacArmys > 3 ){
            throw new ToManyArmiesToAttackException();
		}else{
			ArrayList<Dice> agressorsDice = dice(numerOfAttacArmys, this.agressorsArmies);
			ArrayList<Dice> defendersDice = dice(2, this.defendersArmies);
			while(!defendersDice.isEmpty()){
				if(agressorsDice.get(0).isDiceHigher(defendersDice.get(0))){
					battle(this.defender);
					if(!enoughArmiesToDefend()){
						takeOver();
						agressorsDice.remove(0);
						defendersDice.remove(0);
					}
				}else if(!agressorsDice.get(0).isDiceHigher(defendersDice.get(0))){
					battle(this.agressor);
					agressorsDice.remove(0);
					defendersDice.remove(0);
				}
			}
		}
	}
    public String toString (){
        return "Fight";
    }
}
