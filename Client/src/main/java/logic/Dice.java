package main.java.logic;

public class Dice implements Comparable<Dice>  {
	
	private int dicenumber;
	
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
	
}
