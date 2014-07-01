package interfaces.data.Orders;

import interfaces.IToStringRemote;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author  Stefan Bieliauskas
 * Schnittstelle für einzlene AuftrÃ¤ge, die der Spieler erledigen muss Implementierungen siehe:
 *
 * @see server.logic.data.orders
 * @version 1.0
 *
 * Hierbei wurde eine Schnittstelle verwendet, da die beiden Auftragsarten keine bzw.
 * sehr geringe Gemeinsamkeiten in der implementierung besitzten.
 *
 */
public interface IOrder extends Remote, Serializable, IToStringRemote {
    /**
     *
     * @return Wenn die Order komplett ist => True
     */
    public boolean isCompleted()throws RemoteException;

}
