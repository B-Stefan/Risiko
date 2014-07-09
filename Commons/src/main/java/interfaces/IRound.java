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

import exceptions.NotYourTurnException;
import exceptions.RoundCompleteException;
import exceptions.ToManyNewArmysException;
import exceptions.TurnNotCompleteException;
import interfaces.data.IPlayer;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;


public interface IRound  extends Remote, Serializable, IToStringRemote {
    /**
     * Setzt den nächsten Spieler als aktuellen Spieler
     * @throws RoundCompleteException
     */
    public void setCurrentPlayer() throws RoundCompleteException,RemoteException;

    /**
     * Getter für den aktuellen Spieler
     * @return currentPayler: gibt aktuellen Spieler
     */
    public IPlayer getCurrentPlayer() throws RemoteException;



    /**
     * Erzeugt und setzt den nächsten Turn, wenn erlaubt
     * @throws exceptions.ToManyNewArmysException
     * @throws exceptions.TurnNotCompleteException
     * @throws RoundCompleteException
     */
    public void setNextTurn(IPlayer clientPlayer) throws ToManyNewArmysException, NotYourTurnException, TurnNotCompleteException, RoundCompleteException,RemoteException;


    /**
     * Pürft, ob die Runde komplett abgeschlossen ist, wenn ja True
     * @return True wenn Runde abgeschlossen ist
     * @throws ToManyNewArmysException
     */
    public boolean isComplete() throws ToManyNewArmysException,RemoteException;

    /**
     * GIbt den aktuellen Turn zurück
     * @return
     */
    public ITurn getCurrentTurn() throws RemoteException;

   }
