package logic.data.cards;

import logic.data.*;

public class Card implements Comparable<Card> {
	private Player belongsTo;
	private String type;
	private Country country;
	
	public Card(Country co, String ty){
		this.country = co;
		this.type = ty;
	}
	public void setBelongsTo(Player pl){
		this.belongsTo = pl;
	}
	public Country getCountry(){
		return this.country;
	}
	public Player getBelongsTo(){
		return this.belongsTo;
	}
	public String getType(){
		return this.type;
	}
	public int compareTo(Card otherCard){
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
        Country country = this.getCountry();
        String msg = "";
        if (country != null){
            msg += country.getName();
        }
        msg += this.getType();
        return  msg;
    }
}
