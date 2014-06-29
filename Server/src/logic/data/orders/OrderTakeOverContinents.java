package logic.data.orders;

import interfaces.data.IContinent;
import interfaces.data.IPlayer;
import interfaces.data.Orders.IOrder;
import logic.data.Continent;
import logic.data.Player;


public class OrderTakeOverContinents implements IOrder {
	/**
	 * Erster Kontinent, der erobert werden soll
	 */
	private IContinent continentOne;
	/**
	 * zweiter Kontinent, der erobert werden soll
	 */
	private IContinent continentTwo;
	/**
	 * Der Spieler, dem die Order zugewiesen ist
	 */
	private IPlayer agent;

	/**
	 * der zweite Constructor ist für die Erstellung einer Order, bei der nur zwei bestimmte Kontinente übernommen werden müssen
	 * Die Kontinentliste wird in diesem Fall auf null gesetzt, da es keinen dritten Kontinent zu ermitteln gibt
	 * @param contigent1 erster zu übernehmender Kontinent
	 * @param contigent2 zweiter zu übernehmender Kontinent
	 * @param agend Spieler, dem die Order zugewiesen ist
	 */
	public OrderTakeOverContinents(IContinent continent1, IContinent continent2, IPlayer agend) {

		this.continentOne = continent1;
		this.continentTwo = continent2;
		setAgent(agend);
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
	public void setAgent(IPlayer ag) {
		this.agent = ag;
	}
	/**
	 * Getter für den Agent
	 * @return Player
	 */

	public IPlayer getAgent() {
		return this.agent;
	}

    @Override
    public String toString(){
        return this.agent + " hat die Aufgabe " + continentOne + " und " + continentTwo + " zu erobern. ";
    }
}
