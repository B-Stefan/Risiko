package logic.utils;



/**
 * @author Jennifer Theloy, Thu Nguyen, Stefan Bieliauskas
 *
 * Diese Klasse dient zum Würfeln
 */
public class Dice implements Comparable<Dice>  {
	
	private int dicenumber;

    /**
     * Erstellt und wirft den Würfel
     */
	public Dice(){
		this.throwDice();
	}


	/**
	 *  throw the dice 
	 */
	public void throwDice() {
		dicenumber = (int)(Math.random()*6+1);
		//choose a random number between 1-6 	
		}
	
	public int getDiceNumber() {
		return dicenumber;
	}

    /**
     * Stellt die Funktion zum vergleichen von 2 Würfeln zur Verfügung
     * @param otherDice - der andere Würfel der vergleichen werden soll
     * @return - 0 => equals; 1=> greater; -1 => smaller
     */
    @Override
    public int compareTo(Dice otherDice){
        if(this.dicenumber == otherDice.getDiceNumber()){
            return 0;  //equals
        }
        else if (this.dicenumber > otherDice.getDiceNumber()){
            return 1; //größer
        }
        else {
            return -1; //kleiner
        }
    }

    /**
     * Pürft ob der otherDice größer ist
     * @param otherDice Zu prüfender Würfel
     * @return True, wenn aktueller würfel größer is
     */
    public boolean isDiceHigherOrEqual(Dice otherDice){
    	if(this.compareTo(otherDice)==1){
    		return true;
    	}else if(this.compareTo(otherDice)==0){
    		return true;
    	}else{
    		return false;
    	}
    }
    
    public String toString(){
    	String s = "" + this.dicenumber;
    	return s;
    }
    
}
