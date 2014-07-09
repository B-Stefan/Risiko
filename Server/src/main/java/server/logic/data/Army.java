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

package server.logic.data;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import interfaces.data.*;
import exceptions.CountriesNotConnectedException;

/**
 * @author Jennifer Theloy,  Stefan Bieliauskas
 *
 * Diese Klasse bildet eine einzelne Armee im Spiel ab
 */
public class Army extends UnicastRemoteObject implements IArmy{

    /**
     * Der Owner beschreibt wem diese Armee im Spiel zugeordnet wird.
     */
	private final Player owner;


    /**
     * Die Position spiegelt dabei immer die aktuelle Position dieser Armee da
     */
	private Country position;


    /**
     * Erstellt eine Armee für das Spiel, der Besitzer kann nachträglich nicht geändert werden
     * @param player Besitzer dieser Armee
     */
	public Army(final Player player) throws RemoteException{
		this.owner = player;
	}


	/**
	 * Setzt die Armee auf die neue Position.
     * Dabei wird gleichzeigt der die 1:1 Zuordnung zum Country beachtet, sodass eine Armee immer nur auf einem Country steht.
     * Auf der anderen Seite sind auch alle Armeen, die einem Country zugewiesen wurden auf dieser Position gesetzt.
     * Dies erhöht die Datenintegrität
     *
     *
	 * @param country Übergiebt die (neue) Position der Armee
	 *
     * @see Country#(Army)
     */
	public void setPosition(final Country country)  throws CountriesNotConnectedException, RemoteException{

        //Armee sitzt bereits auf der Position
        if(country == this.position){
            return;
        }
        //Neue Posituon auf dem Spiefeld
        if(country != null && this.position != country) {

            //Positionswechsel auf der Karte
            if(this.position != null ){
                //Prüfen, ob Länder verbunden sind
                if(!this.position.isConnected(country)){
                    throw new CountriesNotConnectedException(this.position, country);
                };
                this.position.removeArmy(this);
            }
            this.position = country;
            country.addArmy(this);
        }
        //Einheit von Position entfernt
        else {
            if(this.position != null  ){
                this.position.removeArmy(this);
            }
            this.position = country;
        }
	}

    /**
     * Gibt den Besitzer der Armee wieder
     * @return  Besitzer der Armee
     */
    public Player getOwner() throws RemoteException{
        return this.owner;
    }


    /**
	 * Getter für die Position
	 * @return position Aktuelle Position der Armee, kann null zurückgeben.
	 */
	public Country getPosition()throws RemoteException{
		return this.position;
	}
	

}
