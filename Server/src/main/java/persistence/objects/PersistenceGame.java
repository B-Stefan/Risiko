package persistence.objects;
import exceptions.PersistenceEndpointIOException;
import interfaces.IGame;
import interfaces.data.IMap;
import interfaces.data.IPlayer;
import logic.Game;
import logic.Round;
import logic.data.Map;
import logic.data.Player;
import persistence.PersistenceManager;
import persistence.dataendpoints.PersistenceEndpoint;


import java.rmi.RemoteException;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

public class PersistenceGame extends PersitenceObject<IGame> {

    public final List<String> players = new ArrayList<String>();
    private final UUID id;
    private final IGame.gameStates gameState;
    private final UUID map;
    public PersistenceGame(IGame game, PersistenceManager manager) throws PersistenceEndpointIOException{
        super(game, manager);
        try {
            this.id =  game.getId();
            this.map = game.getMap().getId();
            this.gameState = game.getCurrentGameState();
            PersistenceEndpoint<IPlayer> playerHandler = manager.getPlayerHandler();
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
            manager.getMapHandler().save(game.getMap());
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
    public IGame convertToSourceObject(PersistenceManager manager) throws PersistenceEndpointIOException{
        try {
            IMap storedMap = manager.getMapHandler().get(this.map);
            Game newGame = new Game(manager.getGameHandler(),storedMap);

            List<IPlayer> players= new ArrayList<IPlayer>();
            PersistenceEndpoint<IPlayer> playerHandler =  manager.getPlayerHandler();

            for(String uuid : this.players) {
                players.add(playerHandler.get(uuid));
            }
            newGame.addPlayers(players);
            newGame.setCurrentGameState(this.gameState);

            //IGame waiting for players
            if (newGame.getPlayers().size() > 0 ) {
                newGame.setCurrentRound(new Round(newGame.getDeck(), newGame.getPlayers(), newGame.getMap()));
            }

            return newGame;
        }catch (RemoteException e){
            //Kann nicht auftreten, da keine Kommunikation üerber netzwerk stattfindet
            throw new RuntimeException(e);
        }
    }
}
