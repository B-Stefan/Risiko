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

import commons.interfaces.data.Orders.IOrder;

import java.rmi.RemoteException;
import java.util.List;

import server.logic.data.Continent;
import server.logic.data.Player;

public class OrderTakeOverThreeContinents extends AbstractOrder implements IOrder{
	/**
	 * Liste alle Kontoinente zur Prüfung des dritten Kontinents
	 */
	private final List<Continent> continents;
	/**
	 * Erster Kontinent, der erobert werden soll
	 */
	private final Continent continentOne;
	/**
	 * zweiter Kontinent, der erobert werden soll
	 */
	private final Continent continentTwo;

	
	/**
	 * der erste Constructor ist für die Erstellung von Orders, bei denen nicht nur zwei Kontinente erobert werden sollen, sondern noch ein zusätzlicher 
	 * @param contigent1 erster zu übernehmender Kontinent
	 * @param contigent2 zweiter zu übernehmender Kontinent
	 * @param agend Spieler, dem die Order zugewiesen ist
	 * @param arrayList die Liste aller Kontinente
	 */
	public OrderTakeOverThreeContinents(final Continent contigent1,final Continent contigent2,final Player agend, final List<Continent> arrayList)throws RemoteException{
		super(agend);
        this.continentOne = contigent1;
		this.continentTwo = contigent2;
		this.continents = arrayList;

	}
	/**
	 * Es wird ein Wahrheitswert ermittelt, der angibt, ob der dritte (beliebige) Kontinent übernommen wurde
	 * @return True, wenn ein dritter Kontinent übernommen wurde, Flase wenn nicht
	 */
	private boolean thirdContinent()throws RemoteException{
		for(Continent c : this.continents){
			if(c != this.continentOne && c != this.continentTwo && c != null){
				if(c.getCurrentOwner() == this.agent){
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean isCompleted()throws RemoteException {
		if(thirdContinent() && this.agent == this.continentOne.getCurrentOwner() && this.agent == this.continentTwo.getCurrentOwner()){
			return true;
		}
		return false;
	}
	@Override
	public String toString(){
		return this.agent + " hat die Aufgabe " + this.continentOne + " und " + this.continentTwo + " und einen weiteren Kontinent zu erobern.";
	}
	
}
