package ui.GUI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.Remote;
import java.rmi.RemoteException;

import javax.swing.*;

import interfaces.IClient;
import interfaces.IFight;
import interfaces.IGame;
import interfaces.data.IPlayer;
import server.logic.ClientEventProcessor;
import server.logic.IFightActionListener;
import ui.GUI.country.JFightGUI;
import ui.GUI.gamePanels.*;
import ui.GUI.menu.JGameMenu;
import ui.GUI.utils.JExceptionDialog;

public class JGameGUI extends JFrame {
	private final IGame game;
	private final JButton update;
	private final IPlayer player;
	private final JMapGUI map;
    private final JMenuBar menuBar;
    private final JPLayerInfoGUI playersInfo;
    private final JOrderInfoGUI orderInfo;
    private final JCurrentStateInfoGUI currentStateInfoGUI;
    private final JCardInfo cardInfo;
    private final ClientEventProcessor remoteEventProcessor;

    /**
     * Wird augerufen, wenn der Server ein Update des UI verlangt
     */
    private class UpdateUIListener implements ActionListener{

        /**
         * Invoked when an action occurs.
         *
         * @param event
         */
        @Override
        public void actionPerformed(ActionEvent event) {
            try {
                JGameGUI.this.update();
            }catch (RemoteException e){
                new JExceptionDialog(JGameGUI.this,e);
            }
        }
    }


    /**
     * Wird augerufen, wenn der Server ein Update des UI verlangt
     */
    private class OpenFightUIListener implements IFightActionListener{

        /**
         * Invoked when an action occurs.
         *
         * @param fight the Fight to show
         */
        @Override
        public void actionPerformed(IFight fight) {
            try{
                SwingUtilities.invokeLater(new JFightGUI(JGameGUI.this.map,fight));

            }catch (RemoteException e){
                new JExceptionDialog(JGameGUI.this,e);
            }

        }
    }


    public JGameGUI(IGame game, IPlayer player, ClientEventProcessor remoteEventProcessor) throws RemoteException{
		super("Risiko");
		this.game = game;
		this.player = player;
		this.map = new JMapGUI(game);
        this.update = new JButton("Update");
        this.menuBar = new JMenuBar();
        this.playersInfo = new JPLayerInfoGUI(this.game);
        this.orderInfo = new JOrderInfoGUI(this.game, this.player);
        this.currentStateInfoGUI =   new JCurrentStateInfoGUI(this.game, this.player, this);
        this.cardInfo =  new JCardInfo(this.player, this.game.getDeck());
        this.remoteEventProcessor = remoteEventProcessor;

        /**
         * Hinzufügen der Listeneners für Events, die vom Server gesnedet werden
         *
         */

        this.remoteEventProcessor.addUpdateUIListener(new UpdateUIListener());
        this.remoteEventProcessor.addFightListener(new OpenFightUIListener());

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

    public void update () throws RemoteException{
        playersInfo.update();
        orderInfo.update();
        currentStateInfoGUI.update();
        cardInfo.update();
        map.repaint();
    }
    public IPlayer getPlayer(){
        return this.player;
    }

    public IGame getGame(){
        return this.game;
    }
}
