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

package server.logic;

import interfaces.IClient;
import interfaces.IFight;
import ui.CUI.CountryCUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Vector;


public class ClientEventProcessor extends UnicastRemoteObject implements IClient {

    /**
     * Listeners für UI
     */
    private final List<ActionListener> updateUiListeners = new Vector<ActionListener>();


    /**
     * Listeners für Fight events
     */
    private final List<IFightActionListener> fightUiListeners = new Vector<IFightActionListener>();

    /**
     * Listeners für Fight events
     */
    private final List<IFightActionListener> fightCloseListeners = new Vector<IFightActionListener>();

    /**
     * Listner für allgemeine Broadcast msg
     */
    private final List<ActionListener> messageListeners = new Vector<ActionListener>();

    /**
     * Dient zur verarbeitung von vom Server gesendeten Events
     * @throws RemoteException
     */
    public ClientEventProcessor() throws RemoteException {
        super();

    }

    /**
     * Füg einen neuen Listener hinzu, der sobald ausgefürt wird wenn der Server meint das UI muss ein Update erfahren
     * @param e
     */
    public void addUpdateUIListener(ActionListener e){
        this.updateUiListeners.add(e);
    }

    /**
     * Füg einen neuen Listener hinzu, der sobald ausgefürt wird wenn der Server meint das UI muss ein Update erfahren
     * @param e
     */
    public void addFightCloseListener(IFightActionListener e){
        this.fightCloseListeners.add(e);
    }


    /**
     * Enfternt einen neuen Listener hinzu, der sobald ausgefürt wird wenn der Server meint das UI muss ein Update erfahren
     * @param e
     */
    public void removeFightCloseListener(IFightActionListener e){
        this.fightCloseListeners.remove(e);
    }


    /**
     * Löscht einen UI Listener
     * @param e
     */
    public void removeUpdateUIListener(ActionListener e){
        this.updateUiListeners.remove(e);
    }

    /**
     * Fügt einen Fight Listener hinzu wird ausgeführt, wenn ein Fihgt geöffnet werden soll
     * @param e
     */
    public void addFightListener(IFightActionListener e){
        this.fightUiListeners.add(e);
    }

    /**
     * Löscht den Fight listener
     * @param e
     */
    public void removeFightListener(IFightActionListener e){
        this.fightUiListeners.remove(e);
    }

    /**
     * Fügt einen Message Listener hinzu, sodass der Server Textnachrichten senden kann
     * @param e
     */
    public void addMessageListener(ActionListener e){
        this.messageListeners.add(e);
    }

    /**
     * Löschen einen Message Listeners
     * @param e
     */
    public void removeMessageListener(ActionEvent e){
        this.messageListeners.remove(e);
    }

    /**
     * WIrd vom Server aufgerufen
     * @param msges - Nachrichtem vom Servers
     * @throws RemoteException
     */
    public void receiveMessage(List<String> msges)throws RemoteException{
        for (String msg: msges){
            receiveMessage(msg);
        }
    }

    /**
     * WIrd vom Server aufgerufen
     * @param msg - Nachrichtem vom Servers
     * @throws RemoteException
     */
    public void receiveMessage(final String msg)throws RemoteException{
        for(final ActionListener e : this.messageListeners){
            Thread t = new Thread(new Runnable() {
                public void run()
                {
                    e.actionPerformed(new ActionEvent("Server",86769,msg));
                }
            });
            t.start();

        }
    }


    /**
     * Wird vom Server aufgerufen
     * @param fight Der Kampf der angezeigt werden soll
     * @throws RemoteException
     */
    public void receiveFightEvent(final IFight fight) throws RemoteException{
        for(final IFightActionListener e : this.fightUiListeners){
            Thread t = new Thread(new Runnable() {
                public void run()
                {
                    e.actionPerformed(fight);
                }
            });
            t.start();

        }
    }

    /**
     *  WIrd aufgerufen wenn ein Update der UI durchgeführt werden soll
     * @throws RemoteException
     */
    public void receiveUIUpdateEvent() throws RemoteException{
        for(final ActionListener e : this.updateUiListeners){
            Runnable r = new Runnable() {
                public void run()
                {
                    e.actionPerformed(new ActionEvent("Server",99999,"Update the UI"));
                }
            };
            Thread t = new Thread(r);
            t.start();

        }
    }

    /**
     * Wird ausglöst, wenn der Fihgt geschlossen werden soll
     * @param fight - Fight der geschlossen werden soll
     */
    public void receiveFightCloseEvent(final IFight fight){
        for(final IFightActionListener e : this.fightCloseListeners){
            Runnable r = new Runnable() {
                public void run()
                {
                    e.actionPerformed(fight);
                }
            };
            Thread t = new Thread(r);
            t.start();

        }
    }

}
