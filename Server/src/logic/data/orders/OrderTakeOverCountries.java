package logic.data.orders;

import interfaces.data.ICountry;
import interfaces.data.IPlayer;
import interfaces.data.Orders.IOrder;
import logic.data.Country;
import logic.data.Player;

public class OrderTakeOverCountries implements IOrder {
	/**
	 * gibt an, ob zwei Armeen auf dem Land stehen sollen (und somit nur 18 Länder erobert werden müssen) oder nicht
	 */
	private boolean withTwoArmies;
	/**
	 * Der Spieler, dem die Order zugewiesen ist
	 */
	private IPlayer agent;
	
	/**
	 * 
	 * @param twoArmies True bedeutet, dass es sich bei dem Auftrag um den Fall handelt, dass zwei Armeen auf den Ländern stehen müssen. Bei False müssen nur eine bestimmte Anzahl an Ländern eingenommen werden
	 * @param agend Der Spieler, dem die Order zugewiesen wird
	 *
	 */
	public OrderTakeOverCountries(boolean twoArmies, IPlayer agend) {
		this.withTwoArmies = twoArmies;
		setAgent(agend);
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
				for(ICountry c : this.agent.getCountries()){
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
	public void setAgent(IPlayer agend)  {
			this.agent = agend;
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

	public IPlayer getAgent() {
		return this.agent;
	}

}
