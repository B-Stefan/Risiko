package interfaces;

import exceptions.*;
import interfaces.data.IArmy;
import interfaces.data.IPlayer;

import interfaces.data.ICountry;
import interfaces.data.cards.ICardDeck;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Stefan on 29.06.14.
 */
public interface ITurn extends Remote, Serializable, IToStringRemote {

    /**
     * Die Steps bilden die möglichen Schritte eines Turns ab
     */
    public  static enum steps{
        DISTRIBUTE,
        FIGHT,
        MOVE
    }
    /**
     * Gibt Den Wahrheitswert heraus ob in diesem Turn bisher ein TakeOver stattgefunden hat
     * @return
     */
    public boolean getTakeOverSucess() throws RemoteException;
    /**
     * Setzt den Wahrheitswert, ob in diesem Turn ein TakeOver stattgefunden hat
     * @param b
     */
    public void setTakeOverSucess(boolean b)  throws RemoteException;

    /**
     *
     * @return - Aktueller Spieler, der diesen Zug durchführen muss
     */
    public IPlayer getPlayer () throws RemoteException;


    /**
     * Tauscht die Karten für den aktuellen Spieler ein
     * @throws ToManyNewArmysException
     * @throws ExchangeNotPossibleException
     * @throws TurnNotAllowedStepException
     * @throws TurnNotInCorrectStepException
     * @throws NotEnoughCardsToExchangeException
     */
    public void exchangeCards() throws ToManyNewArmysException, RemoteException, ExchangeNotPossibleException, TurnNotAllowedStepException, TurnNotInCorrectStepException, NotEnoughCardsToExchangeException;

    /**
     * Per Default der erste Step, der durchgeführt wird. Diese Methode dient dazu eine Armee auf der angegebenen Position zu plazieren.
     * @see interfaces.ITurn.steps
     * @param position - Das Land auf welches die neue Armee plaziert werden soll
     * @param numberOfArmys - Wieviele Einheiten auf diesem Land plaziert werden sollen.
     * @throws TurnNotAllowedStepException
     * @throws TurnNotInCorrectStepException
     * @throws NotEnoughNewArmysException
     */
    public void placeNewArmy(ICountry position, int numberOfArmys, IPlayer clientPlayer) throws RemoteCountryNotFoundException, NotYourTurnException, ToManyNewArmysException, TurnNotAllowedStepException, TurnNotInCorrectStepException, NotEnoughNewArmysException, NotTheOwnerException, RemoteException ;

    /**
     * Per Default der erste Step, der durchgeführt wird. Diese Methode dient dazu eine Armee auf der angegebenen Position zu plazieren.
     * @param position - Das Land auf welches die neue Armee plaziert werden soll
     * @throws TurnNotAllowedStepException
     * @throws TurnNotInCorrectStepException
     * @throws NotTheOwnerException
     * @throws NotEnoughNewArmysException
     */
    public void placeNewArmy(ICountry position, IPlayer clientPlayer) throws  RemoteCountryNotFoundException, NotYourTurnException, ToManyNewArmysException,TurnNotAllowedStepException, TurnNotInCorrectStepException,NotEnoughNewArmysException, NotTheOwnerException, RemoteException;


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
    public IFight fight (ICountry from, ICountry to, IPlayer clientPlayer) throws RemoteCountryNotFoundException,NotYourTurnException,TurnNotInCorrectStepException, TurnNotAllowedStepException, ToManyNewArmysException, NotTheOwnerException, RemoteException;

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
    public void moveArmy(ICountry from,ICountry to, int numberOfArmies, IPlayer clientPlayer) throws RemoteCountryNotFoundException,NotYourTurnException,ToManyNewArmysException, NotEnoughArmysToMoveException, TurnNotAllowedStepException, TurnNotInCorrectStepException, CountriesNotConnectedException, ArmyAlreadyMovedException,NotTheOwnerException, RemoteException;

    /**
     * Überprüft, ob der Turn abgeschlossen wurde.
     * @return True wenn der Turn abgeschlossen wurde, false wenn nicht
     * @throws ToManyNewArmysException
     */
    public boolean isComplete() throws ToManyNewArmysException, RemoteException;
    /**
     * Gibt den aktuellen Step zurück
     * @return
     */
    public steps getCurrentStep() throws RemoteException;
    /**
     * Gibt den folgenden step zurück. Ändert jedoch keine Eigenschaften des Turns
     * Dient dazu rauszufinden welcher step als nächstes dran wäre. Dabei kann null zurückgegeben werden, sobald kein nächster Step mehr da ist.
     * @return - Nächster Step der dran wäre
     */

    public steps getNextStep () throws RemoteException;
    /**
     * Versetzt den Turn in die nächste Stufe.
     *
     * @throws TurnCompleteException
     * @throws ToManyNewArmysException
     */

    public void setNextStep(IPlayer clientPlayer) throws TurnCompleteException,NotYourTurnException, ToManyNewArmysException, RemoteException ;

    /**
     * Gibt die Anzahl der noch zu verteilenden Armeen zurück
     * @return - Anzahl der noch zu verteilenden Armeen
     */
    public int getNewArmysSize() throws RemoteException;


    /**
     * Gibt die in diesem Turn erlaubten steps zurück.
     * @return - In diesem Turn erlaubte steps
     */
    public Queue<steps>  getAllowedSteps() throws RemoteException;

}
