package main.java.ui.GUI;

import java.awt.Dimension;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import main.java.logic.Game;
import main.java.logic.exceptions.GameNotStartedException;
	

public class JPLayerInfoGUI extends JFrame{
	private final Game game;
	private PlayerInfoManager tModel;
	private JScrollPane southFirst;
	
	
	public JPLayerInfoGUI(Game game) throws GameNotStartedException{
		this.game = game;
		setScrollPane();
	}
	
	private void setScrollPane() throws GameNotStartedException{
        // ListModel als "Datencontainer" anlegen:
        final Vector<String> spalten = new Vector<String>();
        spalten.add("Spieler");
        spalten.add("Länder");
        spalten.add("Am Zug");
        
        //Spieler Liste
        this.tModel = new PlayerInfoManager(this.game, spalten);
        JTable playersTable = new JTable(this.tModel);
        this.southFirst = new JScrollPane(playersTable);
        this.southFirst.setPreferredSize(new Dimension(150, 105));
	}
	
	public PlayerInfoManager getTModel(){
		return this.tModel;
	}
	
	public JScrollPane getSouthFirst(){
		return this.southFirst;
	}
}
