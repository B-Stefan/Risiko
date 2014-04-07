package main.resources;

/**
 * Created by Stefan on 31.03.2014.
 * @author  Stefan Bieliauskas
 * Schnittstelle für einzlene Aufträge, die der Spieler erledigen muss Implementierungen siehe:
 *
 * @see main.java.logic.OrderTerminatePlayer
 * @see main.OrderTakeCountries
 * @version 1.0
 *
 * Hierbei wurde eine Schnittstelle verwendet, da die beiden Auftragsarten keine bzw.
 * sehr geringe Gemeinsamkeiten in der implementierung besitzten.
 *
 */
public interface IOrder {
    /**
     *
     * @return Wenn die Order komplett ist => True
     */
    public boolean isCompleted();
    /**
     *
     * @return - Aktueller Spieler, der den Auftrag erledigen muss
     */
    public IPlayer getPlayer();

    /**
     *
     * @param player - Spieler der den Auftrag erledigen muss
     */
    public void setPlayer(IPlayer player);
}
