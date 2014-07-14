 

/*
 * RISIKO-JAVA - Game, Copyright 2014  Jennifer Theloy, Stefan Bieliauskas  -  All Rights Reserved.
 * Hochschule Bremen - University of Applied Sciences
 *
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 * Contact:
 *     Jennifer Theloy: jTheloy@stud.hs-bremen.de
 *     Stefan Bieliauskas: sBieliauskas@stud.hs-bremen.de
 *
 * Web:
 *     https://github.com/B-Stefan/Risiko
 *
 */

package server.logic.data.cards;

import exceptions.PlayerNotExistInGameException;
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
	/**
	 * das Deck mit allen Karten im Spiel
	 */
	private final Stack<Card> globalDeck = new Stack<Card>();
	/**
	 * Eine Liste, in der die jeweiligen Boni stehen, die man dafür bekommt Karten einzutauschen
	 */
	private final Stack<Integer> bonus = new Stack<Integer>();
	/**
	 * das aktuelle Game
	 */
	private final Game game;
	/**
	 * Konstruktor
	 * @param arrayList Liste alle Länder
	 * @param game das aktuelle Spiel
	 * @throws RemoteException
	 */
	public CardDeck(final List<Country> arrayList, final Game game) throws RemoteException{
		this.game = game;
        builtDeck(arrayList);

	}
	/**
	 * Das Deck wird aufgefüllt mit Karten und die Bonusliste wird gesetzt
	 * @param cos Liste aller Länder
	 * @throws RemoteException
	 */
	private void builtDeck(List<Country> cos) throws RemoteException{
		for(Country c : cos){
			if(globalDeck.isEmpty()){
				this.globalDeck.add(new Card(c, "Kanone"));
			}else if(this.globalDeck.lastElement().getType() == "Kanone"){
				this.globalDeck.add(new Card(c, "Soldat"));
			}else if(this.globalDeck.lastElement().getType() == "Soldat"){
				this.globalDeck.add(new Card(c, "Reiter"));
			}else if(this.globalDeck.lastElement().getType() == "Reiter"){
				this.globalDeck.add(new Card(c, "Kanone"));
			}
		}
		this.globalDeck.add(new Card("Joker"));
		this.globalDeck.add(new Card("Joker"));
		Collections.shuffle(this.globalDeck);
		this.bonus.push(20);
		this.bonus.push(15);
		this.bonus.push(12);
		this.bonus.push(10);
		this.bonus.push(8);
		this.bonus.push(6);
		this.bonus.push(4);
	}
	/**
	 * Der nächste Bonus wird ausgegeben
	 * @return
	 * @throws RemoteException
	 */
	public int calculateBonus() throws RemoteException{
		int bo = this.bonus.pop();
		if(this.bonus.isEmpty()){
			this.bonus.push(bo + 5);
		}
		return bo;
	}
	/**
	 * Zum ziehen einer Karte aus dem Deck
	 * @param pl der Spieler, der die Karte zieht
	 * @throws RemoteException
	 */
	
	public void drawCard(Player pl) throws RemoteException{
		pl.drawNewCard(globalDeck.pop());
	}
	/**
	 * Zum Austauschen der Karten gegen einen Armeen Bonus
	 */
	public boolean exchangeCards(IPlayer player) throws NotEnoughCardsToExchangeException, RemoteException{
        Player pl;
        try {
            pl = game.getPlayer(player.getName());
        }catch (PlayerNotExistInGameException e){
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
	/**
	 * entfehrnt die jeweillige Karze aus dem Deck des Spielers
	 * @param pl der Spieler
	 * @param c die Karte, die zurück gegeben werden soll
	 * @throws RemoteException
	 */
	private void putBackCard(Player pl, Card c) throws RemoteException{
		pl.removeCard(c);
	}

	/**
	 * legt die jeweilligen Karten zurück ins Deck
	 * @param card1
	 * @param card2
	 * @param card3
	 * @param pl
	 * @throws RemoteException
	 */
	public void returnCards(Card card1, Card card2, Card card3, Player pl) throws RemoteException{
		this.globalDeck.push(card1);
		putBackCard(pl, card1);
		this.globalDeck.push(card2);
		putBackCard(pl, card2);
		this.globalDeck.push(card3);
		putBackCard(pl, card3);
		Collections.shuffle(this.globalDeck);
	}
	
}
