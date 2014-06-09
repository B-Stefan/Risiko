package main.java.gui.GUI;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

import main.java.logic.*;

public class GameGUI extends JFrame {
	private final Game game;
	
	public GameGUI(Game game){
		super("Risiko");
		this.game = game;
		initialize();
	}
	
	private void initialize(){
		this.setSize(600, 400);

        // Klick auf Kreuz (Fenster schließen) behandeln lassen:
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Container pane = this.getContentPane();
        pane.setLayout(new BorderLayout());
        pane.add(new JLabel("Test"), BorderLayout.NORTH);
        pane.add(new JLabel("Hallo"), BorderLayout.CENTER);
        pane.add(new JLabel("Risiko"), BorderLayout.SOUTH);
		// Fenster anzeigen
		this.setVisible(true);
		this.pack();
	}
}
