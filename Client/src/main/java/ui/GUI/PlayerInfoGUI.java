package main.java.ui.GUI;

import java.util.List;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;
import main.java.logic.*;
import main.java.logic.data.*;
import main.java.logic.exceptions.GameNotStartedException;

public class PlayerInfoGUI extends DefaultTableModel{
	private Game game;
	
	public PlayerInfoGUI(final Game game, final Vector<String> columnNames) throws GameNotStartedException{
		super();
		this.game = game;
		this.columnIdentifiers = columnNames;
		setDataVector(this.game.getPlayers());
	}
	
	public void setDataVector(final List<Player> players) throws GameNotStartedException{
        final Vector<Vector<String>> rows = new Vector<Vector<String>>();
        for (final Player player : players) {
            final Vector<String> playerInVector = new Vector<String>();
            playerInVector.add("" + player.getName());
            playerInVector.add("" + player.getCountries().size());
            playerInVector.add("");
            //playerInVector.add(player == game.getCurrentRound().getCurrentPlayer()? "X" : "");
            rows.add(playerInVector);
        }
        this.setDataVector(rows, columnIdentifiers);
	}

}
