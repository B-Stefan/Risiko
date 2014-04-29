package main.java.logic;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

public class Round {
	/**
	 * Der Spieler, der am Zug ist
	 */
	private Player currentPlayer;
    private Map map;
	private Queue<Player> players;
    private Turn currentTurn;
	
	
	public Round(final List<Player> p,final Map map){
		this.map = map;
        this.players = new LinkedBlockingQueue<Player>(p);
        this.setNextTurn(); //Set next Turn
	}
	/**
	 * Setzt den obersten Spieler der Queue als CurrentPlayer und l�scht ihn aus der Queue (Poll())
	 */
	public void setCurrentPlayer(){
		this.currentPlayer = players.poll();
	}
	/**
	 * Getter f�r den aktuellen Spieler
	 * @return currentPayler: gibt aktuellen Spieler
	 */
	public Player getCurrentPlayer(){
		return this.currentPlayer;
	}
	public Turn setNextTurn(){
		this.setCurrentPlayer();
        this.currentTurn = new Turn(this.getCurrentPlayer());
        return this.currentTurn;
	}
    public Turn getCurrentTurn(){return this.currentTurn; }
}
