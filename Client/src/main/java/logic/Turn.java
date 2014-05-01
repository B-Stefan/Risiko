package main.java.logic;
import main.java.logic.exceptions.ArmyAlreadyMovedException;
import main.java.logic.exceptions.CountriesNotConnectedException;
import main.java.logic.exceptions.NotEnoughNewArmysException;
import main.java.logic.exceptions.TurnNotInMoveStateException;

import java.util.*;

public class Turn {

    public  static enum steps {
        DISTRIBUTE,
        FIGHT,
        MOVE
    }

	private final Player player;
	private final Map map;
	private final Stack<Army> newArmies = new Stack<Army>();
	private final ArrayList<Army> movedArmies = new ArrayList<Army>();

    private steps currentStep = steps.DISTRIBUTE;

	public Turn(final Player p,final Map m){
		this.player = p;
        this.map = m;
		createNewArmies(this.determineAmountOfNewArmies());
	}
	
    public Player getPlayer (){
    	return this.player; 
    }
    
    public String toString(){
        return "Turn: " + this.player.toString();
    }
    
    /**
     * berechnet die Anzahl der Armeen, die der jewilige Spieler am Anfang seines Zuges neu hinzubekommt. 
     * @return Anzahl, der neuen Armeen des jeweiligen Spielers
     */
    private int determineAmountOfNewArmies(){
    	int amountNewArmies = (this.player.getCountries().size())/3;
    	amountNewArmies += this.map.getBonus(this.player);
    	if (amountNewArmies<3){
    		amountNewArmies = 3;
    	}
    	return amountNewArmies;
    }

    /**
     * erstelle eine Liste mit den neuen Armeen
     *
     */
    private void createNewArmies(int numberOfArmysToCreate){
    	for (int i = 0; i<numberOfArmysToCreate; i++){
    		this.newArmies.add(new Army(this.player));
    	}
    }

    /**
     * weist der jeweiligen Armee ein Land zu
     * @param position Das Land, zu welchem die Armee zugewiesen werden soll
     */
    public void placeNewArmy(Country position) throws CountriesNotConnectedException, NotEnoughNewArmysException{
        if(this.newArmies.size() > 0 ){
            Army a = this.newArmies.pop();
            a.setPosition(position);
        }
        else{
            throw new NotEnoughNewArmysException(this);
        }
    }
    /**
     * f�gt der Liste der bereits verschobenen Einheiten die Armee hinzu
     * @param a bewegte Armee
     */
    private void addArmyMoved(Army a){
    	this.movedArmies.add(a);
    }
    /**
     * pr�ft ob die Armee bereits verschoben wurde in diesem Zug
     * @param a Armee, die �berpr�ft werden soll
     * @return boolean -> true wenn die Armee bereits verschoben wurde, false, wenn sie nioch nicht verschoben wurde
     */
    private boolean isArmyAlreadyMoved(Army a){
    	return movedArmies.contains(a);
    }
    /**
     * �ndert die Position der Armee, falls sie noch nicht bewegt wurde und �ndert den Status der Armee in bereits bewegt
     * @param country das Zielland
     * @param army die zu bewegende Armee
     * @return true wenn die Armee bewegt wurde kann, false wenn nicht
     */
    public boolean moveArmy(Country country, Army army) throws CountriesNotConnectedException,TurnNotInMoveStateException, ArmyAlreadyMovedException {
    	if (isArmyAlreadyMoved(army) != true && this.getCurrentStep() == steps.MOVE){
    		army.setPosition(country);
    		addArmyMoved(army);
    		return true;    		
    	}
    	return false;
    }
    public steps getCurrentStep() {
        return currentStep;
    }




}
