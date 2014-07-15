
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

import commons.interfaces.IClient;
import commons.interfaces.IGame;
import commons.interfaces.data.IPlayer;
import commons.interfaces.data.Orders.IOrder;
import commons.interfaces.data.cards.ICardDeck;
import server.ClientManager;
import server.logic.data.*;
import server.logic.data.Map;
import commons.exceptions.*;
import server.logic.data.cards.CardDeck;
import server.logic.data.orders.OrderManager;
import server.persistence.dataendpoints.PersistenceEndpoint;

/**
 * @author Jennifer Theloy,  Stefan Bieliauskas
 *
 * Klasse für ein eizelnes Spiel. Diese dient zur Spielverwaltung.
 */
public class Game extends UnicastRemoteObject implements IGame {
	
	private final Stack<Color> color = new Stack<Color>();
    /**
     * Legt die mindestanzahl an Spielern fest, die für ein Spiel erforderlich sind
     */
    private static final int minCountPlayers = 3;

    /**
     * Legt die maximalanzahl an Spielern fest, die für ein Spiel erforderlich sind
     */
    private static final int maxCountPlayers = 5;

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

    /**
     * Managed alle Clients des Spiels und damit auch alle Events des Spiels
     */
    private final ClientManager clientManager;

    /**
     * Verwaltet alle Clients, die im Moment das GameManagerGUI-Window anzeigen
     */
    private  ClientManager allGameManagerClients;
    /**
     * Name für das SPiel, erstmal aktuelles Datum
     */
    private String name;

    /**
     * Konstruktor
     * @param persistenceEndpoint Endpunkt zum speichern des spiels
     */
    public Game(PersistenceEndpoint<Game> persistenceEndpoint) throws RemoteException{
        this(persistenceEndpoint,new Map(),UUID.randomUUID());
    }

    /**
     * Konstruktor, wennd as Spiel mit einer bestimmten Karte erzeugt werden soll
     * @param persistenceEndpoint
     * @param map
     * @param id
     */
    public Game(PersistenceEndpoint<Game> persistenceEndpoint, Map map, UUID id) throws RemoteException{
    	this.map= map;
        this.persistenceEndpoint = persistenceEndpoint;
        this.id = id;
        this.color.add(Color.BLUE);
        this.color.add(Color.GREEN);
        this.color.add(Color.ORANGE);
        this.color.add(Color.RED);
        this.color.add(Color.MAGENTA);
        this.deck = new CardDeck(this.map.getCountriesReal(),this);
        this.clientManager = new ClientManager();
        ClientManager.startWatchBroadcast(this.clientManager, "Client-Broadcast-Game");
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
     * @throws commons.exceptions.GameAlreadyStartedException
     * @throws PlayerAlreadyHasAnOrderException
     */
    public void onGameStart() throws NotEnoughPlayerException, TooManyPlayerException, NotEnoughCountriesException, GameAlreadyStartedException, PlayerAlreadyHasAnOrderException,RemoteException {

        //Exception-Handling
        if (this.players.size() < Game.minCountPlayers) {
            throw new NotEnoughPlayerException(Game.minCountPlayers);
        } else if (this.players.size() > Game.maxCountPlayers) {
            throw new TooManyPlayerException(Game.maxCountPlayers);
        } else if (this.map.getCountries().size() < this.players.size()) {
            throw new NotEnoughCountriesException(this.map.getCountries().size());
        } else if (this.currentGameState != IGame.gameStates.WAITING) {
            throw new GameAlreadyStartedException();
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
     * @return Aktuelle Runde
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
     * Wird beim Spielstart aufgerufen und setzt für alle Länder genau 1 Armee
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
     * @throws commons.exceptions.PlayerNotExistInGameException
     */
    public void onPlayerDelete(final IPlayer player) throws PlayerNotExistInGameException, RemoteException{
        Player realPlayer  = this.getPlayer(player);
        this.players.remove(realPlayer);
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
            IOrder order = player.getOrder();

            if(order.isCompleted()){
                return player;
            }
        }
        return null;
    }

    /**
     * Gibt den Spieler zum angegebenen Namen zurück
     * @param name - Name des gesuchten Spielers
     * @return Spieler
     * @throws commons.exceptions.PlayerNotExistInGameException Wenn Spieler nicht gefunden wird
     * @throws RemoteException
     */
    public Player getPlayer(final String name) throws PlayerNotExistInGameException, RemoteException{
        for (Player player: players){
            if(player.getName().equals(name)){
                return player;
            }
        }
        throw new PlayerNotExistInGameException(name);
    }

    /**
     * Gibt den Spieler zum angegebenen IPlayer zurück
     * @param otherPlayer - Name des gesuchten Spielers
     * @return Spieler
     * @throws commons.exceptions.PlayerNotExistInGameException Wenn Spieler nicht gefunden wi
     *
     */
    public Player getPlayer(final IPlayer otherPlayer) throws PlayerNotExistInGameException {
        for (Player player: players){
            if(player.equals(otherPlayer)){
                return player;
            }
        }
        throw new PlayerNotExistInGameException(name);
    }

    /**
     * Setzt die aktuelle Runde
     * @param r aktuelle Runde
     */
    public void setCurrentRound(Round r) {
        this.currentRound = r;
    }

    /**
     * Setzt den aktuellen gameState
     *
     * @param s aktueller GameState
     */
    public void setCurrentGameState(IGame.gameStates s) {
        this.currentGameState = s;
    }

    /**
     * Für alle Spieler der Spielerliste hinzu
     *
     * @param players Liste von Spielern
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
     * @param name Name des zu hinzuüfügenden Spielers
     * @param client Zu dem Namen dazugehöriger client
     */
    public Player addPlayer(String name, IClient client) throws GameAlreadyStartedException, PlayerNameAlreadyChooseException,RemoteException{
        if (this.getCurrentGameState() != IGame.gameStates.WAITING) {
            throw new GameAlreadyStartedException();
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
        /**
         * Andere bereits diesem Spiel begetretenen Clients aktualsieren
         */
        this.clientManager.broadcastUIUpdate(IClient.UIUpdateTypes.PLAYER);
        /**
         * Alle UI's aktualisieren die nur die auswahl der Spieler haben weil im namen des Spiels die Spieler stehen
         */
        if(this.allGameManagerClients != null){
            this.allGameManagerClients.broadcastUIUpdate(IClient.UIUpdateTypes.ALL);
        }
        return player;
    }

    /**
     * Setzt für einen Spieler einen client
     * @param player - Spieler für den der client gesetzt werden soll
     * @param client - Der client der für den Spieler gesetzt werden soll
     * @throws RemoteException
     * @throws PlayerNotExistInGameException
     */
    public void setClient(final IPlayer player, final IClient client) throws RemoteException, PlayerNotExistInGameException{
        Player realPlayer = this.getPlayer(player);
        this.clientManager.addClient(client);
        realPlayer.setClient(client);

    }


    /**
     * @return Liste der Spieler
     */
    public List<Player> getPlayers() throws RemoteException{
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
        return "Game: " + this.players.toString();
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

    /**
     * Setter für den namen des Spiels
     * @param name
     */
    public void setName(String name){
        this.name = name;
    }

    /**
     * Getter für name
     * @return
     */
    public String getName(){
        if(this.name == null ){
            return this.players.toString();
        }else {
            return this.name;
        }
    }
    public void setAllGameManagerClients(ClientManager clientManager){
        this.allGameManagerClients = clientManager;
    }
}
