import logic.Game;
import persistence.PersistenceManager;
import persistence.dataendpoints.PersistenceEndpoint;
import persistence.exceptions.PersistenceEndpointIOException;

import java.util.*;
/**
 * Verwaltet eine Anzahl an Games und stellt Methdoen zum Seichern und Anzeigen der gespeicherten Spiele zur Verfügung
 */
public class GameManager {

    /**
     * Handler, der die Interaktion zum File-System oder DB-System verwaltet
     */
    private PersistenceEndpoint handler;

    /**
     * Verwaltet mehrere Spiele
     * @param manager - Manager, der die
     */
    public GameManager(PersistenceManager manager) {
        this.handler = manager.getGameHandler();
    }

    /**
     *
     * @return Gibt die Liste aller gespeicherten Spiele zurück
     * @throws PersistenceEndpointIOException
     */
    public List<Game> getGameList() throws PersistenceEndpointIOException{
        return this.handler.getAll();
    }

    /**
     * Erstellt und speichert dieses neue Spiel ab
     * @return Das neu erstellte Spiel
     * @throws PersistenceEndpointIOException Fehler beim Einlesen der Datei oder Speichern
     */
    public Game addGame () throws PersistenceEndpointIOException{
        Game newGame = new Game(handler);
        this.saveGame(newGame);
        return newGame;
    }

    /**
     * Speichert ein Spiel ab
     * @param g  Spiel das gespeichert werden soll
     * @throws PersistenceEndpointIOException
     */
    public void saveGame(Game g) throws PersistenceEndpointIOException{
        this.handler.save(g);
    }

    /**
     * Speichert ein Spiel ab
     * @param index Index aus der Liste von @see #getGameList
     * @throws PersistenceEndpointIOException
     * @throws IndexOutOfBoundsException
     */
    public void saveGame(int index)throws PersistenceEndpointIOException, IndexOutOfBoundsException{
        List<Game> gameList = this.handler.getAll();
        Game gameToSave =  gameList.get(index);
        this.handler.save(gameToSave);
    }
    @Override
    public String toString(){
        return "GameManager";
    }

}