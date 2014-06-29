package main.java.ui.GUI.gamePanels;

import java.awt.Color;
import java.awt.Dimension;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import main.java.logic.Game;
import main.java.logic.exceptions.GameNotStartedException;
import main.java.ui.GUI.utils.tableModels.PlayerInfoTableModel;


public class JPLayerInfoGUI extends JScrollPane{
	private final Game game;
	private PlayerInfoTableModel tModel;
    private final JTable playersTable;
	
	
	public JPLayerInfoGUI(Game game){
        super();
		this.game = game;
        final Vector<String> spalten = new Vector<String>();
        spalten.add("Spieler");
        spalten.add("Länder");
        spalten.add("Am Zug");

        //Spieler Liste
        this.tModel = new PlayerInfoTableModel(this.game, spalten);
        this.playersTable = new JTable(this.tModel);
        this.getViewport().add(playersTable);
        this.setPreferredSize(new Dimension(150, 105));
	}

	public void update() {
		this.tModel.setDataVector(game.getPlayers());
	}
}
