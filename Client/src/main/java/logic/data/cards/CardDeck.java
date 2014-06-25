package main.java.logic.data.cards;

import java.util.*;

import main.java.ui.CUI.utils.IO;

public class CardDeck {
	private Stack<String> deck = new Stack<String>();
	private Stack<Integer> bonus = new Stack<Integer>();;
	
	public CardDeck(){
		builtDeck();
	}
	
	private void builtDeck(){
		for(int i = 14; i>0; i--){
			this.deck.push("Kanone");
			this.deck.push("Soldat");
			this.deck.push("Reiter");
		}
		this.deck.push("Joker");
		this.deck.push("Joker");
		Collections.shuffle(this.deck);
		this.bonus.push(20);
		this.bonus.push(15);
		this.bonus.push(12);
		this.bonus.push(10);
		this.bonus.push(8);
		this.bonus.push(6);
		this.bonus.push(4);
	}
	private int calculateBonus(){
		int bo = this.bonus.pop();
		if(this.bonus.isEmpty()){
			this.bonus.push(bo + 5);
		}
		return bo;
	}
	public String getCard(){
		return this.deck.pop();
	}
	public Stack<String> getCards(){
		return this.deck;
	}
	public Stack<Integer> getBonusList(){
		return this.bonus;
	}
	
	public int returnCards(String card1, String card2, String card3){
		this.deck.push(card1);
		this.deck.push(card2);
		this.deck.push(card3);
		Collections.shuffle(this.deck);
		return calculateBonus();
	}
	
}
