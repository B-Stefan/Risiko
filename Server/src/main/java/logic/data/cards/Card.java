package logic.data.cards;

import interfaces.data.ICountry;
import interfaces.data.IPlayer;
import interfaces.data.cards.ICard;
import logic.data.*;

public class Card implements ICard {
	private IPlayer belongsTo;
	private String type;
	private ICountry country;
	
	public Card(ICountry c, String ty){
		this.country = c;
		this.type = ty;
	}
	public void setBelongsTo(IPlayer pl){
		this.belongsTo = pl;
	}
	public ICountry getCountry(){
		return this.country;
	}
	public IPlayer getBelongsTo(){
		return this.belongsTo;
	}
	public String getType(){
		return this.type;
	}
	public int compareTo(ICard otherCard){
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
	}
    public String toString(){
        ICountry country = this.getCountry();
        String msg = "";
        if (country != null){
            msg += country.getName();
        }
        msg += this.getType();
        return  msg;
    }
}
