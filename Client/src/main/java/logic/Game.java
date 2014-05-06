package main.java.logic;

import java.util.*;

import main.java.logic.exceptions.PlayerNotExsistInGameException;
import main.java.logic.exceptions.*;
import main.java.logic.orders.OrderManager;
import main.java.logic.orders.IOrder;

/**
 * @author Jennifer Theloy, Thu Nguyen, Stefan Bieliauskas
 *
 * Klasse für ein eizelnes Spiel. Diese dient zur Spielverwaltung.
 */
public class Game {

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
        RUNNING, // The game passes through the rounds
        FINISHED // One player finished his order
    }

    /**
     * Representiert die Karte des Spiels
     */
    private Map map;

    /**
     * Listet alle Spieler auf, die aktiv am Spiel teilnehmen.
     */
    private final ArrayList<Player> players = new ArrayList<Player>();

    /**
     * The current game state, default => WAITING
     */
    private gameStates currentGameState = gameStates.WAITING;

    /**
     * Contains the current round , default null
     */
    private Round currentRound;

    /**
     * Die Game-Klasse dient zur verwaltung des gesammten Spiels
     */
    public Game() {
        this.map = new Map();
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


    public void setNextRound() throws ToManyNewArmysException, RoundNotCompleteException,GameNotStartedException {
        if (this.currentRound != null) {
            if (!this.currentRound.isComplete()) {
                throw new RoundNotCompleteException();
            }
        }
        if (this.getCurrentGameState() == gameStates.WAITING){
            throw  new GameNotStartedException();
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
            Player newPlayer = new Player(name);
            this.addPlayer(newPlayer);
        }
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
     * Gibt die Karte des Spiels zurück
     *
     * @return
     */
    public Map getMap() {
        return map;
    }

    /**
     * Für dem Spiel einen neuen Spieler hinzu
     *
     * @param player - neuer Spieler
     */
    public void addPlayer(final Player player) {
        this.players.add(player);
    }

    /**
     * @return Liste der Spieler
     */
    public ArrayList<Player> getPlayers() {
        return this.players;
    }

    /**
     * Gibt das Spiel als String aus.
     * @return Gibt "Game" zurück
     */
    @Override
    public String toString() {
        return "Game";
    }

}
