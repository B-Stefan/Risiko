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



package Client.ui.GUI.gamePanels;

import java.awt.Dimension;
import java.rmi.RemoteException;
import java.util.Vector;

import javax.swing.JScrollPane;
import javax.swing.JTable;

import commons.interfaces.IGame;
import Client.ui.GUI.utils.tableModels.PlayerInfoTableModel;


public class JPLayerInfoGUI extends JScrollPane{
	/**
	 * aktuelles Spiel
	 */
	private final IGame game;
	/**
	 * Model der Tabelle mit Spielern
	 */
	private PlayerInfoTableModel tModel;

    /**
	 * Konstruktor
	 * @param game aktuelles Spiel
	 * @throws RemoteException
	 */
	public JPLayerInfoGUI(IGame game) throws RemoteException{
        super();
		this.game = game;
        final Vector<String> spalten = new Vector<String>();
        spalten.add("Spieler");
        spalten.add("Länder");
        spalten.add("Am Zug");

        //Spieler Liste
        this.tModel = new PlayerInfoTableModel(this.game, spalten);
        /*
        * Tabelle mit Spielern
	    */
        JTable playersTable = new JTable(this.tModel);
        this.getViewport().add(playersTable);
        this.setPreferredSize(new Dimension(150, 105));
	}
	/**
	 * Zum Updaten der Tabelle
	 * @throws RemoteException
	 */
	public void update() throws RemoteException{
		this.tModel.setDataVector(game.getPlayers());
	}
}
