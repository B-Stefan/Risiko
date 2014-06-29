package interfaces.data.Orders;

import interfaces.data.IPlayer;

/**
 *
 * @author  Stefan Bieliauskas
 * Schnittstelle für einzlene AuftrÃ¤ge, die der Spieler erledigen muss Implementierungen siehe:
 *
 * @see logic.data.orders
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
    public void setAgent(IPlayer ag);


    public String toString();
}
