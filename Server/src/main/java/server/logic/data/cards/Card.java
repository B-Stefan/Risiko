package server.logic.data.cards;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import interfaces.data.ICountry;
import interfaces.data.IPlayer;
import interfaces.data.cards.ICard;
import server.logic.data.*;

public class Card extends UnicastRemoteObject implements ICard, Comparable<ICard> {
	private IPlayer belongsTo;
	private String type;
	private Country country;
	
	public Card(Country c, String ty) throws RemoteException{
		this.country = c;
		this.type = ty;
	}
	public Card(String ty) throws RemoteException{
		this.type = ty;
		this.country = null;
	}
	public Country getCountry() throws RemoteException{
		return this.country;
	}
	public String getType() throws RemoteException{
		return this.type;
	}
	public int compareTo(ICard otherCard){
        try {
            if(otherCard.getType() == this.getType()){
                return 0; //equals
            }else if(otherCard.getType() == "Soldat"){
                return 1;
            }else if(otherCard.getType() == "Kanone"){
                if(this.getType() == "Soldat" || this.getType() == "Reiter"){
                    return -1;
                }else{
                    return 1;
                }
            }else if(otherCard.getType() == "Reiter"){
                if(this.getType() == "Soldat"){
                    return -1;
                }else{
                    return 1;
                }
            }else{
                return -1;
            }
        }catch (RemoteException e){
            throw new RuntimeException(e);
        }
	}
    public String toString(){

        ICountry country = null;
        try{
            country = this.getCountry();
        }catch (RemoteException e){
            e.printStackTrace();
        }

        String msg = "";
        if (country != null){
            try {
				msg += country.getName() + " - ";
			} catch (RemoteException e) {
				e.printStackTrace();
				return " ";
			}
        }

        try{
            msg += this.getType();
        }catch (RemoteException e){
            e.printStackTrace();
        }

        return  msg;
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
