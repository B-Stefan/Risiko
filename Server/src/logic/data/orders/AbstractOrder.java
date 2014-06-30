package logic.data.orders;

import interfaces.data.IPlayer;

/**
 * Created by Stefan on 30.06.14.
 */
public class AbstractOrder {

    /**
     * Der Spieler, dem die Order zugewiesen ist
     */
    protected final IPlayer agent;



    protected AbstractOrder(final IPlayer agend){
        this.agent = agend;
    }

    /**
     * Getter für den Agent
     * @return Player
     */

    public IPlayer getAgent() {
        return this.agent;
    }
}
