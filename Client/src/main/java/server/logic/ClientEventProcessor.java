package server.logic;

import com.sun.xml.internal.ws.api.pipe.Fiber;
import interfaces.IClient;
import interfaces.IFight;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.EventHandler;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Vector;

/**
 * Created by Stefan on 01.07.14.
 */
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
     * @param msg - Nachricht vom Servers
     * @throws RemoteException
     */
    public void receiveMessage(String msg)throws RemoteException{
        for(ActionListener e : this.messageListeners){
            e.actionPerformed(new ActionEvent("Server",86769,msg));
        }
    }

    /**
     * Wird vom Server aufgerufen
     * @param fight Der Kampf der angezeigt werden soll
     * @throws RemoteException
     */
    public void receiveFightEvent(IFight fight) throws RemoteException{
        for(IFightActionListener e : this.fightUiListeners){
            e.actionPerformed(fight);
        }
    }

    /**
     *  WIrd aufgerufen wenn ein Update der UI durchgeführt werden soll
     * @throws RemoteException
     */
    public void receiveUIUpdateEvent() throws RemoteException{
        for(ActionListener e : this.updateUiListeners){
            e.actionPerformed(new ActionEvent("Server",99999,"Update the UI"));
        }
    }


}
