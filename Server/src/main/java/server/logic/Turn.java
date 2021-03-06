/*
 * RISIKO-JAVA - Game, Copyright 2014  Jennifer Theloy, Stefan Bieliauskas  -  All Rights Reserved.
 * Hochschule Bremen - University of Applied Sciences
 *
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 * Contact:
 *     Jennifer Theloy: jTheloy@stud.hs-bremen.de
 *     Stefan Bieliauskas: sBieliauskas@stud.hs-bremen.de
 *
 * Web:
 *     https://github.com/B-Stefan/Risiko
 *
 */

/*
 * RISIKO-JAVA - Game, Copyright 2014  Jennifer Theloy, Stefan Bieliauskas  -  All Rights Reserved.
 * Hochschule Bremen - University of Applied Sciences
 *
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 * Contact:
 *     Jennifer Theloy: jTheloy@stud.hs-bremen.de
 *     Stefan Bieliauskas: sBieliauskas@stud.hs-bremen.de
 *
 * Web:
 *     https://github.com/B-Stefan/Risiko
 *
 */

package server.logic;
import commons.interfaces.IClient;
import commons.interfaces.IFight;
import commons.interfaces.ITurn;
import commons.interfaces.data.ICountry;
import commons.interfaces.data.IPlayer;
import server.ClientManager;
import server.logic.data.*;
import commons.exceptions.*;
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
    /**
     * Der Kartenstapel an unvergebenen Karten
     */
    private final CardDeck deck;
    /**
     * Gibt an, ob in dem Zug ein ein erfolgreicher Twkeover stattgefunden hat
     */
    private boolean takeOverSucess;
    /**
     * Clientmanager
     */
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
    private boolean isArmyAlreadyMoved(Army a){
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
                //Weitere Bedingungen die für das erlauben des Fight nötig sind

            }else if (this.getCurrentStep() == steps.MOVE){
                //Weitere Bedingungen die für das erlauben des Move nötig sind
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
    public synchronized void exchangeCards(IPlayer clientPlayer) throws ToManyNewArmysException, RemoteException, ExchangeNotPossibleException, TurnNotAllowedStepException, TurnNotInCorrectStepException, NotEnoughCardsToExchangeException{
    	if(this.isStepAllowed(steps.DISTRIBUTE)){
    		if(this.determineAmountOfNewArmies() == this.getNewArmysSize()){
    			if(this.deck.exchangeCards(clientPlayer)){
    				this.createNewArmies(this.deck.calculateBonus());
    			}
    		}else{
    			throw new ExchangeNotPossibleException();
    		}
    	}
    }
    /**
     * Per Default der erste Step, der durchgeführt wird. Diese Methode dient dazu eine Armee auf der angegebenen Position zu plazieren.
     * @see commons.interfaces.ITurn.steps
     * @see Turn#getDefaultSteps()
     * @param position - Das Land auf welches die neue Armee plaziert werden soll
     * @param numberOfArmies - Wieviele Einheiten auf diesem Land plaziert werden sollen.
     * @throws TurnNotAllowedStepException
     * @throws TurnNotInCorrectStepException
     * @throws NotEnoughNewArmysException
     * @throws NotYourTurnException 
     */
    public synchronized void placeNewArmy(ICountry position, int numberOfArmies, IPlayer clientPlayer) throws RemoteCountryNotFoundException, ToManyNewArmysException, TurnNotAllowedStepException, TurnNotInCorrectStepException,NotEnoughNewArmysException,NotTheOwnerException, RemoteException, NotYourTurnException {

        if(!this.player.equals(clientPlayer)){
        	throw new NotYourTurnException();
        }
    	for(int i = 0; i!= numberOfArmies; i++){
            this.placeNewArmy(position, clientPlayer);
        }

    }
    /**
     * Per Default der erste Step, der durchgeführt wird. Diese Methode dient dazu eine Armee auf der angegebenen Position zu plazieren.
     * @see commons.interfaces.ITurn.steps
     * @see Turn#getDefaultSteps()
     * @param positionFromClient - Das Land auf welches die neue Armee plaziert werden soll
     * @throws TurnNotAllowedStepException
     * @throws TurnNotInCorrectStepException
     * @throws NotTheOwnerException
     * @throws NotEnoughNewArmysException
     * @throws NotYourTurnException 
     */
    public void placeNewArmy(ICountry positionFromClient, IPlayer clientPlayer) throws  RemoteCountryNotFoundException,ToManyNewArmysException,TurnNotAllowedStepException, TurnNotInCorrectStepException,NotEnoughNewArmysException, NotTheOwnerException, RemoteException, NotYourTurnException{
        if(!this.player.equals(clientPlayer)){
        	throw new NotYourTurnException();
        }
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
     * @throws NotYourTurnException 
     */
    public  IFight fight (ICountry fromClientCountry, ICountry toClientCountry, IPlayer clientPlayer) throws RemoteCountryNotFoundException, TurnNotInCorrectStepException, TurnNotAllowedStepException, ToManyNewArmysException, NotTheOwnerException, RemoteException, NotYourTurnException{
        if(!this.player.equals(clientPlayer)){
        	throw new NotYourTurnException();
        }
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
            Fight newFight = new Fight(from, to, this,clientManager);

            this.clientManager.broadcastFight(newFight);
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
     * @throws NotYourTurnException 
     * @throws NotEoughUnmovedArmiesException 
     */
    public synchronized void moveArmy(ICountry fromClientCountry,ICountry toClientCounty, int numberOfArmies, IPlayer clientPlayer) throws RemoteCountryNotFoundException,ToManyNewArmysException, NotEnoughArmysToMoveException, TurnNotAllowedStepException, TurnNotInCorrectStepException, CountriesNotConnectedException, ArmyAlreadyMovedException,NotTheOwnerException, RemoteException, NotYourTurnException, NotEoughUnmovedArmiesException {
        if(!this.player.equals(clientPlayer)){
        	throw new NotYourTurnException();
        }
        Country from = this.map.getCountry(fromClientCountry);
        Country to = this.map.getCountry(toClientCounty);

        if(from == null || to == null ){
            throw new RemoteCountryNotFoundException();
        }

        List<Army> armies = this.getNotMovedArmies(from.getArmyList());

        if(numberOfArmies>armies.size()){
        	throw new NotEoughUnmovedArmiesException(armies.size());
        }

	    for(int i = 0; i!= numberOfArmies; i++){
	        Army army = armies.get(armies.size()-1);
	        armies.remove(army);
	        this.moveArmy(from, to, army,true);
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
     * @param addArmyFromTakeover Besagt, ob diese Armee beim verschieben auch als verschoben gelten soll, dient zum bewegen nach dem Fight
     * @throws CountriesNotConnectedException
     * @throws ArmyAlreadyMovedException
     */
    public synchronized void moveArmy(Country from,Country to, Army army, boolean addArmyFromTakeover) throws RemoteCountryNotFoundException,ToManyNewArmysException,NotEnoughArmysToMoveException,TurnNotAllowedStepException, TurnNotInCorrectStepException, CountriesNotConnectedException, ArmyAlreadyMovedException, NotTheOwnerException, RemoteException {

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

            /**
             * Wenn vom Fight eine Armee bwegt wird soll der Step nicht geändert werden
             */
            if(addArmyFromTakeover){
                this.allowedSteps.clear(); // Alle steps löschen, da nach einmak move nichts anderes mehr erlaubt ist
                //Einmal eine Einheit bewegt, ändert den Step des Turns
                this.setCurrentStep(steps.MOVE);
            }

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
                //Bewegen der Armee
                from.removeArmy(army);
                army.setPosition(to);
                if(addArmyFromTakeover){
                    addMovedArmy(army);
                }
            }
        }
	}

    /**
     * Gibt eine Liste der Armeen zurück, die auf dem Land noch nicht bewegt wurden, Differenzmenge von this.movedArmies && @param allArmies
     * @param allArmies Liste aller Armeen die gerüft werden soll
     * @return
     * @throws RemoteException
     */
    private List<Army> getNotMovedArmies(List<Army> allArmies) throws RemoteException{
        List<Army> armies = new ArrayList<Army>();
        for(Army a : allArmies){
            armies.add(a);
        }

        //Löschen aller Armeen, die bereits bewegt wurden, somit können nur die Armen versucht werden zu bwegen, die noch nicht bewegt wurde.
        for(Army army : armies){
            if(this.movedArmies.contains(army)){
                armies.remove(army);
            }
        }
        return  armies;
    }
    /**
     * Versetzt die übergebenen Armeen von dem Country from zu dem Country to
     * Die Methode wird nur für den Step FIGHT verwendet, um einen Takeover zu vollziehen
     * @param from
     * @param to
     * @param numberOfArmies
     * @throws RemoteException
     * @throws NotTheOwnerException
     * @throws RemoteCountryNotFoundException
     * @throws ToManyNewArmysException
     * @throws TurnNotAllowedStepException
     * @throws TurnNotInCorrectStepException
     * @throws NotEnoughArmysToMoveException
     * @throws CountriesNotConnectedException
     * @throws ArmyAlreadyMovedException
     */
    public synchronized void moveArmyForTakeover(Country from, Country to, int numberOfArmies) throws RemoteException,ArmyAlreadyMovedException, NotTheOwnerException, RemoteCountryNotFoundException, ToManyNewArmysException, TurnNotAllowedStepException, TurnNotInCorrectStepException, NotEnoughArmysToMoveException, CountriesNotConnectedException{

         if (from.getOwner() != this.getPlayer())
         {
             throw  new NotTheOwnerException(this.getPlayer(), from);
         }
         else if (to.getOwner() != this.getPlayer()){
             throw  new NotTheOwnerException(this.getPlayer(), to);
         }else if (!from.isConnected(to)){
             throw new CountriesNotConnectedException(from,to);
         }

         if(this.isStepAllowed(steps.FIGHT)){
             List<Army> armies = this.getNotMovedArmies(from.getArmyList());

             //Durchgehen bis Anzahl erreicht
             for(int i = 0; i!= numberOfArmies; i++){
                 Army army = armies.get(armies.size()-1); // Minus eins da einer noch auf dem Land bleiben muss
                 armies.remove(army);
                 this.moveArmy(from,to,army,false);
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
     * @throws NotYourTurnException 
     */
    public void setNextStep(IPlayer clientPlayer) throws TurnCompleteException, ToManyNewArmysException, RemoteException, NotYourTurnException{
        if(!this.player.equals(clientPlayer)){
        	throw new NotYourTurnException();
        }
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
     * @see #placeNewArmy(commons.interfaces.data.ICountry, int, commons.interfaces.data.IPlayer)
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
