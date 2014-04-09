package main.java.logic;
import java.util.*;

public class Round {
	/**
	 * Der Spieler, der am Zug ist
	 */
	private Player currentPlayer;
	private Queue<Player> players;
	private Turn currentTurn;
	
	
	public Round(Queue<Player> p){
		this.players = p;
	}
	/**
	 * Setzt den obersten Spieler der Queue als CurrentPlayer und löscht ihn aus der Queue (Poll())
	 */
	public void setCurrentPlayer(){
		this.currentPlayer = players.poll();
	}
	/**
	 * Getter für den aktuellen Spieler
	 * @return currentPayler: gibt aktuellen Spieler
	 */
	public Player getCurrentPlayer(){
		return this.currentPlayer;
	}
	public void createNewTurn(){
		Turn turn = new Turn(currentPlayer); 
		this.currentTurn = turn;
	}
}
