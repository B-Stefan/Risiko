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



package Client.ui.GUI.utils.tableModels;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;

import commons.interfaces.IGame;
import commons.interfaces.data.IPlayer;
import commons.exceptions.GameNotStartedException;

/**
 * Klasse die zur Anzeige eines Spielers in einer JTable dient
 * @see javax.swing.JTable
 *
 */
public class PlayerInfoTableModel extends DefaultTableModel{
    /**
     * Spiel mit dem Playern die angezeigt werden sollen
     */
	private final IGame game;


    /**
     * Klasse, die zur Anzeige aller Spieler in einer JTable dient.
     *
     * @see javax.swing.JTable
     *
     * @param game Spiel mit den Spielern
     * @param columnNames Spaltenüberschriften
     */
	public PlayerInfoTableModel(final IGame game, final Vector<String> columnNames) throws RemoteException{
		super();
		this.game = game;
		this.columnIdentifiers = columnNames;
		setDataVector(this.game.getPlayers());
	}

    /**
     * Setzt die Zeilen und Spalten für die JTable
     *
     * @see javax.swing.JTable
     *
     * @param players Spieler, die in der Liste auftauchen sollen
     */
	public void setDataVector(final List<? extends IPlayer> players) throws RemoteException{
        final Vector<Vector<String>> rows = new Vector<Vector<String>>();
        for (final IPlayer player : players) {
            final Vector<String> playerInVector = new Vector<String>();
            playerInVector.add("" + player.getName());
            playerInVector.add("" + player.getCountries().size());
            if (this.game.getCurrentGameState() == IGame.gameStates.RUNNING){
                try {
                    playerInVector.add(player.equals(game.getCurrentRound().getCurrentPlayer())? "X" : "");
                }catch (GameNotStartedException e){
                    playerInVector.add("");
                }
            }else if (this.game.getCurrentGameState() == IGame.gameStates.WAITING){
            	playerInVector.add("");
        	}
            rows.add(playerInVector);
        }
        this.setDataVector(rows, columnIdentifiers);
	}
}
