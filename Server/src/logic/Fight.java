package logic;
import java.util.*;

import interfaces.IFight;
import interfaces.ITurn;
import interfaces.data.IArmy;
import interfaces.data.ICountry;
import interfaces.data.IPlayer;
import interfaces.data.utils.IDice;
import logic.data.Army;
import logic.data.Country;
import logic.data.Player;
import exceptions.AlreadyDicedException;
import exceptions.ArmyAlreadyMovedException;
import exceptions.CountriesNotConnectedException;
import exceptions.InvalidAmountOfArmiesException;
import exceptions.InvalidFightException;
import exceptions.NotEnoughArmiesToAttackException;
import exceptions.TurnNotAllowedStepException;
import exceptions.TurnNotInCorrectStepException;
import logic.utils.*;
import exceptions.*;

public class Fight implements IFight {
	
	/**
	 * Das Land, von dem aus angegriffen wird
	 */
	private ICountry from;
	/**
	 * Das Land, welches angegriffen wird
	 */
	private ICountry to;
	/**
	 * Der Angreifer
	 */
	private IPlayer agressor;
	/**
	 * Die Liste der Armeen, mit denen in diesem Gefecht verteidigt wird
	 */
	private Stack<IArmy> defendersArmies = new Stack<IArmy>();
	/**
	 * Die Liste der Armeen, mit denen in diesem Gefecht angegriffen wird
	 */
	private Stack<IArmy> agressorsArmies = new Stack<IArmy>();
	/**
	 * Liste der Würfel des Verteidigers
	 */
	private Stack<IDice> defendersDice = new Stack<IDice>();
	/**
	 * Liste der Würfel des Angreifers
	 */
	private Stack<IDice> agressorsDice = new Stack<IDice>();
	
	/**
	 * Zeile 1: Anzahl der verlorenen Einheiten des Angreifers
	 * Zeile 2: Anzahl der verlorenen Einheiten des Verteidigers
	 * Zeile 3: Wenn 0 -> this.to wurde nicht erobert, wenn 1 -> this.to wurde erobert
	 */
	private int[] result = new int[3];
	
	/**
	 * Der Zug, in dem sich der Fight befindet
	 */
	private ITurn currentTurn;
	
	
	/**
	 * Konstruktor setzt Attribute
	 * @param from
	 * @param to
	 */
	public Fight(ICountry from, ICountry to, ITurn turn){
		this.to = to;
		this.from = from;
		this.agressor = this.from.getOwner();
		this.currentTurn = turn;
	}
	
	/**
	 * Formatiert eine List in einen Stack
	 * @param aL
	 * @return
	 */
	private Stack<IArmy> listToStack(List<IArmy> aL){
		Stack<IArmy> stack = new Stack<IArmy>();
		stack.addAll(aL);
		return stack;
	}
	
	/**
	 * Attacking überschrieben für CUI
	 * @param agressorsArmies
	 * @throws NotEnoughArmiesToAttackException
	 * @throws InvalidAmountOfArmiesException
	 * @throws AlreadyDicedException 
	 * @throws InvalidFightException 
	 */
	public void attacking(int agressorsArmies) throws NotEnoughArmiesToAttackException, InvalidAmountOfArmiesException, AlreadyDicedException, InvalidFightException{
		Stack<IArmy> agArmies = new Stack<IArmy>();
		for (int i = 0; i<agressorsArmies; i++){
			if (this.from.getArmyList().size()<agressorsArmies){
				throw new NotEnoughArmiesToAttackException();
			}
			agArmies.push(from.getArmyList().get(i));
		}
		attacking(agArmies);
	}
	
	/**
	 * Bestimmt die Würfel mit denen angegriffen werden soll (und setzt die Liste der Angreifer Armeen und der Angreifer Würfel)
	 * @param agressorsArmies
	 * @throws InvalidAmountOfArmiesException
	 * @throws NotEnoughArmiesToAttackException 
	 * @throws AlreadyDicedException 
	 * @throws InvalidFightException 
	 */
	public void attacking(Stack<IArmy> agressorsArmies) throws InvalidAmountOfArmiesException, NotEnoughArmiesToAttackException, AlreadyDicedException, InvalidFightException{
		this.agressorsArmies.clear();
		this.agressorsArmies = agressorsArmies;
		this.defendersDice.clear();
		if (this.from.getOwner() == this.to.getOwner()){
			throw new InvalidFightException();
		}
		if (!this.agressorsDice.isEmpty()){
			throw new AlreadyDicedException();
		}
		//Der Angreifer muss mit mindestens mit einer und höchstens mit drei Armeen angreifen 
		if(this.agressorsArmies.size() >3 || this.agressorsArmies.size()<1){
			throw new InvalidAmountOfArmiesException(this.agressorsArmies.size(), "1 & 3");
		}
		//Es kann nur angegriffen werden, wenn sich mehr als eine Armee auf dem Ursprungsland befindet
		if(this.from.getArmyList().size()<=this.agressorsArmies.size()){
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
	 * Defending überschrieben für CUI
	 * @param defendersArmies
	 * @throws NotEnoughArmiesToAttackException
	 * @throws InvalidAmountOfArmiesException
	 * @throws CountriesNotConnectedException 
	 * @throws AlreadyDicedException 
	 * @throws ArmyAlreadyMovedException 
	 * @throws TurnNotInCorrectStepException 
	 * @throws TurnNotAllowedStepException 
	 * @throws InvalidFightException
     * @throws AggessorNotThrowDiceException
	 */
	public void defending(int defendersArmies) throws AggessorNotThrowDiceException, ToManyNewArmysException,NotEnoughArmiesToDefendException,NotEnoughArmysToMoveException, InvalidAmountOfArmiesException, CountriesNotConnectedException, AlreadyDicedException, TurnNotAllowedStepException, TurnNotInCorrectStepException, ArmyAlreadyMovedException, InvalidFightException, NotTheOwnerException{
		if(this.agressorsDice.isEmpty()){
            throw new AggessorNotThrowDiceException();
        }
        Stack<IArmy> defArmies = new Stack<IArmy>();
		if (this.to.getArmyList().size()<defendersArmies){
			throw new NotEnoughArmiesToDefendException();
		}
		for (int i = 0; i<defendersArmies; i++){
			defArmies.push(to.getArmyList().get(i));
		}
		defending(defArmies);
	}
	
	/**
	 * Bestimmt die Würfel mit denen verteidigt werden soll (und setzt die Liste der Verteidiger Armeen und der Verteidiger Würfel)
	 * @param defendersArmies
	 * @throws InvalidAmountOfArmiesException
	 * @throws CountriesNotConnectedException 
	 * @throws AlreadyDicedException 
	 * @throws ArmyAlreadyMovedException 
	 * @throws TurnNotInCorrectStepException 
	 * @throws TurnNotAllowedStepException 
	 * @throws InvalidFightException 
	 */
	public void defending(Stack<IArmy> defendersArmies)throws ToManyNewArmysException,InvalidAmountOfArmiesException, NotEnoughArmysToMoveException,CountriesNotConnectedException, AlreadyDicedException, TurnNotAllowedStepException, TurnNotInCorrectStepException, ArmyAlreadyMovedException, InvalidFightException, NotTheOwnerException{
		if (this.from.getOwner() == this.to.getOwner()){
			throw new InvalidFightException();
		}
		if (!this.defendersDice.isEmpty()){
			throw new AlreadyDicedException();
		}
		this.defendersArmies.clear();
		this.defendersArmies = defendersArmies;
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
		setResult();
	}
	
	/**
	 * Vergleicht die Würfel des Verteidigers mit denen des Angreifers und ermittelt wer diesen Fight gewonnen hat
	 * @throws CountriesNotConnectedException
	 * @throws ArmyAlreadyMovedException 
	 * @throws TurnNotInCorrectStepException 
	 * @throws TurnNotAllowedStepException 
	 */
	private int[] result() throws ToManyNewArmysException, CountriesNotConnectedException,NotEnoughArmysToMoveException, TurnNotAllowedStepException, TurnNotInCorrectStepException, ArmyAlreadyMovedException, NotTheOwnerException{
		int[] res = new int[3];
		for(IDice di : this.defendersDice){
			//Wenn der Würfel des Verteidigers höher oder gleich ist, dann wird eine Armee des Angreifers zerstört
			if(di.isDiceHigherOrEqual(this.agressorsDice.get(0))){
				this.from.removeArmy(this.agressorsArmies.pop());
				this.agressorsDice.remove(0);
				res[0] += 1;
			//Wenn der Würfel niedriger ist, dann wird eine Armee des Verteidigers zerstört
			}else if(!di.isDiceHigherOrEqual(this.agressorsDice.get(0))){
				this.to.removeArmy(this.defendersArmies.pop());
				this.agressorsDice.remove(0);
				res[1] += 1;
				//Wenn keine Armeen des Verteidigers mehr auf dem zu erobernden Land stehen, dann ziehen die Armeen des Angreifers rüber
				//Möglicherweise eigene Methode?
				if(this.to.getArmyList().isEmpty()){
					this.currentTurn.setTakeOverSucess(true);
					this.to.setOwner(this.agressor);
					for(IArmy a : this.agressorsArmies){
						this.currentTurn.moveArmy(this.from,this.to, a);
					}
					res[2] = 1;
				}
			}
		}
		this.agressorsDice.clear();
		// muss geelehrt werden nachdem die wwürfel ausgegeben wurden aber nicht am Anfang des 
		return res;
	}
	
	/**
	 * getter für das Result
	 * @return
	 */
	public int[] getResult(){
		return this.result;
	}


	private void setResult() throws ToManyNewArmysException,CountriesNotConnectedException,NotEnoughArmysToMoveException, TurnNotAllowedStepException, TurnNotInCorrectStepException, ArmyAlreadyMovedException, NotTheOwnerException{
		this.result = result();
	}
	
	/**
	 * Getter für die Liste der Würfel des Angreifers
	 * @return Stack<Dice>
	 */
	public Stack<IDice> getAgressorsDice(){
		return this.agressorsDice;
	}
	
	/**
	 * Getter für die Liste der Würfel des Verteidigers
	 * @return Stack<Dice>
	 */
	public Stack<IDice> getDefendersDice(){
		return this.defendersDice;
	}
	
	/**
	 * Setter für die Liste der Armeen des Verteidigers
	 * @param newArmies
	 */
	public void setDefendersArmies(Stack<IArmy> newArmies){
		this.defendersArmies = newArmies;
	}
	
	/**
	 * Setter für die Liste der Armeen des Angreifers
	 * @param newArmies
	 */
	public void setAgressorsArmies(Stack<IArmy> newArmies){
		this.agressorsArmies = newArmies;
	}
	/**
	 * Getter für das To Land
	 * @return
	 */
	public ICountry getTo(){
		return this.to;
	}
	
	/**
	 * Getter für das From Land
	 * @return
	 */
	public ICountry getFrom(){
		return this.from;
	}

    public IPlayer getAggressor (){
        return  this.agressor;
    }
    public IPlayer getDefender (){
        return this.getTo().getOwner();
    }
	
}