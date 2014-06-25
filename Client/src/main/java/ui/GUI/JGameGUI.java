package main.java.ui.GUI;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;

import main.java.logic.*;
import main.java.logic.data.Player;
import main.java.ui.GUI.gamePanels.JCardInfo;
import main.java.ui.GUI.gamePanels.JCurrentStateInfoGUI;
import main.java.ui.GUI.gamePanels.JMapGUI;
import main.java.ui.GUI.gamePanels.JOrderInfoGUI;
import main.java.ui.GUI.gamePanels.JPLayerInfoGUI;

public class JGameGUI extends JFrame {
	private final Game game;
	private Container pane;
	private JButton update;
	private final Player player;
	private final JMapGUI map;
	
	public JGameGUI(Game game, Player player){
		super("Risiko");
		this.game = game;
		this.player = player;
		this.map = new JMapGUI(game);
		initialize();
	}
	
	private void initialize() {
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
	
	private JPanel setSouthPanel() {
		JPanel south = new JPanel();
        south.setLayout(new GridLayout(1, 4, 10, 0));
        
        //JPlayerInfoGUI erzeugen
        final JPLayerInfoGUI playersInfo = new JPLayerInfoGUI(this.game);
        
        //JOrderInfoGUI erzeugen
        final JOrderInfoGUI orderInfo = new JOrderInfoGUI(this.game, this.player);
        
        //Update Button
        this.update = new JButton("Update");
        
        //JCurrentStateInfoGUI erzeugen
        final JCurrentStateInfoGUI currentStateInfo = new JCurrentStateInfoGUI(this.game, this.player, this.update);
      
        //JCardInfo erzeugen
        final JCardInfo cardInfo = new JCardInfo(this.player);
        
        //Panel
        south.add(playersInfo.getContext());
        south.add(currentStateInfo.getContext());
        south.add(orderInfo.getContext());
        south.add(cardInfo.getContext());
        
        south.setBorder(BorderFactory.createTitledBorder("Übersicht"));
        
        update.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent ae) {

                playersInfo.update();
                orderInfo.update();
                currentStateInfo.update();
                map.repaint();

            }

        });
        
        return south;
	}
	
}
