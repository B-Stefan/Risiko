package main.java.persistence.objects;

import main.java.logic.data.Player;
import main.java.persistence.PersistenceManager;
import main.java.persistence.exceptions.PersistenceEndpointIOException;

import java.util.UUID;

public class PersistencePlayer extends PersitenceObject<Player> {

    private transient Player player;
    private final UUID id;
    private final String name;
    public PersistencePlayer(Player player){
        super(player);
        this.player = player;
        this.name = player.getName();
        this.id =  player.getId();
    }

    @Override
    public UUID getID() {
        return this.id;
    }

    @Override
    public Player convertToSourceObject(PersistenceManager manager) throws PersistenceEndpointIOException{

        Player newInstance = new Player(this.name);

        return newInstance;
    }
}
