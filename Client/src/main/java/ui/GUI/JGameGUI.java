package ui.GUI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.Remote;
import java.rmi.RemoteException;

import javax.swing.*;

import exceptions.GameNotStartedException;
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
            }catch (RemoteException | GameNotStartedException e){
                new JExceptionDialog(JGameGUI.this,e);
            }
        }
    }


    /**
     * Wird augerufen, wenn der Server ein Update des UI verlangt
     */
    private class OpenFightUIListener implements IFightActionListener{

        private JFightGUI fightGUI;
        /**
         * Invoked when an action occurs.
         *
         * @param fight the Fight to show
         */
        @Override
        public void actionPerformed(IFight fight) {
            try{
                fightGUI = new JFightGUI(JGameGUI.this.map,fight,remoteEventProcessor);
                SwingUtilities.invokeLater(fightGUI);

            }catch (RemoteException e){
                new JExceptionDialog(JGameGUI.this,e);

            }
        }
    }


    public JGameGUI(IGame game, IPlayer player, ClientEventProcessor remoteEventProcessor) throws RemoteException{
		super("Risiko" + player.getName());
		this.game = game;
		this.player = player;
		this.map = new JMapGUI(game,remoteEventProcessor);
        this.update = new JButton("Update");
        this.menuBar = new JMenuBar();
        this.playersInfo = new JPLayerInfoGUI(this.game);
        this.orderInfo = new JOrderInfoGUI(this.game);
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
		this.pack();
        this.setVisible(true);
		
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

    public void update () throws RemoteException, GameNotStartedException{
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
