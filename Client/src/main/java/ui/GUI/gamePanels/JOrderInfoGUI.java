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



package ui.GUI.gamePanels;

import javax.swing.*;

import exceptions.GameNotStartedException;
import ui.GUI.utils.JExceptionDialog;
import interfaces.IGame;
import interfaces.data.IPlayer;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

public class JOrderInfoGUI extends JPanel {

	private final IGame game;
	private JButton hide = new JButton("Uncover Order");
	private boolean hidden = true;
	private final JTextArea info = new JTextArea("");

	
	private class HideActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			try {
				if(hidden){
					hide.setText("Hide Order");
					setInfo();
					hidden = false;
				}else if(!hidden){
					hide.setText("Uncover Order");
					if(game.getCurrentGameState() == IGame.gameStates.RUNNING){
		                info.setText("");
					}
					hidden = true;
				}
			} catch (RemoteException | GameNotStartedException e) {
				new JExceptionDialog(e);
				return;
			}
		}
		
	}
	
	public JOrderInfoGUI(IGame game){
		super();
		this.info.setWrapStyleWord(true);
		this.setLayout(new GridLayout(2,1));
		this.info.setLineWrap(true);
		this.game = game;
		this.hide.addActionListener(new HideActionListener());
		setContext();
	}
	
	private void setInfo() throws RemoteException, GameNotStartedException{
		if(this.game.getCurrentGameState() == IGame.gameStates.RUNNING){
                this.info.setText(this.game.getCurrentRound().getCurrentPlayer().getOrder().toStringRemote());
		}
	}
	private void setContext(){
		this.add(this.info);
		this.add(this.hide);
	}
	
	public void update() throws RemoteException, GameNotStartedException{
		setContext();
	}

}
