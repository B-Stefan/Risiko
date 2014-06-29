package persistence.objects;
import logic.Game;
import logic.Round;
import logic.data.Map;
import logic.data.Player;
import persistence.PersistenceManager;
import persistence.dataendpoints.PersistenceEndpoint;
import persistence.exceptions.PersistenceEndpointIOException;


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
            newGame.setCurrentRound(new Round(newGame.getDeck(), newGame.getPlayers(), newGame.getMap()));
        }

        return newGame;
    }
}
