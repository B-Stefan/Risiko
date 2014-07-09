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

package server.logic;
import interfaces.IClient;
import interfaces.IRound;
import interfaces.data.IPlayer;
import server.ClientManager;
import server.logic.data.*;
import server.logic.data.Map;
import server.logic.data.cards.CardDeck;
import exceptions.NotYourTurnException;
import exceptions.RoundCompleteException;
import exceptions.ToManyNewArmysException;
import exceptions.TurnNotCompleteException;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

public class Round extends UnicastRemoteObject implements IRound {


    /**
     * Karte auf der das Spiel stattfindet
     */
    private final Map map;
    /**
     * Reihnfolge der Spieler im Spiel
     */
    private final Queue<Player> players;
    /**
     * Welche Steps dürfen in dieser Runde durchgeführt werden
     */
    private final Queue<Turn.steps> turnSteps;

    /**
     * Das Card-Deck, das für das gesamte Spiel verwendet wird
     */
    private final CardDeck deck;
    /**
     * Möglichkeit Rückmeldung an den Client zu geben
     */
    private final ClientManager clientManager;

    /**
     * Der Spieler, der am Zug ist
     */
	private Player currentPlayer;

    /**
     * Aktueller Zug den ein Spieler ausführt
     */
    private Turn currentTurn;


    /**
     * Bildet eine Runde im Spiel ab.
     * Eine Runde besteht darin, dass jeder Spieler seinen Turn vollendet.
     *
     * @param deck Der Kartenstapel für das Spiel
     * @param players Die Spieler in der entsprechenden Reihnfolge
     * @param map Die Karte auf dem das Spiel stattfindet
     * @param steps Die erlaubten Steps in dieser Runde
     * @param clientManager Die Möglichkeit den Spielern Rückmeldung zu geben
     * @throws RemoteException
     */
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

    /**
     * Bildet eine Runde im Spiel ab.
     * Eine Runde besteht darin, dass jeder Spieler seinen Turn vollendet.
     * @param deck Der Kartenstapel für das Spiel
     * @param players Die Spieler in der entsprechenden Reihnfolge
     * @param map Die Karte auf dem das Spiel stattfindet
     * @param clientManager Die Möglichkeit den Spielern Rückmeldung zu geben
     * @throws RemoteException
     */
    public Round(CardDeck deck, final List<Player> players,final Map map,final ClientManager clientManager) throws RemoteException{
        this(deck, players,map, Turn.getDefaultSteps(),clientManager);
    }

    /**
     * Setzt den nächsten Spieler als aktuellen Spieler
     * @throws RoundCompleteException
     * @throws RemoteException
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
     * @param clientPlayer Spieler der die Aktion ausführen möchte
     * @throws ToManyNewArmysException
     * @throws TurnNotCompleteException
     * @throws RoundCompleteException
     * @throws RemoteException
     * @throws NotYourTurnException
     */
	public void setNextTurn(IPlayer clientPlayer) throws ToManyNewArmysException, TurnNotCompleteException, RoundCompleteException,RemoteException, NotYourTurnException{
        if(!this.getCurrentPlayer().getColor().equals(clientPlayer.getColor())){
        	throw new NotYourTurnException();
        }
		this.setNextTurn();
	}

    /**
     * Erzeugt und setzt den nächsten Turn, wenn erlaubt
     * @throws ToManyNewArmysException
     * @throws TurnNotCompleteException
     * @throws RoundCompleteException 
     */
	public void setNextTurn() throws ToManyNewArmysException, RoundCompleteException,RemoteException, TurnNotCompleteException{
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
