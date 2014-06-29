package interfaces;

import exceptions.*;
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
    public boolean getTakeOverSucess();
    public void setTakeOverSucess(boolean b);
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


    }
