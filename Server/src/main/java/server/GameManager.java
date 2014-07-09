package server;

import exceptions.CountryNotInListException;
import exceptions.GameNotFoundException;
import exceptions.PersistenceEndpointIOException;
import interfaces.IGame;
import interfaces.IGameManager;
import server.logic.Game;
import server.persistence.PersistenceManager;
import server.persistence.dataendpoints.PersistenceEndpoint;

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
    private final PersistenceEndpoint handler;

    /**
     * Diese Liste beinhaltet alle Spiele, bei denen aktuell Spieler angemeldet sind
     */
    private final Map<UUID,Game> runningGames = new HashMap<UUID,Game>();

    /**
     * Verwaltet mehrere Spiele
     * @param manager - Manager, der die
     */
    public GameManager(final PersistenceManager manager) throws RemoteException{
        this.handler = manager.getGameHandler();
    }
    /**
     *
     * @return Gibt die Liste aller gespeicherten Spiele zurück
     * @throws PersistenceEndpointIOException
     * @throws CountryNotInListException 
     */
    public List<IGame> getSavedGameList() throws PersistenceEndpointIOException, RemoteException{
        return this.handler.getAll();
    }

    /**
     *
     * @return Gibt die Liste aller gespeicherten Spiele zurück
     * @throws PersistenceEndpointIOException
     */
    public List<IGame> getRunningGameList() throws PersistenceEndpointIOException, RemoteException{
        List<IGame> runningGames = new ArrayList<IGame>();
        for(Map.Entry<UUID,Game> entry : this.runningGames.entrySet()){
            runningGames.add(entry.getValue());
        }
        return runningGames;
    }


    /**
     * Erstellt und speichert dieses neue Spiel ab
     * @return Das neu erstellte Spiel
     * @throws PersistenceEndpointIOException Fehler beim Einlesen der Datei oder Speichern
     */
    public Game addGame () throws PersistenceEndpointIOException, RemoteException{
        Game newGame = new Game(handler);
        this.runningGames.put(newGame.getId(),newGame);
        return newGame;
    }

    /**
     * Speichert ein Spiel ab
     * @param g  Spiel das gespeichert werden soll
     * @throws PersistenceEndpointIOException
     */
    public void saveGame(IGame g) throws PersistenceEndpointIOException,GameNotFoundException, RemoteException{
        Game gameToSave = runningGames.get(g.getId());
        if(gameToSave == null){
            throw new GameNotFoundException();
        }
        else {
            this.handler.save(gameToSave);
        }
    }
    /**
     * Speichert ein Spiel ab
     * @param index Index aus der Liste von @see #getGameList
     * @throws PersistenceEndpointIOException
     * @throws IndexOutOfBoundsException
     * @throws CountryNotInListException 
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

    /**
     * ToString methode, die Remote aufgerufen werden kann
     * @return
     * @throws RemoteException
     */
    public String toStringRemote() throws RemoteException{
        return this.toString();
    }

}
