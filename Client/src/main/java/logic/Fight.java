package main.java.logic;
import java.util.*;

import main.java.logic.exceptions.CountriesNotConnectedException;
import main.java.logic.exceptions.NotEnoughArmiesToAttackException;
import main.java.logic.utils.*;
import main.java.logic.exceptions.*;

public class Fight {
	
	/**
	 * Das Land, von dem aus angegriffen wird
	 */
	private Country from;
	/**
	 * Das Land, welches angegriffen wird
	 */
	private Country to;
	/**
	 * Der Angreifer
	 */
	private Player agressor;
	/**
	 * Die Liste der Armeen, mit denen in diesem Gefecht verteidigt wird
	 */
	private Stack<Army> defendersArmies = new Stack<Army>();
	/**
	 * Die Liste der Armeen, mit denen in diesem Gefecht angegriffen wird
	 */
	private Stack<Army> agressorsArmies = new Stack<Army>();
	/**
	 * Liste der Würfel des Verteidigers
	 */
	private Stack<Dice> defendersDice = new Stack<Dice>();
	/**
	 * Liste der Würfel des Angreifers
	 */
	private Stack<Dice> agressorsDice = new Stack<Dice>();
	
	
	
	public Fight(Country from, Country to){
		this.to = to;
		this.from = from;
		this.agressor = this.from.getOwner();
	}
	
	/**
	 * Formatiert eine List in einen Stack
	 * @param aL
	 * @return
	 */
	private Stack<Army> listToStack(List<Army> aL){
		Stack<Army> stack = new Stack<Army>();
		stack.addAll(aL);
		return stack;
	}
	
	/**
	 * Bestimmt die Würfel mit denen angegriffen werden soll (und setzt die Liste der Angreifer Armeen und der Angreifer Würfel)
	 * @param agressorsArmies
	 * @throws InvalidAmountOfArmiesException
	 * @throws NotEnoughArmiesToAttackException 
	 */
	public void attacking(List<Army> agressorsArmies) throws InvalidAmountOfArmiesException, NotEnoughArmiesToAttackException{
		this.agressorsArmies = listToStack(agressorsArmies);
		//Der Angreifer muss mit mindestens mit einer und höchstens mit drei Armeen angreifen 
		if(this.agressorsArmies.size() >3 || this.agressorsArmies.size()<1){
			throw new InvalidAmountOfArmiesException(this.agressorsArmies.size(), "1 & 3");
		}
		//Es kann nur angegriffen werden, wenn sich mehr als eine Armee auf dem Ursprungsland befindet
		if(this.from.getArmyList().size()<=1){
			throw new NotEnoughArmiesToAttackException();
		}
		//füllt die Liste mit so vielen Würfeln, wie es Armeen gibt
		for(int i = this.agressorsArmies.size(); i>0; i--){
			this.agressorsDice.add(new Dice());
		}
		//Sortiert die Liste der Würfel absteigend nach Wert
		//möglicherweise als Methode auslagern?
		Collections.sort(this.agressorsDice);
		Collections.reverse(this.agressorsDice);
	}
	
	/**
	 * Bestimmt die Würfel mit denen verteidigt werden soll (und setzt die Liste der Verteidiger Armeen und der Verteidiger Würfel)
	 * @param defendersArmies
	 * @throws InvalidAmountOfArmiesException
	 */
	public void defending(List<Army> defendersArmies)throws InvalidAmountOfArmiesException{
		this.defendersArmies = listToStack(defendersArmies);
		//Der Verteidiger muss mindestens mit einer und höchstens mit zwei Armeen verteidigen
		if(this.defendersArmies.size() >2 || this.defendersArmies.size()<1){
			throw new InvalidAmountOfArmiesException(this.defendersArmies.size(), "1 & 2");
		}
		//füllt die Liste mit so vielen Würfeln, wie es Armeen gibt
		for(int i = this.defendersArmies.size(); i>0; i--){
			this.defendersDice.add(new Dice());
		}
		//Sortiert die Liste der Würfel absteigend nach Wert
		//möglicherweise als Methode auslagern?
		Collections.sort(this.defendersDice);
		Collections.reverse(this.defendersDice);
	}
	
	/**
	 * Vergleicht die Würfel des Verteidigers mit denen des Angreifers und ermittelt wer diesen Fight gewonnen hat
	 * @throws CountriesNotConnectedException
	 */
	public void result() throws CountriesNotConnectedException{
		for(Dice di : this.defendersDice){
			//Wenn der Würfel des Verteidigers höher oder gleich ist, dann wird eine Armee des Angreifers zerstört
			if(di.isDiceHigherOrEqual(this.agressorsDice.get(0))){
				this.from.removeArmy(this.agressorsArmies.pop());
				this.agressorsDice.remove(0);
			//Wenn der Würfel niedriger ist, dann wird eine Armee des Verteidigers zerstört
			}else if(!di.isDiceHigherOrEqual(this.agressorsDice.get(0))){
				this.from.removeArmy(this.defendersArmies.pop());
				this.agressorsDice.remove(0);
				//Wenn keine Armeen des Verteidigers mehr auf dem zu erobernden Land stehen, dann ziehen die Armeen des Angreifers rüber
				//Möglicherweise eigene Methode?
				if(this.to.getArmyList().isEmpty()){
					this.to.setOwner(this.agressor);
					for(Army a : this.agressorsArmies){
						a.setPosition(this.to);
					}
				}
			}
		}
	}
	
	/**
	 * Getter für die Liste der Würfel des Angreifers
	 * @return Stack<Dice>
	 */
	public Stack<Dice> getAgressorsDice(){
		return this.agressorsDice;
	}
	
	/**
	 * Getter für die Liste der Würfel des Verteidigers
	 * @return Stack<Dice>
	 */
	public Stack<Dice> getDefendersDice(){
		return this.defendersDice;
	}
	
	/**
	 * Setter für die Liste der Armeen des Verteidigers
	 * @param newArmies
	 */
	public void setDefendersArmies(Stack<Army> newArmies){
		this.defendersArmies = newArmies;
	}
	
	/**
	 * Setter für die Liste der Armeen des Angreifers
	 * @param newArmies
	 */
	public void setAgressorsArmies(Stack<Army> newArmies){
		this.agressorsArmies = newArmies;
	}
	
}