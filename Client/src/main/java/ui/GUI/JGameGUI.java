package main.java.ui.GUI;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import main.java.logic.*;
import main.java.logic.data.Player;
import main.java.logic.exceptions.GameNotStartedException;
import main.java.ui.CUI.utils.IO;
import main.java.ui.GUI.gamePanels.JCurrentStateInfoGUI;
import main.java.ui.GUI.gamePanels.JMapGUI;
import main.java.ui.GUI.gamePanels.JOrderInfoGUI;
import main.java.ui.GUI.gamePanels.JPLayerInfoGUI;
import main.java.ui.GUI.utils.JExceptionDialog;

public class JGameGUI extends JFrame {
	private final Game game;
	private Container pane;
	private JButton update;
	private final Player player;
	private final JMapGUI map;
	
	public JGameGUI(Game game, Player player) throws GameNotStartedException{
		super("Risiko");
		this.game = game;
		this.player = player;
		this.map = new JMapGUI(game);
		initialize();
	}
	
	private void initialize() throws GameNotStartedException{
		//this.setSize(600, 400);
		//this.setPreferredSize(this.getSize());

        // Klick auf Kreuz (Fenster schließen) behandeln lassen:
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pane = this.getContentPane();
        pane.setLayout(new BorderLayout());
        pane.add(this.map, BorderLayout.CENTER);
        pane.add(setSouthPanel(), BorderLayout.SOUTH);
		// Fenster anzeigen
		this.setVisible(true);
		this.pack();
		
	}
	
	private JPanel setSouthPanel() throws GameNotStartedException{
		JPanel south = new JPanel();
        south.setLayout(new GridLayout(1, 4, 10, 0));
        
        //JPlayerInfoGUI erzeugen
        final JPLayerInfoGUI playersInfo = new JPLayerInfoGUI(this.game);
        
        //JOrderInfoGUI erzeugen
        final JOrderInfoGUI orderInfo = new JOrderInfoGUI(this.game, this.player);
        
        //JCurrentStateInfoGUI erzeugen
        final JCurrentStateInfoGUI currentStateInfo = new JCurrentStateInfoGUI(this.game, this.player);
        
        //Update Button
        this.update = new JButton("Update");

        
        //Panel
        south.add(playersInfo.getContext());
        south.add(currentStateInfo.getContext());
        south.add(orderInfo.getContext());
        south.add(this.update);
        
        south.setBorder(BorderFactory.createTitledBorder("Übersicht"));
        
        update.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent ae) {
                try {
                    playersInfo.update();
                    orderInfo.update();
                    currentStateInfo.update();
                    map.repaint();
                } catch (GameNotStartedException e) {
                    new JExceptionDialog(JGameGUI.this,e);
                    return;
                }
            }

        });
        
        return south;
	}
	
}
