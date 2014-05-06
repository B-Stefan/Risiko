package main.java.logic.orders;
import java.util.*;

import main.java.logic.*;


public class OrderTakeOverContinents implements IOrder {
	/**
	 * Erster Kontinent, der erobert werden soll
	 */
	private Continent continentOne;
	/**
	 * zweiter Kontinent, der erobert werden soll
	 */
	private Continent continentTwo;
	/**
	 * Liste alle Kontoinente zur Prüfung des dritten Kontinents
	 */
	private List<Continent> continents = new ArrayList<Continent>();
	/**
	 * Der Spieler, dem die Order zugewiesen ist
	 */
	private Player agent;
	


	
	/**
	 * der erste Constructor ist für die Erstellung von Orders, bei denen nicht nur zwei Kontinente erobert werden sollen, sondern noch ein zusätzlicher 
	 * @param one erster zu übernehmender Kontinent
	 * @param two zweiter zu übernehmender Kontinent
	 * @param pl Spieler, dem die Order zugewiesen ist
	 * @param continents die Liste aller Kontinente
	 */
	public OrderTakeOverContinents(Continent one, Continent two, Player pl, ArrayList<Continent> continents){

		this.continentOne = one;
		this.continentTwo = two;
		this.continents = continents;
		setAgent(pl);
	}

	/**
	 * der zweite Constructor ist für die Erstellung einer Order, bei der nur zwei bestimmte Kontinente übernommen werden müssen
	 * Die Kontinentliste wird in diesem Fall auf null gesetzt, da es keinen dritten Kontinent zu ermitteln gibt
	 * @param one erster zu übernehmender Kontinent
	 * @param two zweiter zu übernehmender Kontinent
	 * @param pl Spieler, dem die Order zugewiesen ist
	 */
	public OrderTakeOverContinents(Continent one, Continent two, Player pl) {

		this.continentOne = one;
		this.continentTwo = two;
		this.continents = null;
		setAgent(pl);
	}
	
	/**
	 * Es wird ein Wahrheitswert ermittelt, der angibt, ob der dritte (beliebige) Kontinent übernommen wurde
	 * @return True, wenn ein dritter Kontinent übernommen wurde, Flase wenn nicht
	 */
	private boolean thirdContinent(){
		for(Continent c : this.continents){
			if(c != this.continentOne && c != this.continentTwo && c != null){
				if(c.getCurrentOwner() == this.agent){
					return true;
				}
			}
		}
		return false;
	}
	/**
	 * Überprüft, ob die Order erfüllt wurde (Für beide Fälle)
	 */
	@Override
	public boolean isCompleted() {
		if (this.continents == null){
			if(this.agent == this.continentOne.getCurrentOwner() && this.agent == this.continentTwo.getCurrentOwner()){
				return true;
			}
		}else{
			if(thirdContinent() && this.agent == this.continentOne.getCurrentOwner() && this.agent == this.continentTwo.getCurrentOwner()){
				return true;
			}
		}
		return false;
	}

	/**
	 * Setzt den Spieler, dem die Order zugewiesen ist und setzt zudem beim Spieler den Auftrag
	 */
	public void setAgent(Player ag) {
		this.agent = ag;
	}
	/**
	 * Getter für den Agent
	 * @return Player
	 */

	public Player getAgent() {
		return this.agent;
	}

    @Override
    public String toString(){
        return this.agent + " hat die Aufgabe " + continentOne + " und " + continentTwo + " zu erobern. ";
    }
}
