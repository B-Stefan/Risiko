/*
 * RISIKO-JAVA - Game, Copyright 2014  Jennifer Theloy, Stefan Bieliauskas  -  All Rights Reserved.
 * Hochschule Bremen - University of Applied Sciences
 *
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 * Contact:
 *     Jennifer Theloy: jTheloy@stud.hs-bremen.de
 *     Stefan Bieliauskas: sBieliauskas@stud.hs-bremen.de
 *
 * Web:
 *     https://github.com/B-Stefan/Risiko
 *
 */

package commons.interfaces.data.Orders;

import commons.interfaces.IToStringRemote;

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
