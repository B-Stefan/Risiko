package main.java.logic;
import main.java.logic.exceptions.*;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

public class Turn {

    public  static enum steps {
        DISTRIBUTE,
        FIGHT,
        MOVE
    }
    public static Queue<steps> getDefaultSteps (){
        Queue<steps> s = new LinkedBlockingQueue<steps>(3) {
        };
        s.add(steps.DISTRIBUTE);
        s.add(steps.FIGHT);
        s.add(steps.MOVE);
        return s;
    }
    public static Queue<steps> getDefaultStepsFirstRound (){
        Queue<steps> s = new LinkedBlockingQueue<steps>(1) {
        };
        s.add(steps.DISTRIBUTE);
        return s;
    }



    private final Player player;
	private final Map map;
	private final Stack<Army> newArmies = new Stack<Army>();
	private final ArrayList<Army> movedArmies = new ArrayList<Army>();
    public  final Queue<steps> allowedSteps;

    private steps currentStep;


    public Turn(final Player p,final Map m, Queue<steps> steps){
        this.player = p;
        this.map = m;

        if(steps.isEmpty()){
            throw  new IllegalArgumentException("Sie müssten mindestens eine Queue mit einem step übergeben");
        }
        LinkedBlockingQueue<steps> s = new LinkedBlockingQueue<steps>();
        s.addAll(steps);
        this.allowedSteps = s;

        this.currentStep = this.allowedSteps.poll();// Erstes element aus der Liste auf aktuellen status setzten
        createNewArmies(this.determineAmountOfNewArmies());
    }

    public Player getPlayer (){
    	return this.player; 
    }
    
    public String toString(){
        return "Turn(" + this.getPlayer() + "):" + this.getCurrentStep();
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
     * f�gt der Liste der bereits verschobenen Einheiten die Armee hinzu
     * @param a bewegte Armee
     */
    private void addMovedArmy(Army a){
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
     *
     * @param stepToCheck
     * @return
     * @throws TurnNotAllowedStepException
     * @throws TurnNotInCorrectStepException
     */
    private boolean isStepAllowed(steps stepToCheck) throws TurnNotAllowedStepException, TurnNotInCorrectStepException{
        if(allowedSteps.contains(stepToCheck)){
            if(this.getNextStep() == stepToCheck){
                return true;
            }
            else {
                throw new TurnNotInCorrectStepException(stepToCheck,this);
            }
        }
        else if(this.getCurrentStep() == stepToCheck){
            return true;
        }
        else{
            throw new TurnNotAllowedStepException(stepToCheck,this);
        }
    }


    /**
     *
     * @param position
     * @throws TurnNotAllowedStepException
     * @throws TurnNotInCorrectStepException
     * @throws NotEnoughNewArmysException
     */
    public void placeNewArmy(Country position) throws  TurnNotAllowedStepException, TurnNotInCorrectStepException,NotEnoughNewArmysException{

        if(this.isStepAllowed(steps.DISTRIBUTE)){

            //Einmal eine neue Armee plaziert ==> Statusänderung im Turn
            this.setCurrentStep(steps.DISTRIBUTE);

            if(this.newArmies.size() == 0 ){
                throw new NotEnoughNewArmysException(this);
            }
            else {
                Army a = this.newArmies.pop();
                try {
                    a.setPosition(position);
                }catch (CountriesNotConnectedException e){
                    //Nicht möglich, dass diese Exception auftritt.
                    throw new RuntimeException(e);
                }
            }

        }
    }

    /**
     * Angreifen eines Landes mit einer definierten Anzahl von einheiten
     * @param from - Von diesem Land wird angegriffen
     * @param to - Dieses land soll angegrifffen werden
     * @param numberOfArmys - Mit dieser Anzahl wird angegriffen
     * @throws TurnNotAllowedStepException
     * @throws CountriesNotConnectedException 
     * @throws InvalidPlayerException 
     * @throws InvalidAmountOfArmiesException 
     * @throws NotEnoughArmiesToDefendException 
     * @throws NotEnoughArmiesToAttackException
     * @throws ToManyNewArmysException
     */
    public void fight (Country from, Country to, int numberOfArmys) throws TurnNotAllowedStepException, TurnNotInCorrectStepException, ToManyNewArmysException,NotEnoughArmiesToAttackException, NotEnoughArmiesToDefendException, InvalidAmountOfArmiesException, InvalidPlayerException, CountriesNotConnectedException{
        if(this.isStepAllowed(steps.FIGHT)){
            this.isComplete();
            //Einmal ein Land angegriffen ändert den step des Turns
            this.setCurrentStep(steps.FIGHT);
            List<Army> agressorsArmies = from.getArmyList().subList(0, numberOfArmys -1);
            List<Army> defendersArmies = to.getArmyList();
            Fight newFight = new Fight(from, to, agressorsArmies , defendersArmies);
            newFight.armyVsArmy();
        }
    }

    /**
     * Bewegt eine Armee auf die neue Position.
     * Dise Methdoe bildet den 2. Step in einem Zug ab.
     *
     * @param country - neue position der Armee
     * @param army - Die Armee, die bewegt werden soll
     * @throws CountriesNotConnectedException
     * @throws ArmyAlreadyMovedException
     */
    public void moveArmy(Country country, Army army) throws TurnNotAllowedStepException, TurnNotInCorrectStepException, CountriesNotConnectedException, ArmyAlreadyMovedException {

        if(this.isStepAllowed(steps.MOVE)){

            //Einmal eine Einheit bewegt, ändert den Step des Turns
            this.setCurrentStep(steps.MOVE);

            if (isArmyAlreadyMoved(army)){
                throw new ArmyAlreadyMovedException(army);

            }
            army.setPosition(country);
            addMovedArmy(army);
        }
    }
    public boolean isComplete() throws ToManyNewArmysException{
        if(this.getNextStep() == null){
            if(this.getCurrentStep() == steps.DISTRIBUTE && this.newArmies.size() > 0) {
                throw new ToManyNewArmysException(this);
            }
            return true;
        }
        else {
            return false;
        }
    }
    public steps getCurrentStep() {
        return currentStep;
    }
    public steps getNextStep (){
        return this.allowedSteps.peek();
    }
    public void setNextStep() throws TurnCompleteException, ToManyNewArmysException {
        if(this.isComplete()){
            throw new TurnCompleteException();
        }
        this.currentStep = this.allowedSteps.poll();
    }
    private void setCurrentStep(steps step) {
        currentStep = step;
    }

    public int getNewArmysSize() {
        return this.newArmies.size();
    }

    public Queue<steps>  getAllowedSteps() {
        return  this.allowedSteps;
    }

}
