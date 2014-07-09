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

package server.logic.utils;


import interfaces.data.utils.IDice;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * @author Jennifer Theloy, Stefan Bieliauskas
 *
 * Diese Klasse dient zum Würfeln
 */
@SuppressWarnings("serial")
public class Dice extends UnicastRemoteObject implements IDice,Comparable<IDice> {
	
	private int dicenumber;

    /**
     * Erstellt und wirft den Würfel
     */
	public Dice()  throws RemoteException{
		this.throwDice();
	}


	/**
	 *  throw the dice 
	 */
	public void throwDice() throws RemoteException {
		dicenumber = (int)(Math.random()*6+1);
		//choose a random number between 1-6 	
		}

    /**
     * Gibt die Gewürfelte Zahl aus
     * @return
     */
	public int getDiceNumber() {
		return dicenumber;
	}

    /**
     * Stellt die Funktion zum vergleichen von 2 Würfeln zur Verfügung
     * @param otherDice - der andere Würfel der vergleichen werden soll
     * @return - 0 => equals; 1=> greater; -1 => smaller
     */

    public int compareTo(IDice otherDice){
        try {
            if(this.dicenumber == otherDice.getDiceNumber()){
                return 0;  //equals
            }
            else if (this.dicenumber > otherDice.getDiceNumber()){
                return 1; //größer
            }
            else {
                return -1; //kleiner
            }
        }catch (RemoteException e){
            throw new RuntimeException(e);
        }
    }

    /**
     * Pürft ob der otherDice größer ist
     * @param otherDice Zu prüfender Würfel
     * @return True, wenn aktueller würfel größer is
     */
    public boolean isDiceHigherOrEqual(IDice otherDice)  throws RemoteException{
    	if(this.compareTo(otherDice)==1){
    		return true;
    	}else if(this.compareTo(otherDice)==0){
    		return true;
    	}else{
    		return false;
    	}
    }
    
    public String toString(){
    	String s = "" + this.dicenumber;
    	return s;
    }
    public String toStringRemote () throws RemoteException{
        return this.toString();
    }

}
