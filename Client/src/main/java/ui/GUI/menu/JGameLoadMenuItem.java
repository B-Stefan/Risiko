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



package ui.GUI.menu;

import exceptions.GameNotFoundException;
import exceptions.PersistenceEndpointIOException;
import interfaces.IGame;
import ui.GUI.JGameManagerGUI;
import ui.GUI.utils.JExceptionDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

public class JGameLoadMenuItem extends JMenuItem {
    private final IGame game;
    private final JGameManagerGUI gameManagerGUI;

    private class OnItemClickedListener implements ActionListener {

        /**
         * Invoked when an action occurs.
         *
         * @param event
         */
        @Override
        public void actionPerformed(ActionEvent event) {
            try {
                JGameLoadMenuItem.this.gameManagerGUI.openGameGUI(JGameLoadMenuItem.this.game);
            }catch (RemoteException | PersistenceEndpointIOException| GameNotFoundException e){
                new JExceptionDialog(JGameLoadMenuItem.this,e);
            }
        }
    }

    public JGameLoadMenuItem (IGame game, JGameManagerGUI gameManagerGUI) throws RemoteException{
        super(game.toStringRemote());
        this.gameManagerGUI = gameManagerGUI;
        this.game = game;
        this.addActionListener(new OnItemClickedListener());
    }
}
