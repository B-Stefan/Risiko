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



package ui.GUI.utils.tableModels;

import interfaces.IGame;

import javax.swing.table.DefaultTableModel;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Vector;

/**
 * Klasse die zum Darstellen eines Spiels in eine Jtable gedacht ist
 * @see javax.swing.JTable
 */
public class GameTableModel extends DefaultTableModel{

    /**
     * Spiel das in dieser Zeile angezeigt werden soll
     */
	private final List<IGame> games;

    /**
     * Klasse, die zur Anzeige eines Spiels in einer JTable dient
     * @see javax.swing.JTable
     * @param games Spiel das angezeigt werden soll
     */
    public GameTableModel(final List<IGame> games) throws RemoteException{
        super();
        this.games = games;
        this.columnIdentifiers = new Vector<String>();
        this.columnIdentifiers.add("Name");
        this.columnIdentifiers.add("# Player");
        setDataVector(this.games);
    }

    /**
     * Klasse, die zur Anzeige eines Spiels in einer JTable dient
     * @see javax.swing.JTable
     * @param games Spiel das angezeigt werden soll
     * @param columnNames Namen der Spalten
     */
    public GameTableModel(final List<IGame> games, final Vector<String> columnNames) throws RemoteException{
		super();
		this.games = games;
		this.columnIdentifiers = columnNames;
		setDataVector(this.games);
	}

    /**
     * Setzt die Daten aus dem IGame in die Rows
     * @see interfaces.IGame
     * @param games
     */
	public void setDataVector(final List<IGame> games) throws RemoteException{
        final Vector<Vector<String>> rows = new Vector<Vector<String>>();
        for (final IGame game : games) {
            final Vector<String> gameInVector = new Vector<String>();
            gameInVector.add("SavedGame");
            gameInVector.add(game.getPlayers().size() + " Player");
            rows.add(gameInVector);
        }
        this.setDataVector(rows, columnIdentifiers);
	}

    /**
     * Gibt zurück, dass eine einzelne Zelle nich
     * @param row
     * @param column
     * @return
     */
    @Override
    public boolean isCellEditable(int row, int column) {
        //Alle Zellen sind nicht editierbar
        return false;
    }
}
