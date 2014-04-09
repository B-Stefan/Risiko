package main.java.logic;
import  java.util.*;
import main.java.logic.exceptions.PlayerNotExsistInGameException;
import main.resources.IGameGUI;
import main.java.logic.exceptions.*;
import org.omg.CORBA.NO_IMPLEMENT;

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
    	this.map = new Map();
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
     * @throws main.java.logic.exceptions.NotEnoughPlayerException
     */
    public void onGameStart() throws NotEnoughPlayerException, TooManyPlayerException, NotEnoughCountriesException {
        if(this.players.size() < Game.minCountPlayers){
            throw new NotEnoughPlayerException(Game.minCountPlayers);
        }
        else if (this.players.size() > Game.maxCountPlayers){
            throw new TooManyPlayerException(Game.maxCountPlayers);
        }
        else if (this.map.getCountries().size() < this.players.size()){
            throw new NotEnoughCountriesException(this.map.getCountries().size());
        }
        distributeCountries();
    }
    /**
     * Kopiert die Liste aller L�nder in einen Stack
     * @return 
     */
    private Stack<Country> countryListToStack(){

    	Stack<Country> c = new Stack<Country>();
        ArrayList<Country> allCountrys = map.getCountries();
        Collections.shuffle(allCountrys);
    	c.addAll(allCountrys);
    	return c;
    }

    /**
     * Verteilt die Länder beim Spielstart an alle angemeldeten Spieler.
     */
    private void distributeCountries(){
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
        while(sizeC >= sizeP){
        	/**
        	 * Durchl�uft die Spieler Liste und f�gt jedes Mal jeweils einem Spieler ein Land zu
        	 * Danach wird die Gr��e des Stacks neu berechnet -> f�r while Schleife
        	 */
        	for(Player p : players){
        		p.addCountry(c.pop());
        	}

        	sizeC = c.size();
        }
        /**
         * war die Anzahl der noch zu verteilenden L�nder kleiner als die Anzahl der Spieler:
         * Wenn die Anzahl der L�nder gr��er als 0 ist wird in der for-Schleife so lange L�nder den Spieler zugeteilt, 
         * bis keine mehr da sind
         */
        if(sizeC>0){
        	for(int i = 0;i<sizeC; i++){
        		Player p = players.get(i);
        		p.addCountry(c.pop());
        	}
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
