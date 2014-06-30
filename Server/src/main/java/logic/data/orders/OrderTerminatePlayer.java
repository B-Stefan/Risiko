package logic.data.orders;

import java.rmi.RemoteException;

import interfaces.data.IPlayer;
import interfaces.data.Orders.IOrder;
import logic.data.Player;

/**
 * Created by Stefan on 31.03.2014.
 * @author Stefan Bieliauskas
 * Diese Klasse ist fÃ¼r die Erfassung der AuftrÃ¤ge ausgelegt,
 * die darauf abzielen einen anderen Spieler zu vernichten.
 *
 *
 * @see IOrder
 */
public class OrderTerminatePlayer extends AbstractOrder implements IOrder{
	/**
	 * Der Spieler, der von dem Spieler, dem die Order zugeiwesen ist, vernichtet werden soll
	 */
	private IPlayer victim;
	
	/**
	 * Zuerst wird der Agent gesetzt, damit ausgeschlossen werden kann, dass das Victim und der Player identisch sind
	 * @param playerToTerminate Opfer des Auftrags
	 * @param agend Der Spieler, dem die Order zugewiesen werden soll
	 */
	 public OrderTerminatePlayer(IPlayer playerToTerminate, IPlayer agend) throws RemoteException {
         super(agend);
         this.victim = playerToTerminate;
	 }

	
    @Override
    public boolean isCompleted()throws RemoteException {
    	if (this.victim.getCountries().isEmpty()) {
            return true;
        } else {
        	return false;
        }
    }
    
    /**
     * Gibt den zu vernichtenden Spieler wieder
     * @return Player
     */
    
	public IPlayer getVictim() {
		return victim;
	}


    @Override
    public String toString(){
        //@todo beschreibung verbessern!
        return this.agent + " hat die Aufgabe den Spieler " + this.getVictim() + " zu vernichten"  ;
    }
}
