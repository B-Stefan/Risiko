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


}
