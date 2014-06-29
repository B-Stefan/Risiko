package logic;
import exceptions.TooManyPlayerException;
import interfaces.IRound;
import interfaces.ITurn;
import interfaces.data.IMap;
import interfaces.data.IPlayer;
import interfaces.data.cards.ICardDeck;
import logic.data.*;
import logic.data.cards.CardDeck;
import exceptions.RoundCompleteException;
import exceptions.ToManyNewArmysException;
import exceptions.TurnNotCompleteException;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

public class Round implements IRound {
	/**
	 * Der Spieler, der am Zug ist
	 */
	private IPlayer currentPlayer;
    private IMap map;
	private Queue<IPlayer> players;
    private Queue<Turn.steps> turnSteps;
    private ITurn currentTurn;
    private ICardDeck deck;
	

	public Round(ICardDeck deck, final List<IPlayer> players,final IMap map, final Queue<Turn.steps> steps){
		this.deck = deck;
		this.map = map;
        this.players = new LinkedBlockingQueue<IPlayer>(players);
        this.turnSteps = steps;
        try {
            this.setNextTurn();
        }catch (ToManyNewArmysException|TurnNotCompleteException|RoundCompleteException e){
            throw  new RuntimeException(e);
        }

	}
    public Round(ICardDeck deck2, final List<IPlayer> players2,final IMap map2){
        this(deck2, players2,map2, ITurn.getDefaultSteps());
    }
    /**
     * Setzt den nächsten Spieler als aktuellen Spieler
     * @throws RoundCompleteException
     */
	public void setCurrentPlayer() throws RoundCompleteException{
        if(this.players.size() == 0){
            throw new RoundCompleteException();
        }
		this.currentPlayer = players.poll();
	}
	/**
	 * Getter für den aktuellen Spieler
	 * @return currentPayler: gibt aktuellen Spieler
	 */
	public IPlayer getCurrentPlayer(){
		return this.currentPlayer;
	}

    /**
     * Erzeugt und setzt den nächsten Turn, wenn erlaubt
     * @throws ToManyNewArmysException
     * @throws TurnNotCompleteException
     * @throws RoundCompleteException
     */
	public void setNextTurn() throws ToManyNewArmysException, TurnNotCompleteException, RoundCompleteException{
        if(this.getCurrentTurn() != null){
            if(!this.getCurrentTurn().isComplete()){
                throw new TurnNotCompleteException(this.getCurrentTurn());
            }
            this.deck.drawCard(this.currentTurn.getPlayer());
        }
        
        this.setCurrentPlayer();
        this.currentTurn = new Turn(this.getCurrentPlayer(), this.map, this.turnSteps, this.deck );

	}


    /**
     * Pürft, ob die Runde komplett abgeschlossen ist, wenn ja True
     * @return True wenn Runde abgeschlossen ist
     * @throws ToManyNewArmysException
     */
    public boolean isComplete() throws ToManyNewArmysException{
        if(this.getCurrentTurn() == null){
            return false;
        }
        else if (players.size() == 0 && this.getCurrentTurn().isComplete() ){
           return true;
        }
        return false;
    }

    /**
     * GIbt den aktuellen Turn zurück
     * @return
     */
    public ITurn getCurrentTurn(){
        return this.currentTurn;
    }

    /**
     * TOString mehtod
     * @return
     */
    @Override
    public String toString (){
        return "Round";
    }
}
