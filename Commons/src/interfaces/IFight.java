package interfaces;

import exceptions.*;
import interfaces.data.IArmy;
import interfaces.data.utils.IDice;

import java.util.Stack;

/**
 * Created by Stefan on 29.06.14.
 */
public interface IFight {
    /**
     * Attacking überschrieben für CUI
     * @param agressorsArmies
     * @throws exceptions.NotEnoughArmiesToAttackException
     * @throws exceptions.InvalidAmountOfArmiesException
     * @throws exceptions.AlreadyDicedException
     * @throws exceptions.InvalidFightException
     */
    public void attacking(int agressorsArmies) throws NotEnoughArmiesToAttackException, InvalidAmountOfArmiesException, AlreadyDicedException, InvalidFightException;

    /**
     * Defending überschrieben für CUI
     * @param defendersArmies
     * @throws NotEnoughArmiesToAttackException
     * @throws InvalidAmountOfArmiesException
     * @throws exceptions.CountriesNotConnectedException
     * @throws AlreadyDicedException
     * @throws exceptions.ArmyAlreadyMovedException
     * @throws exceptions.TurnNotInCorrectStepException
     * @throws exceptions.TurnNotAllowedStepException
     * @throws InvalidFightException
     * @throws exceptions.AggessorNotThrowDiceException
     */
    public void defending(int defendersArmies) throws AggessorNotThrowDiceException, ToManyNewArmysException,NotEnoughArmiesToDefendException,NotEnoughArmysToMoveException, InvalidAmountOfArmiesException, CountriesNotConnectedException, AlreadyDicedException, TurnNotAllowedStepException, TurnNotInCorrectStepException, ArmyAlreadyMovedException, InvalidFightException, NotTheOwnerException;

    /**
     * Gibt das Ergebnis des Fights komprimiert zurück
     *
     *  @return Zeile 1: Anzahl der verlorenen Einheiten des Angreifers
     *          Zeile 2: Anzahl der verlorenen Einheiten des Verteidigers
     *          Zeile 3: Wenn 0 -> this.to wurde nicht erobert, wenn 1 -> this.to wurde erobert
     *
     */
    public int[] getResult();

    /**
     * Getter für die Liste der Würfel des Angreifers
     * @return Stack<Dice>
     */
    public Stack<IDice> getAgressorsDice();



    }
