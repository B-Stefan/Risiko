/*
 * RISIKO-JAVA - Game, Copyright 2014  Jennifer Theloy, Stefan Bieliauskas  -  All Rights Reserved.
 * Hochschule Bremen - University of Applied Sciences
 *
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 * Contact:
 *     Jennifer Theloy: jTheloy@stud.hs-bremen.de
 *     Stefan Bieliauskas: sBieliauskas@stud.hs-bremen.de
 *
 * Web:
 *     https://github.com/B-Stefan/Risiko
 *
 */

package Client.ui.GUI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

import javax.swing.*;

import commons.exceptions.GameNotStartedException;
import commons.interfaces.IFight;
import commons.interfaces.IGame;
import commons.interfaces.data.IPlayer;
import server.logic.ClientEventProcessor;
import server.logic.IFightActionListener;
import Client.ui.GUI.country.JFightGUI;
import Client.ui.GUI.gamePanels.*;
import Client.ui.GUI.menu.JGameMenu;
import Client.ui.GUI.utils.JExceptionDialog;

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
                fightGUI = new JFightGUI(JGameGUI.this.map,fight,remoteEventProcessor, player);
                SwingUtilities.invokeLater(fightGUI);

            }catch (RemoteException e){
                new JExceptionDialog(JGameGUI.this,e);

            }
        }
    }


    public JGameGUI(IGame game, IPlayer player, ClientEventProcessor remoteEventProcessor) throws RemoteException{
		super("Risiko - " + player.getName());
		this.game = game;
		this.player = player;
		this.map = new JMapGUI(game,remoteEventProcessor, player);
        this.update = new JButton("Update");
        this.menuBar = new JMenuBar();
        this.playersInfo = new JPLayerInfoGUI(this.game);
        this.orderInfo = new JOrderInfoGUI(this.game, this.player);
        this.currentStateInfoGUI =   new JCurrentStateInfoGUI(this.game, this, this.player);
        this.cardInfo =  new JCardInfo(this.player, this.game);
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
        // Klick auf Kreuz (Fenster schließen) behandeln lassen:
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Container pane = this.getContentPane();
        pane.setLayout(new BorderLayout());
        pane.add(this.map, BorderLayout.CENTER);
        pane.add(setSouthPanel(), BorderLayout.SOUTH);
		// Fenster anzeigen
        this.menuBar.add(new JGameMenu(this.game));
        this.setJMenuBar(menuBar);
		this.setSize(1200, 1000);
		this.setPreferredSize(this.getSize());
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
