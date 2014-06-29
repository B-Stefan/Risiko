import exceptions.PersistenceEndpointIOException;
import interfaces.IGame;
import interfaces.IGameManager;
import logic.Game;
import persistence.PersistenceManager;
import persistence.dataendpoints.PersistenceEndpoint;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
/**
 * Verwaltet eine Anzahl an Games und stellt Methdoen zum Seichern und Anzeigen der gespeicherten Spiele zur Verfügung
 */
public class GameManager extends UnicastRemoteObject implements IGameManager {

    /**
     * Handler, der die Interaktion zum File-System oder DB-System verwaltet
     */
    private PersistenceEndpoint handler;

    /**
     * Verwaltet mehrere Spiele
     * @param manager - Manager, der die
     */
    public GameManager(PersistenceManager manager) throws RemoteException{
        this.handler = manager.getGameHandler();
    }

    /**
     *
     * @return Gibt die Liste aller gespeicherten Spiele zurück
     * @throws PersistenceEndpointIOException
     */
    public List<IGame> getGameList() throws PersistenceEndpointIOException, RemoteException{
        return this.handler.getAll();
    }

    /**
     * Erstellt und speichert dieses neue Spiel ab
     * @return Das neu erstellte Spiel
     * @throws PersistenceEndpointIOException Fehler beim Einlesen der Datei oder Speichern
     */
    public Game addGame () throws PersistenceEndpointIOException, RemoteException{
        Game newGame = new Game(handler);
        this.saveGame(newGame);
        return newGame;
    }

    /**
     * Speichert ein Spiel ab
     * @param g  Spiel das gespeichert werden soll
     * @throws PersistenceEndpointIOException
     */
    public void saveGame(IGame g) throws PersistenceEndpointIOException, RemoteException{
        this.handler.save(g);
    }

    /**
     * Speichert ein Spiel ab
     * @param index Index aus der Liste von @see #getGameList
     * @throws PersistenceEndpointIOException
     * @throws IndexOutOfBoundsException
     */
    public void saveGame(int index)throws PersistenceEndpointIOException, IndexOutOfBoundsException, RemoteException{
        List<Game> gameList = this.handler.getAll();
        Game gameToSave =  gameList.get(index);
        this.handler.save(gameToSave);
    }
    @Override
    public String toString(){
        return "GameManager";
    }

}
