package main.java.ui.GUI.utils.tableModels;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;

import interfaces.IGame;
import interfaces.data.IPlayer;
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
	public void setDataVector(final List<IPlayer> players) throws RemoteException{
        final Vector<Vector<String>> rows = new Vector<Vector<String>>();
        for (final IPlayer player : players) {
            final Vector<String> playerInVector = new Vector<String>();
            playerInVector.add("" + player.getName());
            playerInVector.add("" + player.getCountries().size());
            if (this.game.getCurrentGameState() == IGame.gameStates.RUNNING){
                try {
                    playerInVector.add(player == game.getCurrentRound().getCurrentPlayer()? "X" : "");
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
