package main.java.ui.GUI;

import java.util.List;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;
import main.java.logic.*;
import main.java.logic.data.*;
import main.java.logic.exceptions.GameNotStartedException;

public class PlayerInfoManager extends DefaultTableModel{
	private Game game;
	
	public PlayerInfoManager(final Game game, final Vector<String> columnNames) throws GameNotStartedException{
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
            if (this.game.getCurrentGameState() == Game.gameStates.RUNNING){
            	playerInVector.add(player == game.getCurrentRound().getCurrentPlayer()? "X" : "");
            }else if (this.game.getCurrentGameState() == Game.gameStates.WAITING){
            	playerInVector.add("");
        	}
            rows.add(playerInVector);
        }
        this.setDataVector(rows, columnIdentifiers);
	}
	
	public void update() throws GameNotStartedException{
		setDataVector(game.getPlayers());
	}

}
