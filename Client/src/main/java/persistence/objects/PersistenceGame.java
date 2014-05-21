package main.java.persistence.objects;
import main.java.logic.Game;
import main.java.logic.Player;


import java.util.List;
import java.util.ArrayList;

public class PersistenceGame {

    public final List<String> players = new ArrayList<String>();
    private transient Game game;
    public PersistenceGame(Game game){
        this.game = game;
        this.setPlayers();
    }
    private void setPlayers() {
        for(Player p : game.getPlayers()){
            this.players.add(p.getId().toString());
        }
    }
}
