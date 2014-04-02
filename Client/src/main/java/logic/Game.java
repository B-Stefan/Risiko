package main.java.logic;
import  java.util.*;
import main.java.logic.exceptions.PlayerNotExsistInGameException;
import main.resources.IGameGUI;
import main.java.logic.exceptions.*;

/**
 * Created by Stefan on 01.04.2014.
 */
public class Game implements IGameGUI{

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
    private Map map;
    /**
     * Listet alle Spieler auf, die aktiv am Spiel teilnehmen.
     */
    private final ArrayList<Player> players = new ArrayList<Player>();

    /**
     * Die Game-Klasse dient zur verwaltung des gesammten Spiels
     */
    public Game (){

    }

    /**
     * Für dem Spiel einen neuen Spieler hinzu
     * @param  player - neuer Spieler
     *
     */
    public void addPlayer(final Player player ){
        this.players.add(player);
    }

    /**
     *
     * @return Liste der Spieler
     */
    public ArrayList<Player> getPlayers(){
        return this.players;
    }

    /**
     * Startet das Speil, sodass der Spielstatus und co aktualisiert werden
     * @throws NotEnoughPlayerException
     */
    public void onGameStart() throws NotEnoughPlayerException, TooManyPlayerException {
        if(this.players.size() < Game.minCountPlayers){
            throw new NotEnoughPlayerException(Game.minCountPlayers);
        }
        else if (this.players.size() > Game.maxCountPlayers){
            throw new TooManyPlayerException(Game.maxCountPlayers);
        }
    }

    /**
     * Wird ausgelöst, wenn durch die GUI ein Spieler das Spiel verlässt
     * @param player - Player der gelöscht werden soll
     * @throws main.java.logic.exceptions.PlayerNotExsistInGameException
     */
    public void onPlayerDelete(final Player player ) throws PlayerNotExsistInGameException {
        try {
            this.players.remove(player);
        }
        catch (final Exception e){
            throw new PlayerNotExsistInGameException(player);
        }
    }

    /**
     * Wird ausgelöst, sobald über die GUI ein neuer Spieler hinzugefügt wird.
     * @param name - Der Name des neuen Spielers
     * @throws Exception
     */
    public void onPlayerAdd(final String name) throws Exception{

        Player newPlayer = new Player(name);
        this.addPlayer(newPlayer);
    }

    /**
     * Die Karte, die fürs Spiel verwendet werden soll
     * @param map
     */
    public void setMap(final Map map) {
        this.map = map;
    }

    /**
     * Gibt die Karte des Spiels zurück
     * @return
     */
    public Map getMap() {
        return map;
    }

}
