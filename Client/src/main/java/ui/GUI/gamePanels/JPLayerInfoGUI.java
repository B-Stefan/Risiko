package main.java.ui.GUI.gamePanels;

import java.awt.Dimension;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import main.java.logic.Game;
import main.java.logic.exceptions.GameNotStartedException;
import main.java.ui.GUI.utils.tableModels.PlayerInfoTableModel;


public class JPLayerInfoGUI extends JFrame{
	private final Game game;
	private PlayerInfoTableModel tModel;
	private JScrollPane context;
	
	
	public JPLayerInfoGUI(Game game){
		this.game = game;
		setContext();
	}
	
	private void setContext(){
        // ListModel als "Datencontainer" anlegen:
        final Vector<String> spalten = new Vector<String>();
        spalten.add("Spieler");
        spalten.add("Länder");
        spalten.add("Am Zug");
        
        //Spieler Liste
        this.tModel = new PlayerInfoTableModel(this.game, spalten);
        JTable playersTable = new JTable(this.tModel);
        this.context = new JScrollPane(playersTable);
        this.context.setPreferredSize(new Dimension(150, 105));
	}
	
	public PlayerInfoTableModel getTModel(){
		return this.tModel;
	}
	
	public JScrollPane getContext(){
		return this.context;
	}
	public void update() throws GameNotStartedException{
		this.tModel.setDataVector(game.getPlayers());
	}
}
