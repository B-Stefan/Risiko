package server.logic.utils;


import interfaces.data.utils.IDice;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * @author Jennifer Theloy, Thu Nguyen, Stefan Bieliauskas
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
