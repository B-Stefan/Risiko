package logic.data.orders;

import interfaces.data.IContinent;
import interfaces.data.IPlayer;
import interfaces.data.Orders.IOrder;

import java.util.ArrayList;
import java.util.List;

import logic.data.Continent;
import logic.data.Player;

public class OrderTakeOverThreeContinents implements IOrder{
	/**
	 * Liste alle Kontoinente zur Prüfung des dritten Kontinents
	 */
	private List<IContinent> continents = new ArrayList<IContinent>();
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
	 * der erste Constructor ist für die Erstellung von Orders, bei denen nicht nur zwei Kontinente erobert werden sollen, sondern noch ein zusätzlicher 
	 * @param contigent1 erster zu übernehmender Kontinent
	 * @param contigent2 zweiter zu übernehmender Kontinent
	 * @param agend Spieler, dem die Order zugewiesen ist
	 * @param arrayList die Liste aller Kontinente
	 */
	public OrderTakeOverThreeContinents(IContinent contigent1, IContinent contigent2, IPlayer agend, ArrayList<IContinent> arrayList){
		this.continentOne = contigent1;
		this.continentTwo = contigent2;
		this.continents = arrayList;
		setAgent(agend);
	}
	/**
	 * Es wird ein Wahrheitswert ermittelt, der angibt, ob der dritte (beliebige) Kontinent übernommen wurde
	 * @return True, wenn ein dritter Kontinent übernommen wurde, Flase wenn nicht
	 */
	private boolean thirdContinent(){
		for(IContinent c : this.continents){
			if(c != this.continentOne && c != this.continentTwo && c != null){
				if(c.getCurrentOwner() == this.agent){
					return true;
				}
			}
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
	/**
	 * Überprüft, ob die Order erfüllt wurde (Für beide Fälle)
	 */
	@Override
	public boolean isCompleted() {
		if(thirdContinent() && this.agent == this.continentOne.getCurrentOwner() && this.agent == this.continentTwo.getCurrentOwner()){
			return true;
		}
		return false;
	}
	@Override
	public String toString(){
		return this.agent + " hat die Aufgabe " + this.continentOne + " und " + this.continentTwo + " und einen weiteren Kontinent zu erobern.";
	}
	
}
