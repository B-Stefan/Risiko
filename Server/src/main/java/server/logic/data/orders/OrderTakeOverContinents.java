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

import commons.interfaces.data.Orders.IOrder;
import server.logic.data.Continent;
import server.logic.data.Player;


public class OrderTakeOverContinents extends AbstractOrder implements IOrder {
	/**
	 * Erster Kontinent, der erobert werden soll
	 */
	private final Continent continentOne;
	/**
	 * zweiter Kontinent, der erobert werden soll
	 */
	private final Continent continentTwo;

	/**
	 * der zweite Constructor ist für die Erstellung einer Order, bei der nur zwei bestimmte Kontinente übernommen werden müssen
	 * Die Kontinentliste wird in diesem Fall auf null gesetzt, da es keinen dritten Kontinent zu ermitteln gibt
	 * @param continent1 erster zu übernehmender Kontinent
	 * @param continent2 zweiter zu übernehmender Kontinent
	 * @param agend Spieler, dem die Order zugewiesen ist
	 */
	public OrderTakeOverContinents(final Continent continent1, final Continent continent2, Player agend)throws RemoteException {
        super(agend);
		this.continentOne = continent1;
		this.continentTwo = continent2;
	}
	
	/**
	 * Überprüft, ob die Order erfüllt wurde (Für beide Fälle)
	 */
	@Override
	public boolean isCompleted() throws RemoteException{
		if(this.agent == this.continentOne.getCurrentOwner() && this.agent == this.continentTwo.getCurrentOwner()){
			return true;
		}
		return false;
	}


    @Override
    public String toString() {
        return this.agent + " hat die Aufgabe " + continentOne + " und " + continentTwo + " zu erobern. ";
    }
}
