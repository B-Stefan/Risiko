package main.java.logic;

import java.util.*;
import java.awt.Color;

import main.java.GameManager;
import main.java.logic.data.*;
import main.java.logic.data.Map;
import main.java.logic.exceptions.PlayerNotExsistInGameException;
import main.java.logic.exceptions.*;
import main.java.logic.data.orders.OrderManager;
import main.java.persistence.dataendpoints.PersistenceEndpoint;
import main.java.persistence.exceptions.PersistenceEndpointIOException;

/**
 * @author Jennifer Theloy, Thu Nguyen, Stefan Bieliauskas
 *
 * Klasse für ein eizelnes Spiel. Diese dient zur Spielverwaltung.
 */
public class Game {
	
	private Stack<Color> color = new Stack<Color>();
    /**
     * Legt die mindestanzahl an Spielern fest, die für ein Spiel erforderlich sind
     */
    public static final int minCountPlayers = 3;

    /**
     * Legt die maximalanzahl an Spielern fest, die für ein Spiel erforderlich sind
     */
    public static final int maxCountPlayers = 5;

    public static enum gameStates {
        WAITING, // Wait for start input
        RUNNING, // The gamePanels passes through the rounds
        FINISHED // One player finished his order
    }

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
    private  gameStates currentGameState = gameStates.WAITING;

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
     * Der GameManager, der für die Persistenz verwendet werden soll.
     */
    private final PersistenceEndpoint<Game> persistenceEndpoint;
    /**
     *
     * @param persistenceEndpoint - Der Manager, der zur Speicherung des Spiels verwnedet werden soll
     */
    public Game(PersistenceEndpoint<Game> persistenceEndpoint) {
        this(persistenceEndpoint,new Map());
    }

    /**
     * Konstruktor, wenn das Spiel mit einer bestimmten Karte erstellt werden soll
     * @param persistenceEndpoint Endpunkt zum speichern des spiels
     * @param map Karte die für das Spiel verwnedet werden soll
     */
    public Game(PersistenceEndpoint<Game> persistenceEndpoint, Map map){
       this.map= map;
       this.persistenceEndpoint = persistenceEndpoint;
       this.id = UUID.randomUUID();
       this.color.add(Color.BLUE);
       this.color.add(Color.GREEN);
       this.color.add(Color.ORANGE);
       this.color.add(Color.RED);
       this.color.add(Color.MAGENTA);
    }

    /**
     * Startet das Speil, sodass der Spielstatus und co aktualisiert werden
     *
     * @throws main.java.logic.exceptions.NotEnoughPlayerException
     */
    public void onGameStart() throws NotEnoughPlayerException, TooManyPlayerException, NotEnoughCountriesException, GameAllreadyStartedException, PlayerAlreadyHasAnOrderException {

        //Exception-Handling
        if (this.players.size() < Game.minCountPlayers) {
            throw new NotEnoughPlayerException(Game.minCountPlayers);
        } else if (this.players.size() > Game.maxCountPlayers) {
            throw new TooManyPlayerException(Game.maxCountPlayers);
        } else if (this.map.getCountries().size() < this.players.size()) {
            throw new NotEnoughCountriesException(this.map.getCountries().size());
        } else if (this.currentGameState != gameStates.WAITING) {
            throw new GameAllreadyStartedException();
        }

        //Spielstart
        this.distributeCountries();
        this.setDefaultArmys();
        OrderManager.createOrdersForPlayers(this.getPlayers(),this);

        this.currentGameState = gameStates.RUNNING;
        this.setCurrentRound(new Round(players, map, Turn.getDefaultStepsFirstRound()));


    }


    public void setNextRound() throws ToManyNewArmysException, RoundNotCompleteException,GameNotStartedException, GameIsCompletedException{
        if (this.currentRound != null) {
            if (!this.currentRound.isComplete()) {
                throw new RoundNotCompleteException();
            }
        }
        if (this.getCurrentGameState() == gameStates.WAITING){
            throw  new GameNotStartedException();
        }
        else if(this.isGameWon()){
            throw  new GameIsCompletedException();
        }
        this.currentRound = new Round(this.players, this.map);
    }

    /**
     * Gibt die aktuelle Runde des Spiels zurück
     *
     * @return
     */
    public Round getCurrentRound() throws GameNotStartedException {
        if (this.getCurrentGameState() == gameStates.WAITING) {
            throw new GameNotStartedException();
        }
        return this.currentRound;
    }

    /**
     * Gibt den aktuellen status des Games zurück
     *
     * @return
     */
    public gameStates getCurrentGameState() {
        return this.currentGameState;
    }


    /**
     * Verteilt die Länder beim Spielstart an alle angemeldeten Spieler.
     *
     */
    private void distributeCountries() {
        /**
         * Stack, der die Länder beinhaltet, die noch zu verteilen sind
         */
        Stack<Country> countriesStack = new Stack<Country>();
        countriesStack.addAll(this.map.getCountries());
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
    private void setDefaultArmys() {
        for (Player player : players) {
            for (Country country : player.getCountries()) {
                //Nur machen, wenn noch keine Armee auf dem Land sitzt
                if (country.getArmyList().size() == 0) {
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
     * @throws main.java.logic.exceptions.PlayerNotExsistInGameException
     */
    public void onPlayerDelete(final Player player) throws PlayerNotExsistInGameException {
        try {
            this.players.remove(player);
        } catch (final Exception e) {
            throw new PlayerNotExsistInGameException(player);
        }
    }


    /**
     * Wird ausgelöst, sobald über die GUI ein neuer Spieler hinzugefügt wird.
     *
     * @param name - Der Name des neuen Spielers
     */
    public void onPlayerAdd(final String name) throws GameAllreadyStartedException {

        if (this.getCurrentGameState() != gameStates.WAITING) {
            throw new GameAllreadyStartedException();
        } else {
            Player newPlayer = new Player(name, this.color.pop());
            this.addPlayer(newPlayer);
        }
    }

    /**
     * Pürft, ob das Spiel gewonnen wurde
     * @return Wenn gewonnen true
     */
    private boolean isGameWon() throws GameNotStartedException{
        if(this.getCurrentGameState() == gameStates.WAITING){
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
    public Player getWinner (){
        for(Player player : players){
            if(player.getOrder().isCompleted()){
                return player;
            }
        }
        return null;
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
    public void setCurrentGameState(gameStates s) {
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
    public main.java.logic.data.Map getMap() {
        return map;
    }

    /**
     * Für dem Spiel einen neuen Spieler hinzu
     *
     * @param player - neuer Spieler
     */
    public void addPlayer(final Player player) {
        if (player.getColor() == null){
            player.setColor(this.color.pop());
        }
        this.players.add(player);
    }

    /**
     * @return Liste der Spieler
     */
    public List<Player> getPlayers() {
        return this.players;
    }

    /**
     * Getter für die ID
     * @return UUID des Spiels
     */
    public UUID getId() {
        return id;
    }

    /**
     * Gibt das Spiel als String aus.
     * @return Gibt "Game" zurück
     */
    @Override
    public String toString() {
        return "Game" + this.name;
    }

    /**
     * Speicher das Spile ab
     *
     */
    public boolean save () throws PersistenceEndpointIOException{
        return this.persistenceEndpoint.save(this);
    }
}
