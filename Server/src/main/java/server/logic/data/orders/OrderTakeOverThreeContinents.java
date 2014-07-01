package server.logic.data.orders;

import interfaces.data.Orders.IOrder;

import java.rmi.RemoteException;
import java.util.List;

import server.logic.data.Continent;
import server.logic.data.Player;

public class OrderTakeOverThreeContinents extends AbstractOrder implements IOrder{
	/**
	 * Liste alle Kontoinente zur Prüfung des dritten Kontinents
	 */
	private final List<Continent> continents;
	/**
	 * Erster Kontinent, der erobert werden soll
	 */
	private final Continent continentOne;
	/**
	 * zweiter Kontinent, der erobert werden soll
	 */
	private final Continent continentTwo;

	
	/**
	 * der erste Constructor ist für die Erstellung von Orders, bei denen nicht nur zwei Kontinente erobert werden sollen, sondern noch ein zusätzlicher 
	 * @param contigent1 erster zu übernehmender Kontinent
	 * @param contigent2 zweiter zu übernehmender Kontinent
	 * @param agend Spieler, dem die Order zugewiesen ist
	 * @param arrayList die Liste aller Kontinente
	 */
	public OrderTakeOverThreeContinents(final Continent contigent1,final Continent contigent2,final Player agend, final List<Continent> arrayList)throws RemoteException{
		super(agend);
        this.continentOne = contigent1;
		this.continentTwo = contigent2;
		this.continents = arrayList;

	}
	/**
	 * Es wird ein Wahrheitswert ermittelt, der angibt, ob der dritte (beliebige) Kontinent übernommen wurde
	 * @return True, wenn ein dritter Kontinent übernommen wurde, Flase wenn nicht
	 */
	private boolean thirdContinent()throws RemoteException{
		for(Continent c : this.continents){
			if(c != this.continentOne && c != this.continentTwo && c != null){
				if(c.getCurrentOwner() == this.agent){
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean isCompleted()throws RemoteException {
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
