package main.java.logic.data.cards;

import java.util.*;

import main.java.logic.data.Country;
import main.java.logic.data.Player;
import main.java.logic.exceptions.NotEnoughCardsToExchangeException;
import main.java.logic.exceptions.YouCannotDrawANewCardException;
import main.java.ui.CUI.utils.IO;

public class CardDeck {
	private Stack<Card> deck = new Stack<Card>();
	private Stack<Integer> bonus = new Stack<Integer>();
	
	public CardDeck(ArrayList<Country> cos){
		builtDeck(cos);
	}
	
	private void builtDeck(ArrayList<Country> cos){
		for(Country c : cos){
			if(deck.isEmpty()||this.deck.size() == 1){
				this.deck.add(new Card(c, "Joker"));
			}else if(this.deck.lastElement().getType() == "Kanone"){
				this.deck.add(new Card(c, "Soldat"));
			}else if(this.deck.lastElement().getType() == "Soldat"){
				this.deck.add(new Card(c, "Reiter"));
			}else if(this.deck.lastElement().getType() == "Reiter"){
				this.deck.add(new Card(c, "Kanone"));
			}
		}
		Collections.shuffle(this.deck);
		this.bonus.push(20);
		this.bonus.push(15);
		this.bonus.push(12);
		this.bonus.push(10);
		this.bonus.push(8);
		this.bonus.push(6);
		this.bonus.push(4);
	}
	public int calculateBonus(){
		int bo = this.bonus.pop();
		if(this.bonus.isEmpty()){
			this.bonus.push(bo + 5);
		}
		return bo;
	}
	public void drawCard(Player pl) throws YouCannotDrawANewCardException{
		pl.drawNewCard(deck.pop());
	}
	public boolean exchangeCards(Player pl) throws NotEnoughCardsToExchangeException{
		if(pl.getCards().size()<3){
			throw new NotEnoughCardsToExchangeException();
		}
		int i = pl.getCards().size() - 1;
		Stack<Card> kanonen = new Stack<Card>();
		Stack<Card> reiter = new Stack<Card>();
		Stack<Card> soldaten = new Stack<Card>();
		Stack<Card> joker = new Stack<Card>();
		while(i>=0){
			if(pl.getCards().get(i).getType() == "Kanone"){
				kanonen.push(pl.getCards().get(i));
			}
			if(pl.getCards().get(i).getType() == "Reiter"){
				reiter.push(pl.getCards().get(i));
			}
			if(pl.getCards().get(i).getType() == "Soldat"){
				soldaten.push(pl.getCards().get(i));
			}
			if(pl.getCards().get(i).getType() == "Joker"){
				joker.push(pl.getCards().get(i));
			}
			i--;
		}
		if(kanonen.size() >= 2){
			if(kanonen.size() >= 3){
				returnCards(kanonen.pop(), kanonen.pop(), kanonen.pop(), pl);
				return true;
			}else if(joker.size()>0){
				returnCards(kanonen.pop(), kanonen.pop(), joker.pop(), pl);
				return true;
			}
		}
		if(reiter.size() >=2){
			if(reiter.size() >= 3){
				returnCards(reiter.pop(), reiter.pop(), reiter.pop(), pl);
				return true;
			}else if(joker.size()>0){
				returnCards(reiter.pop(), reiter.pop(), joker.pop(), pl);
				return true;
			}
		}
		if(soldaten.size() >=2){
			if(soldaten.size() >= 3){
				returnCards(soldaten.pop(), soldaten.pop(), soldaten.pop(), pl);
				return true;
			}else if(joker.size()>0){
				returnCards(soldaten.pop(), soldaten.pop(), soldaten.pop(), pl);
				return true;
			}
		}
		if(kanonen.size()>0){
			if(soldaten.size()>0){
				if(reiter.size()>0){
					returnCards(kanonen.pop(), reiter.pop(), soldaten.pop(), pl);
					return true;
				}else if(joker.size()>0){
					returnCards(kanonen.pop(), joker.pop(), soldaten.pop(), pl);
					return true;
				}
			}else if(joker.size()>0){
				if(reiter.size()>0){
					returnCards(kanonen.pop(), reiter.pop(), joker.pop(), pl);
					return true;
				}
			}
		}else if(joker.size()>0){
			if(reiter.size()>0&&soldaten.size()>0){
				returnCards(joker.pop(), reiter.pop(), soldaten.pop(), pl);
				return true;
			}
		}
		return false;
	}
	private void putBackCard(Player pl, Card c){
		pl.removeCard(c);
	}
	public Stack<Card> getCards(){
		return this.deck;
	}
	public Stack<Integer> getBonusList(){
		return this.bonus;
	}
	
	public void returnCards(Card card1, Card card2, Card card3, Player pl){
		this.deck.push(card1);
		putBackCard(pl, card1);
		this.deck.push(card2);
		putBackCard(pl, card2);
		this.deck.push(card3);
		putBackCard(pl, card3);
		Collections.shuffle(this.deck);
	}
	
}
