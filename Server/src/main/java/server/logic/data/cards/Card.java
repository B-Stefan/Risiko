

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

package server.logic.data.cards;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import interfaces.data.ICountry;
import interfaces.data.IPlayer;
import interfaces.data.cards.ICard;
import server.logic.data.*;

public class Card extends UnicastRemoteObject implements ICard, Comparable<ICard> {
	/**
	 * Der Spieler, in dessen besitz die Karte ist
	 */
	private IPlayer belongsTo;
	/**
	 * Der Typ der Karte (Joker, Reiter, Soldat, Kanone)
	 */
	private String type;
	/**
	 * Das Land, dem die Karte zugewiesen ist
	 */
	private Country country;
	/**
	 * Constructor
	 * @param c Das Land, dem die Karte zugewiesen wrden soll
	 * @param ty Der Typ, der der Karte zugewiesen werden soll
	 * @throws RemoteException
	 */
	public Card(Country c, String ty) throws RemoteException{
		this.country = c;
		this.type = ty;
	}
	/**
	 * Constructor
	 * @param ty Der Typ, der der Karte zugewiesen werden soll
	 * @throws RemoteException
	 */
	public Card(String ty) throws RemoteException{
		this.type = ty;
		this.country = null;
	}
	/**
	 * Gibt das Land aus, welches der Karte zugewiesen ist
	 */
	public Country getCountry() throws RemoteException{
		return this.country;
	}
	/**
	 * Gibt den Typ der Karte aus
	 */
	public String getType() throws RemoteException{
		return this.type;
	}
	/**
	 * Hilfe zur Sortierung der Karte nach Alphabet
	 */
	public int compareTo(ICard otherCard){
        try {
            if(otherCard.getType() == this.getType()){
                return 0; //equals
            }else if(otherCard.getType() == "Soldat"){
                return 1;
            }else if(otherCard.getType() == "Kanone"){
                if(this.getType() == "Soldat" || this.getType() == "Reiter"){
                    return -1;
                }else{
                    return 1;
                }
            }else if(otherCard.getType() == "Reiter"){
                if(this.getType() == "Soldat"){
                    return -1;
                }else{
                    return 1;
                }
            }else{
                return -1;
            }
        }catch (RemoteException e){
            throw new RuntimeException(e);
        }
	}
	/**
	 * Wandelt die Informationen der Karte in einen String
	 */
    public String toString(){

        ICountry country = null;
        try{
            country = this.getCountry();
        }catch (RemoteException e){
            e.printStackTrace();
        }

        String msg = "";
        if (country != null){
            try {
				msg += country.getName() + " - ";
			} catch (RemoteException e) {
				e.printStackTrace();
				return " ";
			}
        }

        try{
            msg += this.getType();
        }catch (RemoteException e){
            e.printStackTrace();
        }

        return  msg;
    }
    /**
     * ToString methode, die Remote aufgerufen werden kann
     * @return
     * @throws RemoteException
     */
    public String toStringRemote() throws RemoteException{
        return this.toString();
    }
}
