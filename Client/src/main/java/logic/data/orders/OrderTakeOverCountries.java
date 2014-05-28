package main.java.logic.data.orders;

import main.java.logic.data.Country;
import main.java.logic.data.Player;

public class OrderTakeOverCountries implements IOrder {
	/**
	 * gibt an, ob zwei Armeen auf dem Land stehen sollen (und somit nur 18 Länder erobert werden müssen) oder nicht
	 */
	private boolean withTwoArmies;
	/**
	 * Der Spieler, dem die Order zugewiesen ist
	 */
	private Player agent;
	
	/**
	 * 
	 * @param twoArmies True bedeutet, dass es sich bei dem Auftrag um den Fall handelt, dass zwei Armeen auf den Ländern stehen müssen. Bei False müssen nur eine bestimmte Anzahl an Ländern eingenommen werden
	 * @param ag Der Spieler, dem die Order zugewiesen wird
	 *
	 */
	public OrderTakeOverCountries(boolean twoArmies, Player ag) {
		this.withTwoArmies = twoArmies;
		setAgent(ag);
	}
	
	/**
	 * Testet, ob die Aufgabe erfüllt ist
	 * Für den Fall, dass zwei Armeen auf dem Land stehen müssen, prüft er, ob mindestens 18 Länder erobert sind und ob auf jedem auch wirklich zwei Armeen stehen
	 * Für den anderen Fall überprüft er nur, ob 24 Länder übernommen wurden
	 */
	@Override
	public boolean isCompleted() {
		if (withTwoArmies){
			if(this.agent.getCountries().size()>=18){
				for(Country c : this.agent.getCountries()){
					if(c.getArmyList().size()<2){
						return false;
					}
				}
				return true;
			}
		} else {
			if(this.agent.getCountries().size()>=24){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Setzt den Spieler, dem die Order zugewiesen ist und setzt zudem beim Spieler den Auftrag
	 */
	@Override
	public void setAgent(Player ag)  {
			this.agent = ag;
	}

    @Override
    public String toString(){
        //@todo beschreibung verbessern!
        return this.agent + " hat die Aufgabe andere Länder zu erobern" ;
    }

	/**
	 * Getter für den Agent
	 * @return Player
	 */

	public Player getAgent() {
		return this.agent;
	}

}
