package interfaces;

import exceptions.*;
import interfaces.data.IArmy;
import interfaces.data.IPlayer;

import interfaces.data.ICountry;
import interfaces.data.cards.ICardDeck;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Stefan on 29.06.14.
 */
public interface ITurn {

    /**
     * Die Steps bilden die möglichen Schritte eines Turns ab
     */
    public  static enum steps{
        DISTRIBUTE,
        FIGHT,
        MOVE
    }
    /**
     * Gibt die Standardschritte zurück, die ein Turn normalerweise druchläuft
     * @return - Standard Schritte
     */
    public static Queue<steps> getDefaultSteps (){
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
    public static Queue<steps> getDefaultStepsFirstRound (){
        Queue<steps> s = new LinkedBlockingQueue<steps>(3) {
        };
        s.add(steps.DISTRIBUTE);
        s.add(steps.FIGHT);
        s.add(steps.MOVE);
        return s;
    }
    /**
     * Gibt Den Wahrheitswert heraus ob in diesem Turn bisher ein TakeOver stattgefunden hat
     * @return
     */
    public boolean getTakeOverSucess();
    /**
     * Setzt den Wahrheitswert, ob in diesem Turn ein TakeOver stattgefunden hat
     * @param b
     */
    public void setTakeOverSucess(boolean b);

    /**
     *
     * @return - Aktueller Spieler, der diesen Zug durchführen muss
     */
    public IPlayer getPlayer ();
    /**
     * Per Default der erste Step, der durchgeführt wird. Diese Methode dient dazu eine Armee auf der angegebenen Position zu plazieren.
     * @see logic.Turn.steps
     * @see Turn#getDefaultSteps()
     * @param position - Das Land auf welches die neue Armee plaziert werden soll
     * @param numberOfArmys - Wieviele Einheiten auf diesem Land plaziert werden sollen.
     * @throws exceptions.TurnNotAllowedStepException
     * @throws exceptions.TurnNotInCorrectStepException
     * @throws exceptions.NotEnoughNewArmysException
     */

    /**
     * Tauscht die Karten für den aktuellen Spieler ein
     * @throws ToManyNewArmysException
     * @throws ExchangeNotPossibleException
     * @throws TurnNotAllowedStepException
     * @throws TurnNotInCorrectStepException
     * @throws NotEnoughCardsToExchangeException
     */
    public void exchangeCards() throws ToManyNewArmysException, ExchangeNotPossibleException, TurnNotAllowedStepException, TurnNotInCorrectStepException, NotEnoughCardsToExchangeException;

    /**
     * Per Default der erste Step, der durchgeführt wird. Diese Methode dient dazu eine Armee auf der angegebenen Position zu plazieren.
     * @see interfaces.ITurn.steps
     * @see #getDefaultSteps()
     * @param position - Das Land auf welches die neue Armee plaziert werden soll
     * @param numberOfArmys - Wieviele Einheiten auf diesem Land plaziert werden sollen.
     * @throws TurnNotAllowedStepException
     * @throws TurnNotInCorrectStepException
     * @throws NotEnoughNewArmysException
     */
    public void placeNewArmy(ICountry position, int numberOfArmys) throws ToManyNewArmysException, TurnNotAllowedStepException, TurnNotInCorrectStepException, NotEnoughNewArmysException, NotTheOwnerException ;

    /**
     * Per Default der erste Step, der durchgeführt wird. Diese Methode dient dazu eine Armee auf der angegebenen Position zu plazieren.
     * @see logic.Turn.steps
     * @see Turn#getDefaultSteps()
     * @param position - Das Land auf welches die neue Armee plaziert werden soll
     * @throws TurnNotAllowedStepException
     * @throws TurnNotInCorrectStepException
     * @throws NotTheOwnerException
     * @throws NotEnoughNewArmysException
     */
    public void placeNewArmy(ICountry position) throws  ToManyNewArmysException,TurnNotAllowedStepException, TurnNotInCorrectStepException,NotEnoughNewArmysException, NotTheOwnerException;


    /**
     * Angreifen eines Landes mit einer definierten Anzahl von einheiten
     * @param from - Von diesem Land wird angegriffen
     * @param to - Dieses land soll angegrifffen werden
     * @throws TurnNotAllowedStepException
     * @throws CountriesNotConnectedException
     * @throws InvalidPlayerException
     * @throws InvalidAmountOfArmiesException
     * @throws NotEnoughArmiesToDefendException
     * @throws NotEnoughArmiesToAttackException
     * @throws NotTheOwnerException
     * @throws ToManyNewArmysException
     */
    public IFight fight (ICountry from, ICountry to) throws TurnNotInCorrectStepException, TurnNotAllowedStepException, ToManyNewArmysException, NotTheOwnerException;

    /**
     * Bewegt eine Einheit von einem Land in ein anderes Land.
     * @param from Land von dem aus sich die Einheit bewegen soll
     * @param to Zielland
     * @param numberOfArmies Anzahl der Armeen
     * @throws NotEnoughArmysToMoveException
     * @throws TurnNotAllowedStepException
     * @throws TurnNotInCorrectStepException
     * @throws CountriesNotConnectedException
     * @throws ArmyAlreadyMovedException
     * @throws NotTheOwnerException
     */
    public void moveArmy(ICountry from,ICountry to, int numberOfArmies) throws ToManyNewArmysException, NotEnoughArmysToMoveException, TurnNotAllowedStepException, TurnNotInCorrectStepException, CountriesNotConnectedException, ArmyAlreadyMovedException,NotTheOwnerException;
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
    public void moveArmy(ICountry from,ICountry to, IArmy army) throws ToManyNewArmysException,NotEnoughArmysToMoveException,TurnNotAllowedStepException, TurnNotInCorrectStepException, CountriesNotConnectedException, ArmyAlreadyMovedException, NotTheOwnerException;

    /**
     * Überprüft, ob der Turn abgeschlossen wurde.
     * @return True wenn der Turn abgeschlossen wurde, false wenn nicht
     * @throws ToManyNewArmysException
     */
    public boolean isComplete() throws ToManyNewArmysException;
    /**
     * Gibt den aktuellen Step zurück
     * @return
     */
    public steps getCurrentStep();
    /**
     * Gibt den folgenden step zurück. Ändert jedoch keine Eigenschaften des Turns
     * Dient dazu rauszufinden welcher step als nächstes dran wäre. Dabei kann null zurückgegeben werden, sobald kein nächster Step mehr da ist.
     * @return - Nächster Step der dran wäre
     */

    public steps getNextStep ();
    /**
     * Versetzt den Turn in die nächste Stufe.
     *
     * @throws TurnCompleteException
     * @throws ToManyNewArmysException
     */

    public void setNextStep() throws TurnCompleteException, ToManyNewArmysException ;

    /**
     * Gibt die Anzahl der noch zu verteilenden Armeen zurück
     * @see #placeNewArmy(Country)
     * @return - Anzahl der noch zu verteilenden Armeen
     */
    public int getNewArmysSize();


    /**
     * Gibt die in diesem Turn erlaubten steps zurück.
     * @return - In diesem Turn erlaubte steps
     */
    public Queue<steps>  getAllowedSteps();

}
