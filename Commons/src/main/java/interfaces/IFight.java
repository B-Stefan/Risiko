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

package interfaces;

import exceptions.*;
import interfaces.data.IArmy;
import interfaces.data.ICountry;
import interfaces.data.IPlayer;
import interfaces.data.utils.IDice;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Stack;

/**
 * Created by Stefan on 29.06.14.
 */
public interface IFight extends Remote, Serializable,IToStringRemote {
    /**
     * Attacking überschrieben für CUI
     * @param agressorsArmies
     * @throws exceptions.NotEnoughArmiesToAttackException
     * @throws exceptions.InvalidAmountOfArmiesException
     * @throws exceptions.AlreadyDicedException
     * @throws exceptions.InvalidFightException
     */
    public void attacking(int agressorsArmies, IPlayer clientPlayer) throws NotEnoughArmiesToAttackException, YouCannotAttackException, InvalidAmountOfArmiesException, AlreadyDicedException, InvalidFightException, RemoteException;

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
     * @throws CountryNotInListException 
     */
    public void defending(int defendersArmies, IPlayer clientPlayer) throws RemoteCountryNotFoundException, YouCannotDefendException, AggessorNotThrowDiceException, ToManyNewArmysException,NotEnoughArmiesToDefendException,NotEnoughArmysToMoveException, InvalidAmountOfArmiesException, CountriesNotConnectedException, AlreadyDicedException, TurnNotAllowedStepException, TurnNotInCorrectStepException, ArmyAlreadyMovedException, RemoteException, InvalidFightException, NotTheOwnerException, CountryNotInListException;

    /**
     * Bewegt die Armeen nach einem Kampf
     * @param number Anzahl der Armeen die bewegt werden sollen
     * @throws FightMoveMinimumOneArmy
     * @throws FightNotWonException
     * @throws RemoteException
     * @throws NotTheOwnerException
     * @throws RemoteCountryNotFoundException
     * @throws ToManyNewArmysException
     * @throws TurnNotAllowedStepException
     * @throws TurnNotInCorrectStepException
     * @throws NotEnoughArmysToMoveException
     * @throws CountriesNotConnectedException
     */
    public void moveArmiesAfterTakeover(int number) throws FightMoveMinimumOneArmy,ArmyAlreadyMovedException, FightNotWonException, RemoteException, NotTheOwnerException, RemoteCountryNotFoundException, ToManyNewArmysException, TurnNotAllowedStepException, TurnNotInCorrectStepException, NotEnoughArmysToMoveException, CountriesNotConnectedException;
    /**
     * Gibt das Ergebnis des Fights komprimiert zurück
     *
     *  @return Zeile 1: Anzahl der verlorenen Einheiten des Angreifers
     *          Zeile 2: Anzahl der verlorenen Einheiten des Verteidigers
     *          Zeile 3: Wenn 0 -> this.to wurde nicht erobert, wenn 1 -> this.to wurde erobert
     *
     */
    public int[] getResult() throws RemoteException;

    /**
     * Getter für die Liste der Würfel des Angreifers
     * @return Stack<Dice>
     */
    public Stack<? extends IDice> getAgressorsDice() throws RemoteException;



    /**
     * Getter für die Liste der Würfel des Verteidigers
     * @return Stack<Dice>
     */
    public Stack<? extends IDice> getDefendersDice() throws RemoteException;

    /**
     * Getter für den Angreifer
     * @return
     */
    public IPlayer getAggressor () throws RemoteException;

    /**
     * Getter für Verteidiger
     * @return
     */
    public IPlayer getDefender () throws RemoteException;

    /**
     * Getter für das From Land
     * @return
     */
    public ICountry getFrom() throws RemoteException;

    /**
     * Getter für das To Land
     * @return
     */
    public ICountry getTo() throws RemoteException;


    /**
     * Wird vom Client aufgerufen, wenn er den Kampf verlässt
     * @param player - Speiler den den Kmapf verlaasen möchte
     * @return
     * @throws RemoteException
     * @throws AlreadyDicedException
     * @throws CanNotCloseFightException
     */
    public void leafFight(IPlayer player) throws RemoteException, AlreadyDicedException,CanNotCloseFightException;

}
