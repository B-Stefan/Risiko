package interfaces;

import exceptions.GameNotFoundException;
import exceptions.PersistenceEndpointIOException;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Created by Stefan on 29.06.14.
 */
public interface IGameManager extends Remote, Serializable, IToStringRemote {

    /**
     *
     * @return Gibt die Liste aller gespeicherten Spiele zurück
     * @throws exceptions.PersistenceEndpointIOException
     */
    public List<IGame> getSavedGameList() throws PersistenceEndpointIOException, RemoteException;

    /**
     *
     * @return Gibt eine Liste aller aktuell laufenden Spiele zurück, laufend bedeutet zur Laufzeit des Server erzeugten
     * @throws exceptions.PersistenceEndpointIOException
     */
    public List<IGame> getRunningGameList() throws PersistenceEndpointIOException, RemoteException;


    /**
     * Erstellt und speichert dieses neue Spiel ab
     * @return Das neu erstellte Spiel
     * @throws PersistenceEndpointIOException Fehler beim Einlesen der Datei oder Speichern
     */
    public IGame addGame () throws PersistenceEndpointIOException, RemoteException;

    /**
     * Speichert ein Spiel ab
     * @param g  Spiel das gespeichert werden soll
     * @throws PersistenceEndpointIOException
     */
    public void saveGame(IGame g) throws PersistenceEndpointIOException, GameNotFoundException,RemoteException;


    /**
     * Speichert ein Spiel ab
     * @param index Index aus der Liste von @see #getGameList
     * @throws PersistenceEndpointIOException
     * @throws IndexOutOfBoundsException
     */
    public void saveGame(int index)throws PersistenceEndpointIOException, IndexOutOfBoundsException, RemoteException;

    }
