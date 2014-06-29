package logic;
import exceptions.TooManyPlayerException;
import interfaces.IRound;
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
	private Player currentPlayer;
    private logic.data.Map map;
	private Queue<Player> players;
    private Queue<Turn.steps> turnSteps;
    private Turn currentTurn;
    private CardDeck deck;
	

	public Round(CardDeck deck, final List<Player> p,final logic.data.Map map, final Queue<Turn.steps> steps){
		this.deck = deck;
		this.map = map;
        this.players = new LinkedBlockingQueue<Player>(p);
        this.turnSteps = steps;
        try {
            this.setNextTurn();
        }catch (ToManyNewArmysException|TurnNotCompleteException|RoundCompleteException e){
            throw  new RuntimeException(e);
        }

	}
    public Round(CardDeck deck, final List<Player> p,final logic.data.Map map){
        this(deck, p,map, Turn.getDefaultSteps());
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
	public Player getCurrentPlayer(){
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
    public Turn getCurrentTurn(){
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
