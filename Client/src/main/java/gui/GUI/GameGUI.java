package main.java.gui.GUI;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import main.java.logic.*;
import main.java.logic.data.Player;
import main.java.logic.exceptions.GameNotStartedException;

public class GameGUI extends JFrame {
	private final Game game;
	
	public GameGUI(Game game) throws GameNotStartedException{
		super("Risiko");
		this.game = game;
		this.game.addPlayer(new Player("Bob"));
		this.game.addPlayer(new Player("C"));
		this.game.addPlayer(new Player("D"));
		initialize();
	}
	
	private void initialize() throws GameNotStartedException{
		this.setSize(600, 400);
		this.setPreferredSize(this.getSize());

        // Klick auf Kreuz (Fenster schließen) behandeln lassen:
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Container pane = this.getContentPane();
        pane.setLayout(new BorderLayout());
        pane.add(new JLabel("Hallo"), BorderLayout.CENTER);
        pane.add(setSouthPanel(), BorderLayout.SOUTH);
		// Fenster anzeigen
		this.setVisible(true);
		this.pack();
	}
	
	private JPanel setSouthPanel() throws GameNotStartedException{
		JPanel south = new JPanel();
        south.setLayout(new GridLayout(1, 4));
      
        // ListModel als "Datencontainer" anlegen:
        final Vector<String> spalten = new Vector<String>();
        spalten.add("Spieler");
        spalten.add("Anzahl Länder");
        spalten.add("Aktueller Spieler");
        
        final PlayerInfoGUI tModel = new PlayerInfoGUI(this.game, spalten);
        JTable playersTable = new JTable(tModel);
        final JScrollPane east = new JScrollPane(playersTable);

        south.add(east);
        south.add(new JTextField());
        south.add(new JLabel("Risiko"));
        south.add(new JLabel("Risiko"));
        
        return south;
	}
	
}
