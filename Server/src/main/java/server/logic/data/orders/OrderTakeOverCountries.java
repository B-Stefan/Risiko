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
import server.logic.data.Country;
import server.logic.data.Player;

public class OrderTakeOverCountries extends AbstractOrder implements IOrder {

    /**
     * Anzahl der Länder die besetzt werden sollen
     */
    private static final int NUMBER_OF_COUNTRIES_NORMAL = 24;

    /**
     * Anzahl der Länder die besetzt werden sollen, wenn jeweils 2 auf einem Land sein sollen
     */
    private static final int NUMBER_OF_COUNTRIES_TWO_ARMIES = 24;


    /**
     * gibt an, ob zwei Armeen auf dem Land stehen sollen (und somit nur 18 Länder erobert werden müssen) oder nicht
     */
    private final boolean withTwoArmies;

    /**
     * Anzahl der Länder die erobert werden sollen
     */
    private final int numberOfCounties;

    /**
     * @param twoArmies True bedeutet, dass es sich bei dem Auftrag um den Fall handelt, dass zwei Armeen auf den
     *                  Ländern stehen müssen. Bei False müssen nur eine bestimmte Anzahl an Ländern eingenommen werden
     * @param agend     Der Spieler, dem die Order zugewiesen wird
     */
    public OrderTakeOverCountries(boolean twoArmies, Player agend) throws RemoteException{
        super(agend);
        this.withTwoArmies = twoArmies;
        if(this.withTwoArmies){
            this.numberOfCounties = NUMBER_OF_COUNTRIES_TWO_ARMIES;
        }
        else {
            this.numberOfCounties = NUMBER_OF_COUNTRIES_NORMAL;
        }

    }

    /**
     * Testet, ob die Aufgabe erfüllt ist Für den Fall, dass zwei Armeen auf dem Land stehen müssen, prüft er, ob
     * mindestens 18 Länder erobert sind und ob auf jedem auch wirklich zwei Armeen stehen Für den anderen Fall
     * überprüft er nur, ob 24 Länder übernommen wurden
     */
    @Override
    public boolean isCompleted() throws RemoteException{
        if (this.agent.getCountries().size() >= this.numberOfCounties) {
            if (withTwoArmies) {
                for (Country c : this.agent.getCountriesReal()) {
                    if (c.getArmySize() < 2) {
                        return false;

                    }
                }
            }
            return true;
        }else {
            return false;
        }
    }
    @Override
    public String toString() {
        return this.agent + " hat die Aufgabe " + this.numberOfCounties + " Länder zu erobertn und mit " + (this.withTwoArmies ? "2 Armeen":"1 Armee") + " zu besetzten ";
    }
}


