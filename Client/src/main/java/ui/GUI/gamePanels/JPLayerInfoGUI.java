package ui.GUI.gamePanels;

import java.awt.Dimension;
import java.rmi.RemoteException;
import java.util.Vector;

import javax.swing.JScrollPane;
import javax.swing.JTable;

import interfaces.IGame;
import main.java.ui.GUI.utils.tableModels.PlayerInfoTableModel;


public class JPLayerInfoGUI extends JScrollPane{
	private final IGame game;
	private PlayerInfoTableModel tModel;
    private final JTable playersTable;
	
	
	public JPLayerInfoGUI(IGame game) throws RemoteException{
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

	public void update() throws RemoteException{
		this.tModel.setDataVector(game.getPlayers());
	}
}
