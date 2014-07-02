package server.logic;
import interfaces.IClient;
import interfaces.IRound;
import server.ClientManager;
import server.logic.data.*;
import server.logic.data.Map;
import server.logic.data.cards.CardDeck;
import exceptions.RoundCompleteException;
import exceptions.ToManyNewArmysException;
import exceptions.TurnNotCompleteException;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

public class Round extends UnicastRemoteObject implements IRound {


    private final Map map;
    private final Queue<Player> players;
    private final Queue<Turn.steps> turnSteps;
    private final CardDeck deck;
    private final ClientManager clientManager;

    /**
     * Der Spieler, der am Zug ist
     */
	private Player currentPlayer;
    private Turn currentTurn;

	

	public Round(CardDeck deck, final List<Player> players,final Map map, final Queue<Turn.steps> steps, final ClientManager clientManager) throws RemoteException{
		this.deck = deck;
		this.map = map;
        this.players = new LinkedBlockingQueue<Player>(players);
        this.turnSteps = steps;
        this.clientManager = clientManager;
        try {
            this.setNextTurn();
        }catch (ToManyNewArmysException|TurnNotCompleteException|RoundCompleteException e){
            throw  new RuntimeException(e);
        }

	}
    public Round(CardDeck deck, final List<Player> players,final Map map,final ClientManager clientManager) throws RemoteException{
        this(deck, players,map, Turn.getDefaultSteps(),clientManager);
    }
    /**
     * Setzt den nächsten Spieler als aktuellen Spieler
     * @throws RoundCompleteException
     */
	public void setCurrentPlayer() throws RoundCompleteException,RemoteException {
        if(this.players.size() == 0){
            throw new RoundCompleteException();
        }
		this.currentPlayer = players.poll();
	}
	/**
	 * Getter für den aktuellen Spieler
	 * @return currentPayler: gibt aktuellen Spieler
	 */
	public Player getCurrentPlayer() throws RemoteException{
		return this.currentPlayer;
	}

    /**
     * Erzeugt und setzt den nächsten Turn, wenn erlaubt
     * @throws ToManyNewArmysException
     * @throws TurnNotCompleteException
     * @throws RoundCompleteException
     */
	public void setNextTurn() throws ToManyNewArmysException, TurnNotCompleteException, RoundCompleteException,RemoteException{
        if(this.getCurrentTurn() != null){
            if(!this.getCurrentTurn().isComplete()){
                throw new TurnNotCompleteException(this.getCurrentTurn());
            }
            if(this.currentTurn.getTakeOverSucess()){
            	this.deck.drawCard(this.currentTurn.getPlayer());
            }
        }
        
        this.setCurrentPlayer();
        this.currentTurn = new Turn(this.currentPlayer, this.map, this.turnSteps, this.deck,clientManager );
        this.clientManager.broadcastUIUpdate(IClient.UIUpdateTypes.PLAYER);
	}


    /**
     * Pürft, ob die Runde komplett abgeschlossen ist, wenn ja True
     * @return True wenn Runde abgeschlossen ist
     * @throws ToManyNewArmysException
     */
    public boolean isComplete() throws ToManyNewArmysException,RemoteException{
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
    public Turn getCurrentTurn() throws RemoteException{
        return this.currentTurn;
    }

    /**
     * TOString mehtod
     * @return
     */
    @Override
    public String toString () {
        return "Round";
    }
    /**
     * ToString Methode, die remote aufgerufen werden kann
     */
    public String toStringRemote() throws RemoteException{
        return this.toString();
    }
}
