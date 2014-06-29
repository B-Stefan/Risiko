package interfaces.data;
import exceptions.CountriesNotConnectedException;

/**
 * Created by Stefan on 29.06.14.
 */
public interface IArmy {
	public void setPosition(ICountry country)  throws CountriesNotConnectedException;

    /**
     * Gibt den Besitzer der Armee wieder
     * @return  Besitzer der Armee
     */
    public IPlayer getOwner();

    /**
	 * Getter für die Position
	 * @return position Aktuelle Position der Armee, kann null zurückgeben.
	 */
	public ICountry getPosition();
	

}
