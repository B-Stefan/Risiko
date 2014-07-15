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

package server.logic.data.orders;

import java.rmi.RemoteException;

import commons.interfaces.data.IPlayer;
import commons.interfaces.data.Orders.IOrder;
import server.logic.data.Player;

/**
 * Created by Stefan on 31.03.2014.
 * @author Stefan Bieliauskas
 * Diese Klasse ist fÃ¼r die Erfassung der AuftrÃ¤ge ausgelegt,
 * die darauf abzielen einen anderen Spieler zu vernichten.
 *
 *
 * @see IOrder
 */
public class OrderTerminatePlayer extends AbstractOrder implements IOrder{
	/**
	 * Der Spieler, der von dem Spieler, dem die Order zugeiwesen ist, vernichtet werden soll
	 */
	private final Player victim;
	
	/**
	 * Zuerst wird der Agent gesetzt, damit ausgeschlossen werden kann, dass das Victim und der Player identisch sind
	 * @param playerToTerminate Opfer des Auftrags
	 * @param agend Der Spieler, dem die Order zugewiesen werden soll
	 */
	 public OrderTerminatePlayer(final Player playerToTerminate, final Player agend) throws RemoteException {
         super(agend);
         this.victim = playerToTerminate;
	 }

	
    @Override
    public boolean isCompleted()throws RemoteException {
    	if (this.victim.getCountries().isEmpty()) {
            return true;
        } else {
        	return false;
        }
    }
    
    /**
     * Gibt den zu vernichtenden Spieler wieder
     * @return Player
     */
    
	public IPlayer getVictim() {
		return victim;
	}


    @Override
    public String toString(){


        return this.agent + " hat die Aufgabe den Spieler " + this.getVictim() + " zu vernichten"  ;
    }
}
