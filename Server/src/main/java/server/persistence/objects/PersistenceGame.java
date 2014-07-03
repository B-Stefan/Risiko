package server.persistence.objects;
import exceptions.*;
import interfaces.IGame;
import interfaces.data.IPlayer;
import server.logic.Game;
import server.logic.data.Map;
import server.logic.data.Player;
import server.persistence.PersistenceManager;
import server.persistence.dataendpoints.PersistenceEndpoint;

import java.rmi.RemoteException;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

public class PersistenceGame extends PersitenceObject<Game> {

    public final List<String> players = new ArrayList<String>();
    private final UUID id;
    private final IGame.gameStates gameState;
    private final UUID map;
    public PersistenceGame(Game game, PersistenceManager manager) throws PersistenceEndpointIOException{
        super(game, manager);
        try {
            this.id =  game.getId();
            this.map = game.getMap().getId();
            this.gameState = game.getCurrentGameState();
            PersistenceEndpoint<Player> playerHandler = manager.getPlayerHandler();
            for(IPlayer p : game.getPlayers()){
                Player player;
                try {
                    player = (Player) p;
                }catch (ClassCastException e){
                    throw new RuntimeException(e);
                }
                this.players.add(p.getId().toString());
                playerHandler.save(player);
            }
            manager.getMapHandler().save((Map)game.getMap());
        }catch (RemoteException e){
            //Kann nicht auftreten, da keine Netzwerkkommunikation stattfindet
            throw new RuntimeException(e);
        }

    }

    @Override
    public UUID getID() {
        return this.id;
    }

    @Override
    public Game convertToSourceObject(PersistenceManager manager) throws PersistenceEndpointIOException, CountryNotInListException{
        try {
            Map storedMap = manager.getMapHandler().get(this.map);
            Game newGame = new Game(manager.getGameHandler(),storedMap);

            List<Player> players= new ArrayList<Player>();
            PersistenceEndpoint<Player> playerHandler =  manager.getPlayerHandler();

            for(String uuid : this.players) {
                players.add(playerHandler.get(uuid));
            }
            newGame.addPlayers(players);
            newGame.setCurrentGameState(this.gameState);

            //IGame waiting for players
            if (newGame.getPlayers().size() > 0 ) {
                try {
                    newGame.setNextRound();
                }catch (ToManyNewArmysException | RoundNotCompleteException | GameNotStartedException | GameIsCompletedException | RemoteException e){
                    throw new PersistenceEndpointIOException(e);
                }
            }

            return newGame;
        }catch (RemoteException e){
            //Kann nicht auftreten, da keine Kommunikation üerber netzwerk stattfindet
            throw new RuntimeException(e);
        }
    }
}
