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

package ui.GUI.country;
import interfaces.IFight;
import interfaces.ITurn;
import interfaces.data.ICountry;
import interfaces.data.IPlayer;
import server.logic.ClientEventProcessor;
import ui.GUI.utils.JExceptionDialog;
import ui.GUI.utils.JModalDialog;
import exceptions.*;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

/**
 * Created by Stefan on 09.06.14.
 */
public class JCountryFightMenu extends JCountryNeighborsMenu {

    private final ITurn turn;
    private final ICountry country;
	private final IPlayer clientPlayer;
    private final ClientEventProcessor remoteEventProcessor;
    public class NeighborActionListener implements ActionListener{

        /**
         * Invoked when an action occurs.
         *
         * @param event
         */
        @Override
        public void actionPerformed(ActionEvent event){
            if(event.getActionCommand() == "onCountryClick"){
                ICountry from    =  JCountryFightMenu.this.country;
                ICountry to      =  JCountryFightMenu.this.getSelectedNeighborsMenuItem().getCountry();
                IFight fight;
                try {
                    fight = JCountryFightMenu.this.turn.fight(from, to, clientPlayer);
                }catch (TurnNotInCorrectStepException | NotYourTurnException | TurnNotAllowedStepException | ToManyNewArmysException | NotTheOwnerException | RemoteException | RemoteCountryNotFoundException e ){
                    new JExceptionDialog(JCountryFightMenu.this,e);
                    return;
                }
                JPopupMenu menu = (JPopupMenu) JCountryFightMenu.this.getParent();
                try {
                    JModalDialog modal = new JFightGUI(menu.getInvoker(),fight,JCountryFightMenu.this.remoteEventProcessor, clientPlayer);
                    SwingUtilities.invokeLater(modal);
                }catch (RemoteException e){
                    new JExceptionDialog(JCountryFightMenu.this,e);
                    return;
                }

            }
        }
    }
    public JCountryFightMenu(final ICountry country, final ITurn turn, final ClientEventProcessor remoteEventProcessor, IPlayer clientPlayer) throws RemoteException{
        super("Fight",country);
        this.country = country;
        this.clientPlayer = clientPlayer;
        this.turn = turn;
        this.remoteEventProcessor = remoteEventProcessor;
        this.addActionListener(new NeighborActionListener());

    }
}
