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

import exceptions.PersistenceEndpointIOException;
import interfaces.IGame;
import interfaces.IGameManager;
import ui.GUI.JGameManagerGUI;
import ui.GUI.utils.JExceptionDialog;

import javax.swing.*;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Created by Stefan on 30.06.14.
 */
public class JGameLoadRunningGameMenu extends JMenu {

    private final IGameManager manager;

    public JGameLoadRunningGameMenu(IGameManager manager, JGameManagerGUI GameManagerGUI) throws RemoteException{
        super("Spiel beitreten");
        this.manager = manager;

        List<IGame> runningGames;
        try {
            runningGames = this.manager.getRunningGameList();
        }catch (PersistenceEndpointIOException e){
            new JExceptionDialog(this,e);
            return;
        }

        //Add saved games to Meue
        for(IGame runningGame : runningGames){
            JMenuItem item = new JGameLoadMenuItem(runningGame,GameManagerGUI);
            this.add(item);
        }
    }
}
