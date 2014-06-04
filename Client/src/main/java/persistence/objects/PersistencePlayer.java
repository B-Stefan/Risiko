package main.java.persistence.objects;

import main.java.logic.data.Country;
import main.java.logic.data.Map;
import main.java.logic.data.Player;
import main.java.persistence.PersistenceManager;
import main.java.persistence.exceptions.PersistenceEndpointIOException;

import java.util.HashMap;
import java.util.UUID;

public class PersistencePlayer extends PersitenceObject<Player> {

    private transient Player player;
    private final UUID id;
    private final String name;
    private final HashMap<UUID,Integer> countries = new HashMap<UUID,Integer>();
    public PersistencePlayer(Player player){
        super(player);
        this.player = player;
        this.name = player.getName();
        this.id =  player.getId();
        for (Country c : player.getCountries()){
            countries.put(c.getId(),c.getNumberOfArmys());
        }
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
