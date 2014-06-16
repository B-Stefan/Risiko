package main.java.ui.GUI.utils.tableModels;

import main.java.logic.Game;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.util.Vector;

public class GameTableModel extends DefaultTableModel{
	private List<Game> games;

    public GameTableModel(final List<Game> games) {
        super();
        this.games = games;
        this.columnIdentifiers = new Vector<String>();
        this.columnIdentifiers.add("Name");
        this.columnIdentifiers.add("# Player");
        setDataVector(this.games);
    }

    public GameTableModel(final List<Game> games, final Vector<String> columnNames) {
		super();
		this.games = games;
		this.columnIdentifiers = columnNames;
		setDataVector(this.games);
	}
	
	public void setDataVector(final List<Game> games){
        final Vector<Vector<String>> rows = new Vector<Vector<String>>();
        for (final Game game : games) {
            final Vector<String> gameInVector = new Vector<String>();
            gameInVector.add("SavedGame");
            gameInVector.add(game.getPlayers().size() + " Player");
            rows.add(gameInVector);
        }
        this.setDataVector(rows, columnIdentifiers);
	}
    @Override
    public boolean isCellEditable(int row, int column) {
        //Alle Zellen sind nicht editierbar
        return false;
    }
}
