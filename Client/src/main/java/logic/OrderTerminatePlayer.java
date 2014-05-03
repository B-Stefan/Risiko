package main.java.logic;

import main.resources.IOrder;
import main.resources.IPlayer;

/**
 * @author Stefan Bieliauskas
 * Diese Klasse ist für die Erfassung der Aufträge ausgelegt,
 * die darauf abzielen einen anderen Spieler zu vernichten.
 *
 *
 * @see main.resources.IOrder
 */
public class OrderTerminatePlayer  implements IOrder{

    @Override
    public boolean isCompleted() {
        return false;
    }

    @Override
    public IPlayer getPlayer() {
        return null;
    }

    @Override
    public void setPlayer(IPlayer player) {

    }
}
