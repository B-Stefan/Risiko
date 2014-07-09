
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
import java.lang.Thread;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.awt.Color;
import java.util.concurrent.ThreadFactory;

import interfaces.IClient;
import interfaces.IGame;
import interfaces.data.IPlayer;
import interfaces.data.cards.ICardDeck;
import server.ClientManager;
import server.logic.data.*;
import server.logic.data.Map;
import exceptions.*;
import server.logic.data.cards.CardDeck;
import server.logic.data.orders.OrderManager;
import server.persistence.dataendpoints.PersistenceEndpoint;

/**
 * @author Jennifer Theloy,  Stefan Bieliauskas
 *
 * Klasse für ein eizelnes Spiel. Diese dient zur Spielverwaltung.
 */
public class Game extends UnicastRemoteObject implements IGame {
	
	private Stack<Color> color = new Stack<Color>();
    /**
     * Legt die mindestanzahl an Spielern fest, die für ein Spiel erforderlich sind
     */
    public static final int minCountPlayers = 3;

    /**
     * Legt die maximalanzahl an Spielern fest, die für ein Spiel erforderlich sind
     */
    public static final int maxCountPlayers = 5;

    /**
     * Representiert die Karte des Spiels
     */
    private final Map map;

    /**
     * Listet alle Spieler auf, die aktiv am Spiel teilnehmen.
     */
    private final List<Player> players = new ArrayList<Player>();

    /**
     * The current gamePanels state, default => WAITING
     */
    private IGame.gameStates currentGameState = IGame.gameStates.WAITING;

    /**
     * Contains the current round , default null
     */
    private  Round currentRound;

    /**
     * Name für das SPiel, erstmal aktuelles Datum
     */
    private final String name = new Date().toString();;

    /**
     * Die UUID für das Spiel
     */
    private final UUID id;

    /**
     * Der server.GameManager, der für die Persistenz verwendet werden soll.
     */
    private final PersistenceEndpoint<Game> persistenceEndpoint;
    /**
     *
     * @param persistenceEndpoint - Der Manager, der zur Speicherung des Spiels verwnedet werden soll
     */
    private final CardDeck deck;

    private final ClientManager clientManager;

    /**
     * Konstruktor
     * @param persistenceEndpoint Endpunkt zum speichern des spiels
     */
    public Game(PersistenceEndpoint<Game> persistenceEndpoint) throws RemoteException{
        this(persistenceEndpoint,new Map());
    }

    /**
     * Konstruktor, wennd as Spiel mit einer bestimmten Karte erzeugt werden soll
     * @param persistenceEndpoint
     * @param map
     */
    public Game(PersistenceEndpoint<Game> persistenceEndpoint, Map map) throws RemoteException{
    	this.map= map;
        this.persistenceEndpoint = persistenceEndpoint;
        this.id = UUID.randomUUID();
        this.color.add(Color.BLUE);
        this.color.add(Color.GREEN);
        this.color.add(Color.ORANGE);
        this.color.add(Color.RED);
        this.color.add(Color.MAGENTA);
        this.deck = new CardDeck(this.map.getCountriesReal(),this);
        this.clientManager = new ClientManager();
        ClientManager.startWatchBroadcast(this.clientManager);
        Thread t = new Thread(this.clientManager);
        t.start();

    }

    /**
     * Gibt das Card-Deck des Spiel zurück
     * @return Das Karten-Deck, das die Funktion für die Ereigniskarten bereitstellt
     * @throws RemoteException
     */
    public ICardDeck getDeck() throws RemoteException{
    	return this.deck;
    }

    /**
     * Startet das Spiel
     * @throws NotEnoughPlayerException
     * @throws TooManyPlayerException
     * @throws NotEnoughCountriesException
     * @throws GameAllreadyStartedException
     * @throws PlayerAlreadyHasAnOrderException
     */
    public void onGameStart() throws NotEnoughPlayerException, TooManyPlayerException, NotEnoughCountriesException, GameAllreadyStartedException, PlayerAlreadyHasAnOrderException,RemoteException {

        //Exception-Handling
        if (this.players.size() < Game.minCountPlayers) {
            throw new NotEnoughPlayerException(Game.minCountPlayers);
        } else if (this.players.size() > Game.maxCountPlayers) {
            throw new TooManyPlayerException(Game.maxCountPlayers);
        } else if (this.map.getCountries().size() < this.players.size()) {
            throw new NotEnoughCountriesException(this.map.getCountries().size());
        } else if (this.currentGameState != IGame.gameStates.WAITING) {
            throw new GameAllreadyStartedException();
        }

        //Spielstart
        this.distributeCountries();
        this.distributeColors();
        this.setDefaultArmys();
        OrderManager.createOrdersForPlayers(this.players,this,this.map);

        this.currentGameState = IGame.gameStates.RUNNING;
        this.setCurrentRound(new Round(this.deck, players, map, Turn.getDefaultStepsFirstRound(),this.clientManager));

        this.clientManager.broadcastMessage("Das Spiel wurde gestartet");
        this.clientManager.broadcastUIUpdate(IClient.UIUpdateTypes.ALL);

    }


    /**
     * Versetzt das Spiel in die nächste Runde
     * @throws ToManyNewArmysException
     * @throws RoundNotCompleteException
     * @throws GameNotStartedException
     * @throws GameIsCompletedException
     */
    public void setNextRound() throws ToManyNewArmysException, RoundNotCompleteException,GameNotStartedException, GameIsCompletedException,RemoteException{
        if (this.currentRound != null) {
            try {
                if (!this.currentRound.isComplete()) {
                    throw new RoundNotCompleteException();
                }
            }catch (RemoteException e){
                throw new RuntimeException(e);
            }
        }
        if (this.getCurrentGameState() == IGame.gameStates.WAITING){
            throw  new GameNotStartedException();
        }
        else if(this.isGameWon()){
            throw  new GameIsCompletedException();
        }
        this.currentRound = new Round(this.deck, this.players, this.map,this.clientManager);
    }

    /**
     * Gibt die Aktelle Runde des Spielers zurück
     * @return
     * @throws GameNotStartedException
     */
    public Round getCurrentRound() throws GameNotStartedException,RemoteException {
        if (this.getCurrentGameState() == IGame.gameStates.WAITING) {
            throw new GameNotStartedException();
        }
        return this.currentRound;
    }

    /**
     * Gibt den aktuellen Status des Spiels zurück
     * @return Status des Spiels
     */
    public Game.gameStates getCurrentGameState() throws RemoteException{
        return this.currentGameState;
    }


    /**
     * Verteilt die Farben an die Spiler
     */
    private void distributeColors() throws RemoteException{

        for(Player player : this.players){
            if(player.getColor() == null){
                if(this.color.peek() != null ) {
                    player.setColor(this.color.pop());
                }
                else {
                    throw new RuntimeException("Nicht geünügt Farben für alle Spieler vorhanden");
                }
            }
        }
    }
    /**
     * Verteilt die Länder beim Spielstart an alle angemeldeten Spieler.
     *
     */
    private void distributeCountries()throws RemoteException {
        /**
         * Stack, der die Länder beinhaltet, die noch zu verteilen sind
         */
        Stack<Country> countriesStack = new Stack<Country>();
        countriesStack.addAll(this.map.getCountriesReal());
        Collections.shuffle(countriesStack); // Durchmischen der Länder

        /**
         * Durchläuft die Schleife so lange, bis die Anzahl der L�nder, die noch zu verteilen sind,
         * kleiner ist, als die Anzahl der Spieler
         */
        while (!countriesStack.empty()) {

            for (Player p : players) {
                if(!countriesStack.empty()){
                    p.addCountry(countriesStack.pop());
                }
                else {
                    break;
                }
            }
        }
    }

    /**
     *
     * Wird beim Spielstart aufgerufen und setzt für alle Länder genau 1 Armee
     *
     *
     */
    private void setDefaultArmys()throws RemoteException {
        for (Player player : players) {
            for (Country country : player.getCountriesReal()) {
                //Nur machen, wenn noch keine Armee auf dem Land sitzt
                if (country.getArmySize() == 0) {
                    Army a = new Army(player);
                    try {
                        country.addArmy(a);
                    } catch (CountriesNotConnectedException e) {
                        //Kann nicht auftreten, da die diefalut-Armys zuerst keinem Land zugewiesen wurden.
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    /**
     * Wird ausgelöst, wenn durch die GUI ein Spieler das Spiel verlässt
     *
     * @param player - Player der gelöscht werden soll
     *
     * @throws exceptions.PlayerNotExsistInGameException
     */
    public void onPlayerDelete(final IPlayer player) throws PlayerNotExsistInGameException, RemoteException{
        try {
            this.players.remove(player);
        } catch (final Exception e) {
            throw new PlayerNotExsistInGameException(player);
        }
    }


    /**
     * Pürft, ob das Spiel gewonnen wurde
     * @return Wenn gewonnen true
     */
    private boolean isGameWon() throws GameNotStartedException,RemoteException{
        if(this.getCurrentGameState() == IGame.gameStates.WAITING){
            throw new GameNotStartedException();
        }
        if(this.getWinner()!=null){
            return true;
        }
        return false;
    }


    /**
     * Gibt den Gewinner zurück, der das Spiel gewonnen hat
     * Wenn keiner gewonnen hat gibt die Methode null zurück
     * @return Sieger des Spiels
     */
    public Player getWinner () throws RemoteException{
        for(Player player : players){
            if(player.getOrder().isCompleted()){
                return player;
            }
        }
        return null;
    }

    /**
     * Gibt den Spieler zum angegebenen Namen zurück
     * @param name - Name des gesuchten Spielers
     * @return Spieler
     * @throws PlayerNotExsistInGameException Wenn Spieler nicht gefunden wird
     * @throws RemoteException
     */
    public Player getPlayer(final String name) throws PlayerNotExsistInGameException, RemoteException{
        for (Player player: players){
            if(player.getName().equals(name)){
                return player;
            }
        }
        throw new PlayerNotExsistInGameException(name);
    }

    /**
     * Gibt den Spieler zum angegebenen IPlayer zurück
     * @param otherPlayer - Name des gesuchten Spielers
     * @return Spieler
     * @throws PlayerNotExsistInGameException Wenn Spieler nicht gefunden wird
     * @throws RemoteException
     */
    public Player getPlayer(final IPlayer otherPlayer) throws PlayerNotExsistInGameException{
        for (Player player: players){
            if(player.equals(otherPlayer)){
                return player;
            }
        }
        throw new PlayerNotExsistInGameException(name);
    }

    /**
     * Setzt die aktuelle Runde
     *
     * @param r
     */
    public void setCurrentRound(Round r) {
        this.currentRound = r;
    }

    /**
     * Setzt den aktuellen gameState
     *
     * @param s
     */
    public void setCurrentGameState(IGame.gameStates s) {
        this.currentGameState = s;
    }

    /**
     * Für alle Spieler der Spielerliste hinzu
     *
     * @param players
     */
    public void addPlayers(List<Player> players) {
        this.players.addAll(players);
    }

    /**
     * Gibt die Karte des Spiels zurück
     *
     * @return
     */
    public Map getMap() throws RemoteException{
        return map;
    }

    /**
     * Fügt einen neuen Spieler aufgrund des namens hinzu
     *
     * @param name
     */
    public Player addPlayer(String name, IClient client) throws GameAllreadyStartedException, PlayerNameAlreadyChooseException,RemoteException{
        if (this.getCurrentGameState() != IGame.gameStates.WAITING) {
            throw new GameAllreadyStartedException();
        }

        for (Player player: this.players){
            if (player.getName().equals(name)){
                throw new PlayerNameAlreadyChooseException(name);
            }
        }
        this.clientManager.addClient(client);
        Player player = new Player(name);
        player.setClient(client);

        this.players.add(player);
        this.clientManager.broadcastUIUpdate(IClient.UIUpdateTypes.PLAYER);
        return player;
    }

    /**
     * @return Liste der Spieler
     */
    public List<? extends IPlayer> getPlayers() throws RemoteException{
        return this.players;
    }

    /**
     * @return Liste der Spieler
     */
    public List<Player> getPlayersReal(){
        return this.players;
    }

    /**
     * Getter für die ID
     * @return UUID des Spiels
     */
    public UUID getId() throws RemoteException{
        return id;
    }

    /**
     * Gibt das Spiel als String aus.
     * @return Gibt "IGame" zurück
     */
    @Override
    public String toString() {
        return "Game" + this.name;
    }

    /**
     * Speicher das Spiel ab
     *
     */
    public boolean save () throws PersistenceEndpointIOException,RemoteException{
        return this.persistenceEndpoint.save(this);
    }

    /**
     * ToString Methode
     */
    public String toStringRemote() throws RemoteException{
        return this.toString();
    }
}
