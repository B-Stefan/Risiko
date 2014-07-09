package interfaces.data.cards;

import interfaces.data.IPlayer;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Collections;
import java.util.Stack;

import exceptions.NotEnoughCardsToExchangeException;

/**
 * Interface für das alle Karten im Spiel
 */
public interface ICardDeck extends Remote, Serializable {

	public boolean exchangeCards(IPlayer pl) throws NotEnoughCardsToExchangeException, RemoteException;
}
