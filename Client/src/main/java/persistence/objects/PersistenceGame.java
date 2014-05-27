package main.java.persistence.objects;
import main.java.logic.Game;
import main.java.logic.Player;


import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

public class PersistenceGame implements IPersitenceObject {

    public final List<String> players = new ArrayList<String>();
    private transient Game game;
    private final UUID id;
    public PersistenceGame(Game game){
        this.game = game;
        this.setPlayers();
        this.id =  game.getId();
    }
    private void setPlayers() {
        for(Player p : game.getPlayers()){
            this.players.add(p.getId().toString());
        }
    }

    @Override
    public UUID getID() {
        return this.id;
    }
}
