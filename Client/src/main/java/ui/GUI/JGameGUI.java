package main.java.ui.GUI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import main.java.logic.*;
import main.java.logic.data.Player;
import main.java.ui.GUI.gamePanels.*;
import main.java.ui.GUI.menu.JGameMenu;

public class JGameGUI extends JFrame {
	private final Game game;
	private final JButton update;
	private final Player player;
	private final JMapGUI map;
    private final JMenuBar menuBar;
    private final JPLayerInfoGUI playersInfo;
    private final JOrderInfoGUI orderInfo;
    private final JCurrentStateInfoGUI currentStateInfoGUI;
    private final JCardInfo cardInfo;

    public JGameGUI(Game game, Player player){
		super("Risiko");
		this.game = game;
		this.player = player;
		this.map = new JMapGUI(game);
        this.update = new JButton("Update");
        this.menuBar = new JMenuBar();
        this.playersInfo = new JPLayerInfoGUI(this.game);
        this.orderInfo = new JOrderInfoGUI(this.game, this.player);
        this.currentStateInfoGUI =   new JCurrentStateInfoGUI(this.game, this.player, this);
        this.cardInfo =  new JCardInfo(this.player);
		initialize();
	}
	
	private void initialize() {
		//this.setSize(600, 400);
		//this.setPreferredSize(this.getSize());

        // Klick auf Kreuz (Fenster schließen) behandeln lassen:
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Container pane = this.getContentPane();
        pane.setLayout(new BorderLayout());
        pane.add(this.map, BorderLayout.CENTER);
        pane.add(setSouthPanel(), BorderLayout.SOUTH);
		// Fenster anzeigen
        this.menuBar.add(new JGameMenu(this.game));
        this.setJMenuBar(menuBar);
		this.setVisible(true);
		this.pack();
		
	}
	
	private JPanel setSouthPanel() {
		JPanel south = new JPanel();
        south.setLayout(new GridLayout(1, 4, 10, 0));

        //Panel
        south.add(playersInfo);
        south.add(currentStateInfoGUI);
        south.add(orderInfo);
        south.add(cardInfo.getContext());
        
        south.setBorder(BorderFactory.createTitledBorder("Übersicht"));
        return south;
	}

    public void update (){
        playersInfo.update();
        orderInfo.update();
        currentStateInfoGUI.update();
        map.repaint();
    }
    public Player getPlayer(){
        return this.player;
    }

    public Game getGame(){
        return this.game;
    }
}
