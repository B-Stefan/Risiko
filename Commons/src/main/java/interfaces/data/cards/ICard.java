package interfaces.data.cards;

import interfaces.data.ICountry;
import interfaces.data.IPlayer;

import java.io.Serializable;
import java.rmi.Remote;

/**
 * Created by Stefan on 29.06.14.
 */
public interface ICard extends Remote, Serializable, Comparable<ICard> {
	public void setBelongsTo(IPlayer pl);
	
	public ICountry getCountry();
	
	public IPlayer getBelongsTo();
	
	public String getType();
	
	public int compareTo(ICard otherCard);
	
    public String toString();
}
