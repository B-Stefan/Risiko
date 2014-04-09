package main.java.logic;
import java.util.*;

public class Round {
	/**
	 * Der Spieler, der am Zug ist
	 */
	private Player currentPlayer;
	private Queue<Player> players;
	
	
	public Round(Queue<Player> p){
		this.players = p;
	}
	/**
	 * Setzt den aktuellen Spieler
	 * @param p aktueller Spieler
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
}
