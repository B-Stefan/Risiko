package server.logic.data;

import interfaces.data.*;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.Map;


/**
 * @author Jennifer Theloy, Thu Nguyen, Stefan Bieliauskas
 *
 * Dise Klasse bildet ein einzelnes Kontinent ab
 */
public class Continent extends UnicastRemoteObject implements IContinent{

	/**
	 * Name des Kontinents
	 */
	private final String name;

	/**
	 * Eine ArrayLsit mit den Ländern, die sich auf dem Kontinent befinden Länder
     * @return Gibt die Liste der Länder zurück, die disem Kontinent zugeordnet wurden
	 */
	private final HashMap<UUID,Country> countrys = new HashMap<UUID, Country>();

	/**
	 * der Bonus, der die Kontrolle des jeweiligen Kontinents einbringt für die neuen Armeen
	 */
	private final int bonus;

    /**
     * Erstellt ein Kontinent
     * @param name Name des Kontinents
     * @param bonus Bonus, der der Owner pro Runde an Armeen erhält
     */
	public Continent (final String name, int bonus) throws RemoteException{
		this.name = name;
		this.bonus = bonus;
	}

	/**
	 * Gibt den namen des Kontinents zurück
	 * @return Gibt den Namen des KOntinents zurück
	 */

	public String getName() throws RemoteException{
		return this.name;
	}


    /**
     * Ermittelt den aktuellen Besitzter des Kontinents, prüft ob alle Länder den gleichen Player als owner haben
     * @return Gibt den Player zurück, der alle Länder auf diesem Kontinent besitzt, wenn der Kontinent geteilt wird wird null zurück gegeben.
     */
    public Player getCurrentOwner () throws RemoteException{
        boolean playerChange = false;
        Player ruler = null;

        for (Map.Entry<UUID,Country> entry : countrys.entrySet()){
            Country currentCountry = entry.getValue();

            if(ruler != null && ruler!= currentCountry.getOwner()){
                playerChange = true;
            }
            else if(ruler == null ){
                ruler = currentCountry.getOwner();
            }

        }
        if(!playerChange){
            return  ruler;
        }
        else {
            return null;
        }
    }

	/**
	 * Fügt dem Kontinent ein Land hinzu
	 * @param player Der Land, der dem Kontinent hinzugefügt werden soll
	 */
	public void addCountry(Country player) throws RemoteException{
		if(!countrys.containsKey(player.getId())){
            countrys.put(player.getId(), player);
        }
	}

    /**
     * Löscht aus dem Kontinent ein Land
     * @param countryToDelete Land das gelöscht werden soll
     * @return
     */
    public boolean removeCountry (ICountry countryToDelete) throws RemoteException{
        if(countrys.containsKey(countryToDelete.getId())){
            countrys.remove(countryToDelete);
            return  true;
        }
        return  false;

    }

    public int getBonus() throws RemoteException{
    	return this.bonus;
    }

    @Override
    public String toString() {
        try {
			return this.getName();
		} catch (RemoteException e) {
			e.printStackTrace();
			return "";
		}
    }

    /**
     * ToString methode, die Remote aufgerufen werden kann
     * @return
     * @throws RemoteException
     */
    public String toStringRemote() throws RemoteException{
        return this.toString();
    }

}