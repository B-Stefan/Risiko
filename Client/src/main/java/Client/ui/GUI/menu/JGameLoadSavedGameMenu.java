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



package Client.ui.GUI.menu;

import commons.exceptions.PersistenceEndpointIOException;
import commons.interfaces.IGame;
import commons.interfaces.IGameManager;
import Client.ui.GUI.JGameManagerGUI;
import Client.ui.GUI.utils.JExceptionDialog;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.List;


/**
 * Klasse die zur Anzeige der gespeicherten Speile dient
 */
public class JGameLoadSavedGameMenu extends JMenu{


    private final JGameManagerGUI gameManagerGUI;
    /**
     * Wird augerufen, wenn der Server ein Update sendet
     */
    private class UpdateUIListener implements ActionListener {

        /**
         * Invoked when an action occurs.
         *
         * @param event
         */
        @Override
        public void actionPerformed(ActionEvent event) {
            try{
                update();
            }catch (RemoteException e){
                new JExceptionDialog(JGameLoadSavedGameMenu.this,e);
            }
        }
    }
    private final IGameManager manager;
    public JGameLoadSavedGameMenu(IGameManager manager, JGameManagerGUI gameManagerGUI) throws RemoteException{
        super("Spiel laden");
        this.gameManagerGUI = gameManagerGUI;
        this.manager = manager;
        this.update();
    }

    /**
     * Läd die gespeicherten Spiele vom Server und zeigt diese an
     * @throws RemoteException
     */
    private void update() throws RemoteException{
        this.removeAll();
        List<? extends IGame> savedGames;
        try {
            savedGames = this.manager.getSavedGameList();
        }catch (PersistenceEndpointIOException e) {
            new JExceptionDialog(this, e);
            return;
        }

        //Add saved games to Meue
        for(IGame savedGame : savedGames){
            JMenuItem item = new JGameLoadMenuItem(savedGame,this.gameManagerGUI);
            this.add(item);
        }
    }
}
