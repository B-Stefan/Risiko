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
import Client.ui.GUI.JGameGUI;
import Client.ui.GUI.utils.JExceptionDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

/**
 * Created by Stefan on 25.06.14.
 */
public class JGameMenu extends JMenu{

    private final IGame game;
    private final JMenuItem saveMenuItem;

    private class SaveGameListener implements ActionListener {
        /**
         * Invoked when an action occurs.
         *
         * @param event
         */
        @Override
        public void actionPerformed(ActionEvent event) {

            JGameGUI currentGameGUI =  (JGameGUI) SwingUtilities.getRoot(JGameMenu.this);
            if(currentGameGUI == null){
                throw new RuntimeException("Kann nur in Verbindung mit einer GAMGE GUI als RootFrame benutzt werden");
            }
            try {
                JGameMenu.this.game.save();
            }catch (PersistenceEndpointIOException | RemoteException e){
                new JExceptionDialog(JGameMenu.this,e);
                return;
            }
        }
    }
    public JGameMenu(IGame game){
        super("Spiel");
        this.game = game;
        this.saveMenuItem = new JMenuItem("Speichern");
        this.saveMenuItem.addActionListener(new SaveGameListener());
        this.add(this.saveMenuItem);

    }

}
