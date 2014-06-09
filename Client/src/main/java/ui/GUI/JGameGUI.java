package main.java.ui.GUI;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
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
import main.java.ui.CUI.utils.IO;

public class JGameGUI extends JFrame {
	private final Game game;
	private Container pane;
	private JButton update;
	
	public JGameGUI(Game game) throws GameNotStartedException{
		super("Risiko");
		this.game = game;
		this.game.addPlayer(new Player("Bob"));
		this.game.addPlayer(new Player("Raito"));
		this.game.addPlayer(new Player("Daichi"));
		this.game.addPlayer(new Player("Steve"));
		this.game.addPlayer(new Player("Alice"));
		initialize();
	}
	
	private void initialize() throws GameNotStartedException{
		//this.setSize(600, 400);
		//this.setPreferredSize(this.getSize());

        // Klick auf Kreuz (Fenster schließen) behandeln lassen:
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pane = this.getContentPane();
        pane.setLayout(new BorderLayout());
        pane.add(new JMapGUI(game.getMap()), BorderLayout.CENTER);
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
        spalten.add("Länder");
        spalten.add("Am Zug");
        
        //Spieler Liste
        final PlayerInfoGUI tModel = new PlayerInfoGUI(this.game, spalten);
        JTable playersTable = new JTable(tModel);
        final JScrollPane east = new JScrollPane(playersTable);
        east.setPreferredSize(new Dimension(150, 105));
        
        //Update Button
        this.update = new JButton("Update");
        
        //Panel
        south.add(east);
        south.add(this.update);
        south.add(new JLabel("Risiko"));
        south.add(new JLabel("Risiko"));
        
        south.setBorder(BorderFactory.createTitledBorder("Übersicht"));
        
        update.addActionListener(new ActionListener(){
			public void actionPerformed(final ActionEvent ae){
				try {
					tModel.update();
				} catch (GameNotStartedException e) {
					IO.println(e.getMessage());
					return;
				}
			}

		});
        
        return south;
	}
	
}
