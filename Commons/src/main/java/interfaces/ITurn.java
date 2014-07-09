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
 * Beschreibt das Interface für einen einzelnen Spielzug eines Spielers mit den verscheiedenen stufen
 *
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
     * @param clientPlayer Der Spieler, der die Funktion ausgelöst hat
     * @throws RemoteCountryNotFoundException
     * @throws NotYourTurnException
     * @throws ToManyNewArmysException
     * @throws TurnNotAllowedStepException
     * @throws TurnNotInCorrectStepException
     * @throws NotEnoughNewArmysException
     * @throws NotTheOwnerException
     * @throws RemoteException
     */
    public void placeNewArmy(ICountry position, IPlayer clientPlayer) throws  RemoteCountryNotFoundException, NotYourTurnException, ToManyNewArmysException,TurnNotAllowedStepException, TurnNotInCorrectStepException,NotEnoughNewArmysException, NotTheOwnerException, RemoteException;

    /**
     * Angreifen eines Landes mit einer definierten Anzahl von einheiten
     * @param from - Von diesem Land wird angegriffen
     * @param to - Dieses land soll angegrifffen werd
     * @param clientPlayer Der Spieler, der die Funktion ausgelöst hat
     * @return den fight zwischen den beiden ländern
     * @throws RemoteCountryNotFoundException
     * @throws NotYourTurnException
     * @throws TurnNotInCorrectStepException
     * @throws TurnNotAllowedStepException
     * @throws ToManyNewArmysException
     * @throws NotTheOwnerException
     * @throws RemoteException
     */
    public IFight fight (ICountry from, ICountry to, IPlayer clientPlayer) throws RemoteCountryNotFoundException,NotYourTurnException,TurnNotInCorrectStepException, TurnNotAllowedStepException, ToManyNewArmysException, NotTheOwnerException, RemoteException;


    /**
     *
     * Bewegt eine Einheit von einem Land in ein anderes Land.
     * @param from Land von dem aus sich die Einheit bewegen soll
     * @param to Zielland
     * @param numberOfArmies Anzahl der Armeen
     * @param clientPlayer Der Spieler, der die Funktion ausgelöst hat
     * @throws RemoteCountryNotFoundException
     * @throws NotYourTurnException
     * @throws ToManyNewArmysException
     * @throws NotEnoughArmysToMoveException
     * @throws TurnNotAllowedStepException
     * @throws TurnNotInCorrectStepException
     * @throws CountriesNotConnectedException
     * @throws ArmyAlreadyMovedException
     * @throws NotTheOwnerException
     * @throws RemoteException
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
     *
     * @param clientPlayer Der Spieler, der die Funktion ausgelöst hat
     * @throws TurnCompleteException
     * @throws NotYourTurnException
     * @throws ToManyNewArmysException
     * @throws RemoteException
     */
    public void setNextStep(IPlayer clientPlayer) throws TurnCompleteException,NotYourTurnException, ToManyNewArmysException, RemoteException ;

    /**
     * Gibt die Anzahl der noch zu verteilenden Armeen zurück
     * @return - Anzahl der noch zu verteilenden Armeen
     *
     */
    public int getNewArmysSize() throws RemoteException;


    /**
     * Gibt die in diesem Turn erlaubten steps zurück.
     * @return - In diesem Turn erlaubte steps
     */
    public Queue<steps>  getAllowedSteps() throws RemoteException;

}
