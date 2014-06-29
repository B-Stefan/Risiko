package interfaces;

import exceptions.*;
import interfaces.data.IMap;
import interfaces.data.IPlayer;

import java.util.List;
import java.util.UUID;

/**
 * Created by Stefan on 29.06.14.
 */
public interface IGame {

    public static enum gameStates {
        WAITING, // Wait for start input
        RUNNING, // The gamePanels passes through the rounds
        FINISHED // One player finished his order
    }
    /**
     * Startet das Spiel
     * @throws exceptions.NotEnoughPlayerException
     * @throws exceptions.TooManyPlayerException
     * @throws exceptions.NotEnoughCountriesException
     * @throws exceptions.GameAllreadyStartedException
     * @throws exceptions.PlayerAlreadyHasAnOrderException
     */
    public void onGameStart() throws NotEnoughPlayerException, TooManyPlayerException, NotEnoughCountriesException, GameAllreadyStartedException, PlayerAlreadyHasAnOrderException;



    /**
     * Versetzt das Spiel in die nächste Runde
     * @throws ToManyNewArmysException
     * @throws RoundNotCompleteException
     * @throws GameNotStartedException
     * @throws GameIsCompletedException
     */
    public void setNextRound() throws ToManyNewArmysException, RoundNotCompleteException,GameNotStartedException, GameIsCompletedException;

    /**
     * Gibt die Aktelle Runde des Spielers zurück
     * @return
     * @throws GameNotStartedException
     */
    public IRound getCurrentRound() throws GameNotStartedException;

    /**
     * Gibt den aktuellen Status des Spiels zurück
     * @return Status des Spiels
     */
    public gameStates getCurrentGameState();

    /**
     * Wird ausgelöst, wenn durch die GUI ein Spieler das Spiel verlässt
     *
     * @param player - Player der gelöscht werden soll
     *
     * @throws exceptions.PlayerNotExsistInGameException
     */
    public void onPlayerDelete(final IPlayer player) throws PlayerNotExsistInGameException;



    /**
     * Wird ausgelöst, sobald über die GUI ein neuer Spieler hinzugefügt wird.
     *
     * @param name - Der Name des neuen Spielers
     */
    public void onPlayerAdd(final String name) throws GameAllreadyStartedException;

    /**
     * Gibt den Gewinner zurück, der das Spiel gewonnen hat
     * Wenn keiner gewonnen hat gibt die Methode null zurück
     * @return Sieger des Spiels
     */
    public IPlayer getWinner ();

    /**
     * Gibt die Karte des Spiels zurück
     *
     * @return
     */
    public IMap getMap();

    /**
     * Für dem Spiel einen neuen Spieler hinzu
     *
     * @param player - neuer Spieler
     */
    public void addPlayer(final IPlayer player);

    /**
     * @return Liste der Spieler
     */
    public List<IPlayer> getPlayers();


    /**
     * Getter für die ID
     * @return UUID des Spiels
     */
    public UUID getId();

    /**
     * Speicher das Spiel ab
     *
     */
    public boolean save ();




    }
