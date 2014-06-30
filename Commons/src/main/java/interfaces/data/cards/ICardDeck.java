package interfaces.data.cards;

import interfaces.data.IPlayer;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Collections;
import java.util.Stack;

import exceptions.NotEnoughCardsToExchangeException;

/**
 * Created by Stefan on 29.06.14.
 */
public interface ICardDeck extends Remote, Serializable {
	public int calculateBonus()throws RemoteException;
	
	public void drawCard(IPlayer p) throws RemoteException;
	
	public boolean exchangeCards(IPlayer pl) throws NotEnoughCardsToExchangeException, RemoteException;

	public Stack<ICard> getCards()throws RemoteException;
	
	public Stack<Integer> getBonusList()throws RemoteException;
	
	public void returnCards(ICard card1, ICard card2, ICard card3, IPlayer pl)throws RemoteException;
}
