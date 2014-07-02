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