package main.java.persistence.objects;
import main.java.logic.Game;
import main.java.logic.Round;
import main.java.logic.data.Map;
import main.java.logic.data.Player;
import main.java.persistence.PersistenceManager;
import main.java.persistence.dataendpoints.PersistenceEndpoint;
import main.java.persistence.exceptions.PersistenceEndpointIOException;


import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

public class PersistenceGame extends PersitenceObject<Game> {

    public final List<String> players = new ArrayList<String>();
    private final UUID id;
    private final Game.gameStates gameState;
    private final UUID map;
    public PersistenceGame(Game game, PersistenceManager manager) throws PersistenceEndpointIOException{
        super(game, manager);
        this.id =  game.getId();
        this.map = game.getMap().getId();
        this.gameState = game.getCurrentGameState();
        PersistenceEndpoint<Player> playerHandler = manager.getPlayerHandler();
        for(Player p : game.getPlayers()){
            this.players.add(p.getId().toString());
            playerHandler.save(p);
        }
        manager.getMapHandler().save(game.getMap());
    }

    @Override
    public UUID getID() {
        return this.id;
    }

    @Override
    public Game convertToSourceObject(PersistenceManager manager) throws PersistenceEndpointIOException{
        Map storedMap = manager.getMapHandler().get(this.map);
        Game newGame = new Game(manager.getGameHandler(),storedMap);

        List<Player> players= new ArrayList<Player>();
        PersistenceEndpoint<Player> playerHandler =  manager.getPlayerHandler();

        for(String uuid : this.players) {
            players.add(playerHandler.get(uuid));
        }
        newGame.addPlayers(players);
        newGame.setCurrentGameState(this.gameState);

        //Game waiting for players
        if (newGame.getPlayers().size() > 0 ) {
            newGame.setCurrentRound(new Round(newGame.getPlayers(), newGame.getMap()));
        }

        return newGame;
    }
}
