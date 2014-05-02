package main.java.logic;

import java.util.*;

import main.java.logic.exceptions.PlayerNotExsistInGameException;
import main.java.logic.exceptions.*;

/**
 * Created by Stefan on 01.04.2014.
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
     * Startet das Speil, sodass der Spielstatus und co aktualisiert werden
     *
     * @throws main.java.logic.exceptions.NotEnoughPlayerException
     */
    public void onGameStart() throws NotEnoughPlayerException, TooManyPlayerException, NotEnoughCountriesException, GameAllreadyStartedException {
        if (this.players.size() < Game.minCountPlayers) {
            throw new NotEnoughPlayerException(Game.minCountPlayers);
        } else if (this.players.size() > Game.maxCountPlayers) {
            throw new TooManyPlayerException(Game.maxCountPlayers);
        } else if (this.map.getCountries().size() < this.players.size()) {
            throw new NotEnoughCountriesException(this.map.getCountries().size());
        } else if (this.currentGameState != gameStates.WAITING){
            throw new GameAllreadyStartedException();
        }
        distributeCountries();
        this.currentGameState = gameStates.RUNNING;
        this.setCurrentRound(new Round(players,map, Turn.getDefaultStepsFirstRound()));


    }

    /**
     * Setzt die aktuelle Runde
     * @param r
     */
    public void setCurrentRound(Round r) {

        this.currentRound = r;
    }

    public void setNextRound() throws ToManyNewArmysException, RoundNotCompleteException{
        if(this.currentRound != null){
            if(!this.currentRound.isComplete()){
                throw new RoundNotCompleteException();
            }
        }
        this.currentRound = new Round(this.players,this.map);
    }

    /**
     * Gibt die aktuelle Runde des Spiels zurück
     * @return
     */
    public Round getCurrentRound() throws GameNotStartedException{
        if (this.getCurrentGameState() == gameStates.WAITING) {
            throw new GameNotStartedException();
        }
        return this.currentRound;
    }

    /**
     * Gibt den aktuellen status des Games zurück
     * @return
     */
    public gameStates getCurrentGameState() {
        return this.currentGameState;
    }

    /**
     * Kopiert die Liste aller L�nder in einen Stack
     *
     * @return
     */
    private Stack<Country> countryListToStack() {

        Stack<Country> c = new Stack<Country>();
        ArrayList<Country> allCountrys = map.getCountries();
        Collections.shuffle(allCountrys);
        c.addAll(allCountrys);
        return c;
    }

    /**
     * Verteilt die Länder beim Spielstart an alle angemeldeten Spieler.
     */
    private void distributeCountries() {
        Stack<Country> c = countryListToStack();
        /**
         * Größe des L�nder Stacks
         */
        int sizeC = c.size();
        /**
         * Größe der Spieler Liste
         */
        int sizeP = this.players.size();
        /**
         * Durchläuft die Schleife so lange, bis die Anzahl der L�nder, die noch zu verteilen sind,
         * kleiner ist, als die Anzahl der Spieler
         */
        while (sizeC >= sizeP) {
            /**
             * Durchl�uft die Spieler Liste und f�gt jedes Mal jeweils einem Spieler ein Land zu
             * Danach wird die Gr��e des Stacks neu berechnet -> f�r while Schleife
             */
            for (Player p : players) {
                p.addCountry(c.pop());

            }

            sizeC = c.size();
        }
        /**
         * war die Anzahl der noch zu verteilenden L�nder kleiner als die Anzahl der Spieler:
         * Wenn die Anzahl der L�nder gr��er als 0 ist wird in der for-Schleife so lange L�nder den Spieler zugeteilt, 
         * bis keine mehr da sind
         */
        if (sizeC > 0) {
            for (int i = 0; i < sizeC; i++) {
                Player p = players.get(i);
                p.addCountry(c.pop());
            }
        }
        setDefaultArmys();
    }

    /**
     * Wird beim Spielstart aufgerufen und setzt für alle Länder genau 1 Armee
     */
    private void setDefaultArmys() {
        for (Player o : players) {
            for (Country c : o.getCountries()) {
                //Nur machen, wenn noch keine Armee auf dem Land sitzt
                if (c.getArmyList().size() == 0) {
                    Army a = new Army(o, c);
                    try {
                        c.addArmy(a);
                    }catch (CountriesNotConnectedException e){
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
     * Wird ausgeführt sobald der näcshte Spieler an der Reihe ist
     */
    public void onNextPlayer(){

    }
    /**
     * Wird ausgelöst, sobald über die GUI ein neuer Spieler hinzugefügt wird.
     *
     * @param name - Der Name des neuen Spielers
     */
    public void onPlayerAdd(final String name) throws GameAllreadyStartedException{

        if (this.getCurrentGameState() != gameStates.WAITING){
            throw  new GameAllreadyStartedException();
        }
        else {
            Player newPlayer = new Player(name);
            this.addPlayer(newPlayer);
        }
    }

    /**
     * Die Karte, die fürs Spiel verwendet werden soll
     *
     * @param map
     */
    public void setMap(final Map map) {
        this.map = map;
    }

    /**
     * Gibt die Karte des Spiels zurück
     *
     * @return
     */
    public Map getMap() {
        return map;
    }
    public String toString() {
        return "Game";
    }

}
