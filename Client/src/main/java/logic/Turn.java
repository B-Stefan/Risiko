package main.java.logic;
import java.util.*;

public class Turn {
	private Player player;
	private Map map;
	private ArrayList<Army> newArmies = new ArrayList<Army>();
	
	public Turn(Player p, Map m){
		this.player = p;
	}
	
    public Player getPlayer (){
    	return this.player; 
    }
    
    public String toString(){
        return "Turn: " + this.player.toString();
    }
    
    /**
     * berechnet die Anzahl der Armeen, die der jewilige Spieler am Anfang seines Zuges neu hinzubekommt. 
     * @return Anzahl, der neuen Armeen des jeweiligen Spielers
     */
    private int determineAmountOfNewArmies(){
    	int amountNewArmies = (this.player.getCountries().size())/3;
    	amountNewArmies += this.map.getBonus(this.player);
    	if (amountNewArmies<3){
    		amountNewArmies = 3;
    	}
    	return amountNewArmies;
    }
    
    private void createNewArmies(){
    	for (int i = 0; i<determineAmountOfNewArmies(); i++){
    		this.newArmies.add(null);
    		
    	}
    }
    
    public ArrayList<Army> getNewArmies(){
    	return this.newArmies;
    }
    
    public void setNewArmy(Country position){
    		Army a = new Army(this.player, position);
    		position.addArmy(a);
    }
    
    
}
