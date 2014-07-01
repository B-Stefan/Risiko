package interfaces;

import exceptions.*;
import interfaces.data.IMap;
import interfaces.data.IPlayer;
import interfaces.data.cards.ICardDeck;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.UUID;

/**
 * Created by Stefan on 29.06.14.
 */
public interface IGame extends Remote, Serializable, IToStringRemote {

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
    public void onGameStart() throws RemoteException, NotEnoughPlayerException, TooManyPlayerException, NotEnoughCountriesException, GameAllreadyStartedException, PlayerAlreadyHasAnOrderException;



    /**
     * Versetzt das Spiel in die nächste Runde
     * @throws ToManyNewArmysException
     * @throws RoundNotCompleteException
     * @throws GameNotStartedException
     * @throws GameIsCompletedException
     */
    public void setNextRound() throws ToManyNewArmysException, RoundNotCompleteException,GameNotStartedException, GameIsCompletedException,RemoteException;

    /**
     * Gibt die Aktelle Runde des Spielers zurück
     * @return
     * @throws GameNotStartedException
     */
    public IRound getCurrentRound() throws GameNotStartedException,RemoteException;

    /**
     * Gibt den aktuellen Status des Spiels zurück
     * @return Status des Spiels
     */
    public gameStates getCurrentGameState() throws RemoteException;

    /**
     * Wird ausgelöst, wenn durch die GUI ein Spieler das Spiel verlässt
     *
     * @param player - Player der gelöscht werden soll
     *
     * @throws exceptions.PlayerNotExsistInGameException
     */
    public void onPlayerDelete(final IPlayer player) throws PlayerNotExsistInGameException,RemoteException;


    /**
     * Gibt den Gewinner zurück, der das Spiel gewonnen hat
     * Wenn keiner gewonnen hat gibt die Methode null zurück
     * @return Sieger des Spiels
     */
    public IPlayer getWinner () throws RemoteException;

    /**
     * Gibt die Karte des Spiels zurück
     *
     * @return
     */
    public IMap getMap() throws RemoteException;


    /**
     * FÜgt dem Spiel den Spieler mit dem Namem hinzu
     * @param name Name des Neuen Spielers
     * @param client Client object des Spielers für Brodcast
     * @return Den neuen Spieler
     * @throws PlayerNameAlreadyChooseException
     * @throws RemoteException
     */
    public IPlayer addPlayer(final String name, final IClient client) throws GameAllreadyStartedException,PlayerNameAlreadyChooseException,RemoteException;

    /**
     * Gibt den Spieler zum angegebenen Namen zurück
     * @param name - Name des gesuchten Spielers
     * @return Spieler
     * @throws PlayerNotExsistInGameException Wenn Spieler nicht gefunden wird
     * @throws RemoteException
     */
    public IPlayer getPlayer(final String name) throws PlayerNotExsistInGameException, RemoteException;

    /**
     * @return Liste der Spieler
     */
    public List<? extends IPlayer> getPlayers() throws RemoteException;


    /**
     * Getter für die ID
     * @return UUID des Spiels
     */
    public UUID getId() throws RemoteException;

    /**
     * Speicher das Spiel ab
     *
     */
    public boolean save () throws PersistenceEndpointIOException, RemoteException;


    public ICardDeck getDeck();

    }
