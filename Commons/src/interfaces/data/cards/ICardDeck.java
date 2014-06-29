package interfaces.data.cards;

import interfaces.data.IPlayer;
import java.util.Collections;
import java.util.Stack;

import exceptions.NotEnoughCardsToExchangeException;

/**
 * Created by Stefan on 29.06.14.
 */
public interface ICardDeck {
	public int calculateBonus();
	
	public void drawCard(IPlayer p);
	
	public boolean exchangeCards(IPlayer pl) throws NotEnoughCardsToExchangeException;

	public Stack<ICard> getCards();
	
	public Stack<Integer> getBonusList();
	
	public void returnCards(ICard card1, ICard card2, ICard card3, IPlayer pl);
}
