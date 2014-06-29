package logic.data.orders;

import logic.data.Continent;
import logic.data.Player;


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
	 * Der Spieler, dem die Order zugewiesen ist
	 */
	private Player agent;

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
		setAgent(pl);
	}
	
	/**
	 * Überprüft, ob die Order erfüllt wurde (Für beide Fälle)
	 */
	@Override
	public boolean isCompleted() {
		if(this.agent == this.continentOne.getCurrentOwner() && this.agent == this.continentTwo.getCurrentOwner()){
			return true;
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
