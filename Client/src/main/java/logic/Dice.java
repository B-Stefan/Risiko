package main.java.logic;

public class Dice {
	
	private int dicenumber;
	
	/**
	 *  throw the dice 
	 */
	public void throwDice() {
		dicenumber = (int)(Math.random()*6+1);
		//choose a random number between 1-6 	
		}
	
	public int getDice() {
		return dicenumber;
	}
	
}
