package server.logic.data.cards;

import exceptions.PlayerNotExsistInGameException;
import exceptions.RemoteExceptionPlayerNotFound;
import interfaces.data.IPlayer;
import interfaces.data.cards.ICardDeck;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

import server.logic.Game;
import server.logic.data.Country;
import server.logic.data.Player;
import exceptions.NotEnoughCardsToExchangeException;

public class CardDeck extends UnicastRemoteObject implements ICardDeck {
	private final Stack<Card> deck = new Stack<Card>();
	private final Stack<Integer> bonus = new Stack<Integer>();
	private final Game game;
	public CardDeck(final List<Country> arrayList, final Game game) throws RemoteException{
		this.game = game;
        builtDeck(arrayList);

	}
	
	private void builtDeck(List<Country> cos) throws RemoteException{
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
	public int calculateBonus() throws RemoteException{
		int bo = this.bonus.pop();
		if(this.bonus.isEmpty()){
			this.bonus.push(bo + 5);
		}
		return bo;
	}
	public void drawCard(Player pl) throws RemoteException{
		pl.drawNewCard(deck.pop());
	}
	public boolean exchangeCards(IPlayer player) throws NotEnoughCardsToExchangeException, RemoteException{
        Player pl;
        try {
            pl = game.getPlayer(player.getName());
        }catch (PlayerNotExsistInGameException e){
            throw new RemoteExceptionPlayerNotFound();
        }

        if(pl.getCards().size()<3){
            throw new NotEnoughCardsToExchangeException();
        }
		int i = pl.getCards().size() - 1;
		Stack<Card> kanonen     = new Stack<Card>();
		Stack<Card> reiter      = new Stack<Card>();
		Stack<Card> soldaten    = new Stack<Card>();
		Stack<Card> joker       = new Stack<Card>();
		while(i>=0){
			if(pl.getCards().get(i).getType() == "Kanone"){
				kanonen.push(pl.getCardsReal().get(i));
			}
			if(pl.getCards().get(i).getType() == "Reiter"){
				reiter.push(pl.getCardsReal().get(i));
			}
			if(pl.getCards().get(i).getType() == "Soldat"){
				soldaten.push(pl.getCardsReal().get(i));
			}
			if(pl.getCards().get(i).getType() == "Joker"){
				joker.push(pl.getCardsReal().get(i));
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
	private void putBackCard(Player pl, Card c) throws RemoteException{
		pl.removeCard(c);
	}


	public void returnCards(Card card1, Card card2, Card card3, Player pl) throws RemoteException{
		this.deck.push(card1);
		putBackCard(pl, card1);
		this.deck.push(card2);
		putBackCard(pl, card2);
		this.deck.push(card3);
		putBackCard(pl, card3);
		Collections.shuffle(this.deck);
	}
	
}
