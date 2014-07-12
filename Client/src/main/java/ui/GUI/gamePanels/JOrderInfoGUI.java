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

public class JOrderInfoGUI extends JTextArea {
	/**
	 * Das aktuelle Spiel
	 */
	private final IGame game;
	/**
	 * Spieler des jeweilligen Clients
	 */
	private final IPlayer cPlayer;
	/**
	 * Text über die Order des jeweilligen Spielers
	 */
	private final JTextArea info = new JTextArea("");

	/**
	 * Konstruktor
	 * @param game aktuelles Spiel
	 * @param cPlayer Spieler des jeweilligen Clients
	 */
	
	public JOrderInfoGUI(IGame game, IPlayer cPlayer){
		super();
		this.cPlayer = cPlayer;
		this.info.setWrapStyleWord(true);
		this.setLayout(new GridLayout(2,1));
		this.info.setLineWrap(true);
		this.game = game;
		setContext();
	}
	/**
	 * Setzt den Infotext
	 * @throws RemoteException
	 * @throws GameNotStartedException
	 */
	private void setInfo() throws RemoteException, GameNotStartedException{
		if(this.game.getCurrentGameState() == IGame.gameStates.RUNNING){
                this.info.setText(this.cPlayer.getOrder().toStringRemote());
		}
	}
	/**
	 * setzt den Kontext
	 */
	private void setContext(){
		this.add(this.info);
	}
	/**
	 * Updatefunktion
	 * @throws RemoteException
	 * @throws GameNotStartedException
	 */
	public void update() throws RemoteException, GameNotStartedException{
		setContext();
	}

}
