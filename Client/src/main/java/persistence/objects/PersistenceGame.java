package main.java.persistence.objects;
import main.java.logic.Game;
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
    public PersistenceGame(Game game){
        super(game);
        this.id =  game.getId();
        for(Player p : game.getPlayers()){
            this.players.add(p.getId().toString());
        }
    }

    @Override
    public UUID getID() {
        return this.id;
    }

    @Override
    public Game convertToSourceObject(PersistenceManager manager) throws PersistenceEndpointIOException{
        Game newGame = new Game();
        List<Player> player= new ArrayList<Player>();
        PersistenceEndpoint<Player> playerHandler =  manager.getPlayerHandler();

        for(String uuid : this.players) {
            player.add(playerHandler.get(uuid));
        }
        return newGame;
    }
}
