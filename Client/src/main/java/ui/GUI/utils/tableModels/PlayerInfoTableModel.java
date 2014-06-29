package main.java.ui.GUI.utils.tableModels;

import java.util.List;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;
import logic.*;
import logic.data.*;
import exceptions.GameNotStartedException;

/**
 * Klasse die zur Anzeige eines Spielers in einer JTable dient
 * @see javax.swing.JTable
 *
 */
public class PlayerInfoTableModel extends DefaultTableModel{
    /**
     * Spiel mit dem Playern die angezeigt werden sollen
     */
	private final Game game;


    /**
     * Klasse, die zur Anzeige aller Spieler in einer JTable dient.
     *
     * @see javax.swing.JTable
     *
     * @param game Spiel mit den Spielern
     * @param columnNames Spaltenüberschriften
     */
	public PlayerInfoTableModel(final Game game, final Vector<String> columnNames) {
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
	public void setDataVector(final List<Player> players) {
        final Vector<Vector<String>> rows = new Vector<Vector<String>>();
        for (final Player player : players) {
            final Vector<String> playerInVector = new Vector<String>();
            playerInVector.add("" + player.getName());
            playerInVector.add("" + player.getCountries().size());
            if (this.game.getCurrentGameState() == Game.gameStates.RUNNING){
                try {
                    playerInVector.add(player == game.getCurrentRound().getCurrentPlayer()? "X" : "");
                }catch (GameNotStartedException e){
                    playerInVector.add("");
                }
            }else if (this.game.getCurrentGameState() == Game.gameStates.WAITING){
            	playerInVector.add("");
        	}
            rows.add(playerInVector);
        }
        this.setDataVector(rows, columnIdentifiers);
	}
}
