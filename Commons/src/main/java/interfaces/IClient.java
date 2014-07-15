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

package interfaces;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;


/**
 * Interface was für den Bradcast vom Server zum Client benötigt wird
 */
public interface IClient extends Remote,Serializable{

    public static enum UIUpdateTypes {
        RUNNING_GAME_LIST,
        PLAYER,
        COUNtRY,
        FIGHT,
        FIGHT_CLOSE,
        ALL
    }

    /**
     *
     * @param msg Liste von Nachrichten, die angezeigt werden sollen
     * @throws RemoteException
     */
    public void receiveMessage(List<String> msg) throws RemoteException;

    /**
     * Wird vom Server aufgerufen, wenn ein Clien einen fight startet, sodass dieser sich auch auf dem anderen Client öffnet
     * @param fight Der Fight der geöffnet werden soll
     * @throws RemoteException
     */
    public void receiveFightEvent(IFight fight) throws RemoteException;

    /**
     * Gibt an den Client eine Nachricht weiter
     * @param msg Nachricht
     * @throws RemoteException
     */
    public void receiveMessage(String msg) throws RemoteException;

    /**
     * Wenn ein Update des UI notwendig ist
     * @throws RemoteException
     */
    public void receiveUIUpdateEvent() throws RemoteException;

    /**
     * Wird ausgeläst wenn der Fight geschlossen werden soll
     * @param fight - Fight der geschlossen werden soll
     */
    public void receiveFightCloseEvent(IFight fight) throws RemoteException;
}
