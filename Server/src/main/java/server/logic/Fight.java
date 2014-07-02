package server.logic;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

import configuration.FightConfiguration;
import interfaces.IClient;
import interfaces.IFight;
import interfaces.data.IPlayer;
import interfaces.data.utils.IDice;
import exceptions.AlreadyDicedException;
import exceptions.ArmyAlreadyMovedException;
import exceptions.CountriesNotConnectedException;
import exceptions.InvalidAmountOfArmiesException;
import exceptions.InvalidFightException;
import exceptions.NotEnoughArmiesToAttackException;
import exceptions.TurnNotAllowedStepException;
import exceptions.TurnNotInCorrectStepException;
import exceptions.YouCannotAttackException;
import exceptions.YouCannotDefendException;
import server.ClientManager;
import server.logic.data.Army;
import server.logic.data.Country;
import server.logic.data.Player;
import server.logic.utils.*;
import exceptions.*;

public class Fight extends UnicastRemoteObject implements IFight {
	
	/**
	 * Das Land, von dem aus angegriffen wird
	 */
	private final Country from;
	/**
	 * Das Land, welches angegriffen wird
	 */
	private final Country to;
	/**
	 * Der Angreifer
	 */
	private final Player agressor;
	/**
	 * Die Liste der Armeen, mit denen in diesem Gefecht verteidigt wird
	 */
	private final Stack<Army> defendersArmies = new Stack<Army>();
	/**
	 * Die Liste der Armeen, mit denen in diesem Gefecht angegriffen wird
	 */
	private final Stack<Army> agressorsArmies = new Stack<Army>();
	/**
	 * Liste der Würfel des Verteidigers
	 */
	private final Stack<Dice> defendersDice = new Stack<Dice>();
	/**
	 * Liste der Würfel des Angreifers
	 */
	private final Stack<Dice> agressorsDice = new Stack<Dice>();

	/**
	 * Zeile 1: Anzahl der verlorenen Einheiten des Angreifers
	 * Zeile 2: Anzahl der verlorenen Einheiten des Verteidigers
	 * Zeile 3: Wenn 0 -> this.to wurde nicht erobert, wenn 1 -> this.to wurde erobert
	 */
	private int[] result = new int[3];
	
	/**
	 * Der Zug, in dem sich der Fight befindet
	 */
	private Turn currentTurn;

    /**
     * Client Manager um nachrichten an die Clients zu verteilen
     */
    private final ClientManager clientManager;
	
	
	/**
	 * Konstruktor setzt Attribute
	 * @param from
	 * @param to
	 */
	public Fight(final Country from, final Country to, final Turn turn, final ClientManager clientManager) throws RemoteException{
		this.to = to;
		this.from = from;
		this.currentTurn = turn;
        this.agressor = from.getOwner();
        this.clientManager = clientManager;
	}

	
	/**
	 * Attacking überschrieben für CUI
	 * @param agressorsArmies
	 * @throws NotEnoughArmiesToAttackException
	 * @throws InvalidAmountOfArmiesException
	 * @throws AlreadyDicedException 
	 * @throws InvalidFightException 
	 * @throws YouCannotAttackException 
	 */
	public void attacking(int agressorsArmies, IPlayer clientPlayer) throws NotEnoughArmiesToAttackException, InvalidAmountOfArmiesException, AlreadyDicedException, InvalidFightException, RemoteException, YouCannotAttackException{
		if(!this.agressor.getColor().equals(clientPlayer.getColor())){
			throw new YouCannotAttackException();
		}
		Stack<Army> agArmies = new Stack<Army>();
		for (int i = 0; i<agressorsArmies; i++){
			if (this.from.getArmyList().size()<agressorsArmies){
				throw new NotEnoughArmiesToAttackException();
			}
			agArmies.push((Army)from.getArmyList().get(i));
		}
		attacking(agArmies);
        this.clientManager.broadcastUIUpdate(IClient.UIUpdateTypes.FIGHT);
	}
	
	/**
	 * Bestimmt die Würfel mit denen angegriffen werden soll (und setzt die Liste der Angreifer Armeen und der Angreifer Würfel)
	 * @param agressorsArmies
	 * @throws InvalidAmountOfArmiesException
	 * @throws NotEnoughArmiesToAttackException 
	 * @throws AlreadyDicedException 
	 * @throws InvalidFightException 
	 */
	public void attacking(Stack<Army> agressorsArmies) throws InvalidAmountOfArmiesException, RemoteException, NotEnoughArmiesToAttackException, AlreadyDicedException, InvalidFightException {
		this.agressorsArmies.clear();
		this.agressorsArmies.addAll(agressorsArmies);
		this.defendersDice.clear();
		if (this.from.getOwner() == this.to.getOwner()){
			throw new InvalidFightException();
		}
		if (!this.agressorsDice.isEmpty()){
			throw new AlreadyDicedException();
		}
		//Der Angreifer muss mit mindestens mit einer und höchstens mit drei Armeen angreifen 
		if(this.agressorsArmies.size() > FightConfiguration.AGGRESSOR_MAX_NUMBER_OF_ARMIES_TO_ATTACK || this.agressorsArmies.size()< FightConfiguration.NUMBER_OF_ARMIES_EXCLUDE_FROM_FIGHT){
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
	 * @throws YouCannotDefendException 
	 */
	public void defending(int defendersArmies, IPlayer clientPlayer) throws RemoteCountryNotFoundException, AggessorNotThrowDiceException, ToManyNewArmysException,NotEnoughArmiesToDefendException,NotEnoughArmysToMoveException, InvalidAmountOfArmiesException, CountriesNotConnectedException, AlreadyDicedException, TurnNotAllowedStepException, TurnNotInCorrectStepException, ArmyAlreadyMovedException, InvalidFightException, NotTheOwnerException, RemoteException, YouCannotDefendException{
		if(!this.to.getOwner().getColor().equals(clientPlayer.getColor())){
			throw new YouCannotDefendException();
		}
		if(this.agressorsDice.isEmpty()){
            throw new AggessorNotThrowDiceException();
        }
        Stack<Army> defArmies = new Stack<Army>();
		if (this.to.getArmyList().size()<defendersArmies){
			throw new NotEnoughArmiesToDefendException();
		}
		for (int i = 0; i<defendersArmies; i++){
			defArmies.push((Army)to.getArmyList().get(i));
		}
		defending(defArmies);
        this.clientManager.broadcastUIUpdate(IClient.UIUpdateTypes.FIGHT);
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
	public void defending(Stack<Army> defendersArmies)throws RemoteCountryNotFoundException, ToManyNewArmysException,InvalidAmountOfArmiesException, NotEnoughArmysToMoveException,CountriesNotConnectedException, AlreadyDicedException, TurnNotAllowedStepException, TurnNotInCorrectStepException, ArmyAlreadyMovedException, InvalidFightException, NotTheOwnerException, RemoteException{
		if (this.from.getOwner() == this.to.getOwner()){
			throw new InvalidFightException();
		}
		if (!this.defendersDice.isEmpty()){
			throw new AlreadyDicedException();
		}
		this.defendersArmies.clear();
		this.defendersArmies.addAll(defendersArmies);
		//Der Verteidiger muss mindestens mit einer und höchstens mit zwei Armeen verteidigen
		if(this.defendersArmies.size() > FightConfiguration.DEFENDER_MAX_NUMBER_OF_ARMIES_TO_DEFEND || this.defendersArmies.size()< FightConfiguration.NUMBER_OF_ARMIES_EXCLUDE_FROM_FIGHT){
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
	private int[] result() throws RemoteCountryNotFoundException, ToManyNewArmysException, CountriesNotConnectedException,NotEnoughArmysToMoveException, TurnNotAllowedStepException, TurnNotInCorrectStepException, ArmyAlreadyMovedException, NotTheOwnerException, RemoteException{
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
					this.currentTurn.moveArmyForTakeover(this.from, this.to, this.agressorsArmies);
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
	public int[] getResult() throws RemoteException{
		return this.result;
	}


	private void setResult() throws RemoteCountryNotFoundException, ToManyNewArmysException,CountriesNotConnectedException,NotEnoughArmysToMoveException, TurnNotAllowedStepException, TurnNotInCorrectStepException, ArmyAlreadyMovedException, NotTheOwnerException, RemoteException{
		this.result = result();
	}

	/**
	 * Getter für die Liste der Würfel des Angreifers
	 * @return Stack<Dice>
	 */
	public Stack<? extends IDice> getAgressorsDice() throws RemoteException{
		return this.agressorsDice;
	}
	
	/**
	 * Getter für die Liste der Würfel des Verteidigers
	 * @return Stack<Dice>
	 */
	public Stack<? extends IDice> getDefendersDice() throws RemoteException{
		return this.defendersDice;
	}
	/**
	 * Getter für das To Land
	 * @return
	 */
	public Country getTo() throws RemoteException{
		return this.to;
	}

    /**
     * Pürft, ob der Kampf in diesem Status abgebrochen werden darf
     *
     * @return
     *
     * @throws java.rmi.RemoteException
     */
    @Override
    public boolean isValidToClose() throws RemoteException {
        return false;
    }

    /**
	 * Getter für das From Land
	 * @return
	 */
	public Country getFrom() throws RemoteException{
		return this.from;
	}

    /**
     * Getter für den Angreifer
     * @return
     */
    public Player getAggressor () throws RemoteException{
        return  this.agressor;
    }

    /**
     * Getter für Verteidiger
     * @return
     */
    public Player getDefender () throws RemoteException{
        return this.getTo().getOwner();
    }

    /**
     * ToString methode, die Remote aufgerufen werden kann
     * @return
     * @throws RemoteException
     */
    public String toStringRemote() throws RemoteException{
        return "From" + this.getFrom().getName() + " to " + this.getTo().getName();
    }
}