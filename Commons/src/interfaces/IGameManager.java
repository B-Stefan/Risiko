package interfaces;

import exceptions.PersistenceEndpointIOException;

import java.util.List;

/**
 * Created by Stefan on 29.06.14.
 */
public interface IGameManager {

    /**
     *
     * @return Gibt die Liste aller gespeicherten Spiele zurück
     * @throws exceptions.PersistenceEndpointIOException
     */
    public List<IGame> getGameList() throws PersistenceEndpointIOException;


    /**
     * Erstellt und speichert dieses neue Spiel ab
     * @return Das neu erstellte Spiel
     * @throws PersistenceEndpointIOException Fehler beim Einlesen der Datei oder Speichern
     */
    public IGame addGame () throws PersistenceEndpointIOException;

    /**
     * Speichert ein Spiel ab
     * @param g  Spiel das gespeichert werden soll
     * @throws PersistenceEndpointIOException
     */
    public void saveGame(IGame g) throws PersistenceEndpointIOException;


    /**
     * Speichert ein Spiel ab
     * @param index Index aus der Liste von @see #getGameList
     * @throws PersistenceEndpointIOException
     * @throws IndexOutOfBoundsException
     */
    public void saveGame(int index)throws PersistenceEndpointIOException, IndexOutOfBoundsException;


    }
