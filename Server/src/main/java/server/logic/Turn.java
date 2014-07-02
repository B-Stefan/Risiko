package server.logic;
import interfaces.IClient;
import interfaces.IFight;
import interfaces.ITurn;
import interfaces.data.IArmy;
import interfaces.data.ICountry;
import server.ClientManager;
import server.logic.data.*;
import exceptions.*;
import server.logic.data.Map;
import server.logic.data.cards.CardDeck;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Der Turn bildet einen einzelnen Zug eines Spielers ab.
 * Dabei druchläuft ein Turn verschiende Schritte (steps) Der Turn wird durch die Runde (Round) erstellt.
 *
 */
public class Turn extends UnicastRemoteObject implements ITurn{


    /**
     * Gibt die Standardschritte zurück, die ein Turn normalerweise druchläuft
     * @return - Standard Schritte
     */
    public static Queue<steps> getDefaultSteps () {
        Queue<steps> s = new LinkedBlockingQueue<steps>(3) {
        };
        s.add(steps.DISTRIBUTE);
        s.add(steps.FIGHT);
        s.add(steps.MOVE);
        return s;
    }

    /**
     * Gibt die Schritte zurück, die alle Spieler in der ersten Runde druchlaufen müssen
     * @return - Schritte für die erste Runde
     */
    public static Queue<steps> getDefaultStepsFirstRound () {
        Queue<steps> s = new LinkedBlockingQueue<steps>(3) {
        };
        s.add(steps.DISTRIBUTE);
        s.add(steps.FIGHT);
        s.add(steps.MOVE);
        return s;
    }

    /**
     * Bildet den Spieler ab, dir diesen Turn durchführen musss
     */
    private final Player player;
    /**
     * Bildet die Karte ab auf dem der Spieler diesen Zug durchführt
     */
	private final Map map;
    /**
     * Bildet ein Stack mit neuen Armeen ab, die der Spieler auf dem Spielfeld verteilen muss
     */
	private final Stack<Army> newArmies = new Stack<Army>();

    /**
     * Bildet die Liste der Armeen ab, die in diesem Zug bereits bewegt wurden
     */
	private final ArrayList<Army> movedArmies = new ArrayList<Army>();

    /**
     * Bildet die Warteschlange der Steps ab, die der Spieler noch durchlaufen muss.
     * Dabei befindet sich der current step nicht mehr in der Liste.
     * @see #currentStep
     * @see java.util.concurrent.LinkedBlockingQueue
     */
    private final Queue<steps> allowedSteps;

    /**
     * Bildet die aktuelle Stufe des Zuges ab.
     * Diese Varriable kann nie den Wert null haben
     * @see #(Player, server.logic.data.Map, java.util.Queue)
     */
    private steps currentStep;
    
    private CardDeck deck;
    
    private boolean takeOverSucess;

    private final ClientManager clientManager;


    /**
     * Konstruktor für einen Turn
     * @param player Spieler der den Turn durchführen soll
     * @param map Karte für den Turn
     * @param steps Erlaubte Steps für diesen Turn
     * @param deck Das Card-Deck für alle Spieler
     * @throws RemoteException
     */
    public Turn(final Player player,final Map map,final  Queue<steps> steps, CardDeck deck, ClientManager clientManager) throws RemoteException{
        this.deck = deck;
    	this.player = player;
        this.map = map;
        this.clientManager = clientManager;
        //Argumentprüfung
        if(steps.isEmpty()){
            throw  new IllegalArgumentException("Sie müssten mindestens eine Queue mit einem step übergeben");
        }

        //Kopieren der übergebenen Queue, da die die Queue im Turn verändert wird und diese Veränderung keinen Auswirkungen auf andere Programmteile haben dürfen.
        LinkedBlockingQueue<steps> s = new LinkedBlockingQueue<steps>();
        s.addAll(steps);
        this.allowedSteps = s;

        //Wenn der Step "Verteilen" erlaubt ist werden die neuen Armeen, die zu verteilen sind erzeugt
        if(this.getAllowedSteps().contains(Turn.steps.DISTRIBUTE)  ){
            createNewArmies(this.determineAmountOfNewArmies());
        }

        //Aktuellen status auf den ersten Eintrag in der Queue setzten
        this.setCurrentStep(this.allowedSteps.poll());// Erstes element aus der Liste auf aktuellen status setzten

    }
    /**
     * Gibt Den Wahrheitswert heraus ob in diesem Turn bisher ein TakeOver stattgefunden hat
     * @return
     */
    public boolean getTakeOverSucess() throws RemoteException{
    	return this.takeOverSucess;
    }
    /**
     * Setzt den Wahrheitswert, ob in diesem Turn ein TakeOver stattgefunden hat
     * @param b
     */
    public void setTakeOverSucess(boolean b) throws RemoteException{
    	this.takeOverSucess = b;
    }

    /**
     *
     * @return - Aktueller Spieler, der diesen Zug durchführen muss
     */
    public Player getPlayer () throws RemoteException{
    	return this.player; 
    }

    /**
     * Gibt den Turn in einem zusammengefassten String zurück
     * @return - Zusammenfassung des Turns
     */
    public String toString(){
        String msg = "";
        try {
            msg = "Turn(" + this.getPlayer() + "):" + this.getCurrentStep();
        }catch (RemoteException e){
            throw new RuntimeException(e);
        }
        return msg;
    }

    /**
     * ToString methode, die Remote aufgerufen werden kann
     * @return
     * @throws RemoteException
     */
    public String toStringRemote() throws RemoteException{
        return this.toString();
    }

    /**
     * Berechnet die Anzahl der Armeen, die der jewilige Spieler am Anfang seines Zuges neu hinzubekommt.
     * @return Anzahl, der neuen Armeen des jeweiligen Spielers
     */
    private int determineAmountOfNewArmies()throws RemoteException{
    	int amountNewArmies = (this.player.getCountries().size())/3;
    	amountNewArmies += this.map.getBonus(this.player);
    	if (amountNewArmies<3){
    		amountNewArmies = 3;
    	}
    	return amountNewArmies;
    }

    /**
     * Füllt die Liste der neuen Armeen
     * @param numberOfArmysToCreate - Anzahl wie viele Armeen erstellt werden sollen
     */
    private void createNewArmies(int numberOfArmysToCreate)throws RemoteException{
    	for (int i = 0; i<numberOfArmysToCreate; i++){
    		this.newArmies.add(new Army(this.player));
    	}
    }


    /**
     * Fügt der Liste der bereits verschobenen Einheiten die Armee hinzu
     * @param a bewegte Armee
     */

    private void addMovedArmy(Army a){
    	this.movedArmies.add(a);
    }
    /**
     * Prüft ob die Armee bereits verschoben wurde in diesem Zug
     * @param a Armee, die Überprüft werden soll
     * @return boolean -> true wenn die Armee bereits verschoben wurde, false, wenn sie nioch nicht verschoben wurde
     */
    private boolean isArmyAlreadyMoved(IArmy a){
    	return movedArmies.contains(a);
    }


    /**
     * Diese Methode dient der Überprüfung, ob der übergebene step im Moment erlaubt wäre druchführen.
     * @param stepToCheck Der Step, der überprüft werden soll
     * @return Wenn der Step erlaubt ist True, False tritt nicht auf es werden Exceptions für False ausgelöst.
     * @throws TurnNotAllowedStepException
     * @throws TurnNotInCorrectStepException
     * @throws ToManyNewArmysException
     */
    private boolean isStepAllowed(steps stepToCheck) throws ToManyNewArmysException, TurnNotAllowedStepException, TurnNotInCorrectStepException, RemoteException{

        if(this.getCurrentStep() == stepToCheck){
            return true;
        }
        else if(allowedSteps.contains(stepToCheck)){

            if(this.getCurrentStep() == steps.DISTRIBUTE){
                if(this.getNewArmysSize() > 0 ){
                    throw new ToManyNewArmysException(this);
                }

            }else if (this.getCurrentStep() == steps.FIGHT){


            }else if (this.getCurrentStep() == steps.MOVE){

            }
            else {
                throw new TurnNotInCorrectStepException(stepToCheck,this);
            }

        }
        else{
            throw new TurnNotAllowedStepException(stepToCheck,this);
        }
        return true;
    }


    /**
     * Tauscht die Karten für den aktuellen Spieler ein
     * @throws ToManyNewArmysException
     * @throws ExchangeNotPossibleException
     * @throws TurnNotAllowedStepException
     * @throws TurnNotInCorrectStepException
     * @throws NotEnoughCardsToExchangeException
     */
    public synchronized void exchangeCards() throws ToManyNewArmysException, RemoteException, ExchangeNotPossibleException, TurnNotAllowedStepException, TurnNotInCorrectStepException, NotEnoughCardsToExchangeException{
    	if(this.isStepAllowed(steps.DISTRIBUTE)){
    		if(this.determineAmountOfNewArmies() == this.getNewArmysSize()){
    			if(this.deck.exchangeCards(this.player)){
    				this.createNewArmies(this.deck.calculateBonus());
    			}
    		}else{
    			throw new ExchangeNotPossibleException();
    		}
    	}
    }
    /**
     * Per Default der erste Step, der durchgeführt wird. Diese Methode dient dazu eine Armee auf der angegebenen Position zu plazieren.
     * @see server.logic.Turn.steps
     * @see Turn#getDefaultSteps()
     * @param position - Das Land auf welches die neue Armee plaziert werden soll
     * @param numberOfArmys - Wieviele Einheiten auf diesem Land plaziert werden sollen.
     * @throws TurnNotAllowedStepException
     * @throws TurnNotInCorrectStepException
     * @throws NotEnoughNewArmysException
     */
    public synchronized void placeNewArmy(ICountry position, int numberOfArmys) throws RemoteCountryNotFoundException, ToManyNewArmysException, TurnNotAllowedStepException, TurnNotInCorrectStepException,NotEnoughNewArmysException,NotTheOwnerException, RemoteException {
        for(int i = 0; i!= numberOfArmys; i++){
            this.placeNewArmy(position);
        }

    }
    /**
     * Per Default der erste Step, der durchgeführt wird. Diese Methode dient dazu eine Armee auf der angegebenen Position zu plazieren.
     * @see server.logic.Turn.steps
     * @see Turn#getDefaultSteps()
     * @param positionFromClient - Das Land auf welches die neue Armee plaziert werden soll
     * @throws TurnNotAllowedStepException
     * @throws TurnNotInCorrectStepException
     * @throws NotTheOwnerException
     * @throws NotEnoughNewArmysException
     */
    public void placeNewArmy(ICountry positionFromClient) throws  RemoteCountryNotFoundException,ToManyNewArmysException,TurnNotAllowedStepException, TurnNotInCorrectStepException,NotEnoughNewArmysException, NotTheOwnerException, RemoteException{
        Country position = this.map.getCountry(positionFromClient);
        if(position == null){
            throw new RemoteCountryNotFoundException();
        }
        else if (position.getOwner() != this.getPlayer())
        {
            throw  new NotTheOwnerException(this.getPlayer(), position);
        }
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
            this.clientManager.broadcastUIUpdate(IClient.UIUpdateTypes.COUNtRY);
        }
    }

    /**
     * Führt einen Kampf aus
     * @param fromClientCountry - Von diesem Land wird angegriffen
     * @param toClientCountry - Dieses land soll angegrifffen werden
     * @return Die Kampfinstanz
     * @throws TurnNotInCorrectStepException
     * @throws TurnNotAllowedStepException
     * @throws ToManyNewArmysException
     * @throws NotTheOwnerException
     * @throws RemoteException
     */
    public  IFight fight (ICountry fromClientCountry, ICountry toClientCountry) throws RemoteCountryNotFoundException, TurnNotInCorrectStepException, TurnNotAllowedStepException, ToManyNewArmysException, NotTheOwnerException, RemoteException{
        Country from = this.map.getCountry(fromClientCountry);
        Country to = this.map.getCountry(toClientCountry);

        if(from == null || to == null ){
            throw new RemoteCountryNotFoundException();
        }
        else if (from.getOwner() != this.getPlayer())
        {
            throw  new NotTheOwnerException(this.getPlayer(), from);
        }

        if(this.isStepAllowed(steps.FIGHT)){
            this.isComplete();
            //Einmal ein Land angegriffen ändert den step des Turns
            this.setCurrentStep(steps.FIGHT);
            Fight newFight = new Fight(from, to, this);
            try {
                this.clientManager.broadcastFight(newFight, newFight.getDefender());
            }catch (ClientNotFoundException e){
                //this.clientManager.broadcastMessage("Es konnt kein Client für den Spieler " );
            }
            return newFight;


        }
        throw new RuntimeException("Codeteile nicht erlaubt !");
    }


    /**
     * Bewegt eine Einheit von einem Land in ein anderes Land.
     * @param fromClientCountry Land von dem aus sich die Einheit bewegen soll
     * @param toClientCounty Zielland
     * @param numberOfArmies Anzahl der Armeen
     * @throws NotEnoughArmysToMoveException
     * @throws TurnNotAllowedStepException
     * @throws TurnNotInCorrectStepException
     * @throws CountriesNotConnectedException
     * @throws ArmyAlreadyMovedException
     * @throws NotTheOwnerException
     */
    public void moveArmy(ICountry fromClientCountry,ICountry toClientCounty, int numberOfArmies) throws RemoteCountryNotFoundException,ToManyNewArmysException, NotEnoughArmysToMoveException, TurnNotAllowedStepException, TurnNotInCorrectStepException, CountriesNotConnectedException, ArmyAlreadyMovedException,NotTheOwnerException, RemoteException {

        Country from = this.map.getCountry(fromClientCountry);
        Country to = this.map.getCountry(toClientCounty);

        if(from == null || to == null ){
            throw new RemoteCountryNotFoundException();
        }

        List<Army> armies = new ArrayList<Army>();
        Collections.copy(armies,from.getArmyList());

        //Löschen aller Armeen, die bereits bewegt wurden, somit können nur die Armen versucht werden zu bwegen, die noch nicht bewegt wurde.
        for(Army army : armies){
            if(this.movedArmies.contains(army)){
                armies.remove(army);
            }
        }

        for(int i = 0; i!= numberOfArmies; i++){
            Army army = armies.get(armies.size()-1);
            this.moveArmy(from, to, army);
        }
        this.clientManager.broadcastUIUpdate(IClient.UIUpdateTypes.COUNtRY);
    }
    /**
     * Bewegt eine Armee auf die neue Position.
     * Dise Methdoe bildet den 2. Step in einem Zug ab.
     *
     * @param from - Ausgangsland
     * @param to - Neues Land
     * @param army - Die Armee, die bewegt werden soll
     * @throws CountriesNotConnectedException
     * @throws ArmyAlreadyMovedException
     */
    public synchronized void moveArmy(Country from,Country to, Army army) throws RemoteCountryNotFoundException,ToManyNewArmysException,NotEnoughArmysToMoveException,TurnNotAllowedStepException, TurnNotInCorrectStepException, CountriesNotConnectedException, ArmyAlreadyMovedException, NotTheOwnerException, RemoteException {

        if(from == null || to == null ){
            throw new RemoteCountryNotFoundException();
        }
        else if (from.getOwner() != this.getPlayer())
        {
            throw  new NotTheOwnerException(this.getPlayer(), from);
        }
        else if (to.getOwner() != this.getPlayer()){
            throw  new NotTheOwnerException(this.getPlayer(), to);
        }

        if(this.isStepAllowed(steps.MOVE)){

            this.allowedSteps.clear(); // Alle steps löschen, da nach einmak move nichts anderes mehr erlaubt ist
            //Einmal eine Einheit bewegt, ändert den Step des Turns
            this.setCurrentStep(steps.MOVE);
            if(!from.isConnected(to)){
                throw new CountriesNotConnectedException(from,to);
            }
            else if (isArmyAlreadyMoved(army)){
                throw new ArmyAlreadyMovedException(army);
            }
            else if(from.getNumberOfArmys() == 1){
                throw new NotEnoughArmysToMoveException(from);
            }
            else {
                from.removeArmy(army);
                army.setPosition(to);
                addMovedArmy(army);
            }
        }
	}

    /**
     * Überprüft, ob der Turn abgeschlossen wurde.
     * @return True wenn der Turn abgeschlossen wurde, false wenn nicht
     * @throws ToManyNewArmysException
     */
    public  boolean isComplete() throws ToManyNewArmysException, RemoteException{

        if(this.getCurrentStep() == steps.DISTRIBUTE && this.newArmies.size() > 0) {
            throw new ToManyNewArmysException(this);
        }
        return true;
    }

    /**
     * Gibt den aktuellen Step zurück
     * @return
     */
    public  steps getCurrentStep() throws RemoteException{
        return currentStep;
    }

    /**
     * Gibt den folgenden step zurück. Ändert jedoch keine Eigenschaften des Turns
     * Dient dazu rauszufinden welcher step als nächstes dran wäre. Dabei kann null zurückgegeben werden, sobald kein nächster Step mehr da ist.
     * @return - Nächster Step der dran wäre
     */
    public  steps getNextStep () throws RemoteException{
        return this.allowedSteps.peek();
    }

    /**
     * Versetzt den Turn in die nächste Stufe.
     *
     * @throws TurnCompleteException
     * @throws ToManyNewArmysException
     */
    public void setNextStep() throws TurnCompleteException, ToManyNewArmysException, RemoteException{
        if(this.isComplete()){
            throw new TurnCompleteException();
        }
        this.currentStep = this.allowedSteps.poll();
    }

    /**
     * Setzt den currentStep
     * @param step - Step der gesetz werden soll
     */
    private void setCurrentStep(steps step) throws RemoteException{
        this.allowedSteps.remove(this.getCurrentStep());
        currentStep = step;
    }

    /**
     * Gibt die Anzahl der noch zu verteilenden Armeen zurück
     * @see #placeNewArmy(interfaces.data.ICountry)
     * @return - Anzahl der noch zu verteilenden Armeen
     */
    public int getNewArmysSize() throws RemoteException {
        return this.newArmies.size();
    }

    /**
     * Gibt die in diesem Turn erlaubten steps zurück.
     * @return - In diesem Turn erlaubte steps
     */
    public Queue<steps>  getAllowedSteps() throws RemoteException {
        return  this.allowedSteps;
    }

}
