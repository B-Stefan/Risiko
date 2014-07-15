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



package Client.ui.GUI.country;
import commons.interfaces.ITurn;
import commons.interfaces.data.ICountry;
import commons.interfaces.data.IPlayer;
import commons.exceptions.*;
import Client.ui.GUI.utils.JExceptionDialog;
import Client.ui.GUI.utils.JModalDialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

public class JCountryMoveMenu extends JCountryNeighborsMenu {

    private final ITurn turn;
	private final IPlayer clientPlayer;
    public class NeighborActionListener implements ActionListener{

        /**
         * Invoked when an action occurs.
         *
         * @param event
         */
        @Override
        public void actionPerformed(ActionEvent event) {
            if(event.getActionCommand().equals("onCountryClick")){
                ICountry from    = JCountryMoveMenu.this.getCountry();
                ICountry to      = JCountryMoveMenu.this.getSelectedNeighborsMenuItem().getCountry();

                int numberOfArmyies;
                int min = 1;
                int max = 1; //-1, da noch eine Einheit da bleiben muss, Dient hier aber nur zur Clientseitigen prüfen wird auch Serverseite auch nochnal geprüft.
                String message = "";
                try {
                    max = from.getNumberOfArmys()-1;
                    message = String.format("Bitte geben Sie an, wieviele Armeen Sie von " + from.getName() + " nach " + to.getName() + " verschieben möchten. %n Sie können maximal " +  max);
                }catch (RemoteException e){
                    new JExceptionDialog(JCountryMoveMenu.this,e);
                    return;
                }


                try {
                    numberOfArmyies = JModalDialog.showAskIntegerModal(JCountryMoveMenu.this,"Anzahl Armeen",message,min,max);
                }catch (UserCanceledException e){
                    //Benutzer hat abgebrochen, kein move durchführen
                    JModalDialog.showInfoDialog(JCountryMoveMenu.this,"Abbruch", "Sie haben die Aktion abgebrochen, es wurden keine Armeen bewegt");
                    return;
                }


                try {
                    turn.moveArmy(from,to,numberOfArmyies, clientPlayer);
                }catch ( ToManyNewArmysException | NotEnoughArmysToMoveException |  NotYourTurnException |TurnNotAllowedStepException | TurnNotInCorrectStepException | CountriesNotConnectedException | ArmyAlreadyMovedException | NotTheOwnerException  | RemoteCountryNotFoundException | RemoteException | NotEoughUnmovedArmiesException e ){
                   new JExceptionDialog(JCountryMoveMenu.this,e);
                   return;
                }

            }
        }
    }
    public JCountryMoveMenu (final ICountry country, final ITurn turn, IPlayer cPlayer) throws RemoteException{
        super("Move",country);
        this.turn = turn;
        this.clientPlayer = cPlayer;
        this.addActionListener(new NeighborActionListener());

    }
}
