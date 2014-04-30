package main.java.logic;
import java.util.*;

public class Turn {
	private Player player;
	private Map map;
	private Stack<Army> newArmies = new Stack<Army>();
	private ArrayList<Army> movedArmies = new ArrayList<Army>();
	
	public Turn(Player p, Map m){
		this.player = p;
		createNewArmies();
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
    /**
     * erstelle eine Liste mit den neuen Armeen     
     */
    private void createNewArmies(){
    	for (int i = 0; i<determineAmountOfNewArmies(); i++){
    		this.newArmies.add(new Army(this.player));
    	}
    }
    /**
     * getter für die Liste der neuen Armeen
     * @return Stack der neuen Armeen
     */
    
    public Stack<Army> getNewArmies(){
    	return this.newArmies;
    }
    
    /**
     * weist der jeweiligen Armee ein Land zu
     * @param position Das Land, zu welchem die Armee zugewiesen werden soll
     */
    public void setNewArmy(Country position){
    	Army a = this.newArmies.pop();	
    	a.setPosition(position);
    	position.addArmy(a);
    	//@todo Exeption
    }
    /**
     * fügt der Liste der bereits verschobenen Einheiten die Armee hinzu
     * @param a bewegte Armee
     */
    public void armyMoved(Army a){
    	this.movedArmies.add(a);
    }
    /**
     * prüft ob die Armee bereits verschoben wurde in diesem Zug
     * @param a Armee, die überprüft werden soll
     * @return boolean -> true wenn die Armee bereits verschoben wurde, false, wenn sie nioch nicht verschoben wurde
     */
    public boolean isArmyAlreadyMoved(Army a){
    	return movedArmies.contains(a);
    }
    /**
     * Ändert die Position der Armee, falls sie noch nicht bewegt wurde und ändert den Status der Armee in bereits bewegt
     * @param c das Zielland
     * @param a die zu bewegende Armee
     * @return true wenn die Armee bewegt wurde kann, false wenn nicht
     */
    public boolean moveArmy(Country c, Army a){
    	if (isArmyAlreadyMoved(a) != true){
    		a.setPosition(c);
    		c.addArmy(a);
    		armyMoved(a);
    		return true;    		
    	}
    	return false;
    }
    

    
}
