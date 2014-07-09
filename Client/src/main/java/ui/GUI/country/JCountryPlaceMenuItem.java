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

import interfaces.ITurn;
import interfaces.data.ICountry;
import interfaces.data.IPlayer;
import exceptions.*;
import ui.GUI.utils.JExceptionDialog;
import ui.GUI.utils.JModalDialog;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

/**
 * Created by Stefan on 11.06.14.
 */
public class JCountryPlaceMenuItem extends JMenuItem {
	private final IPlayer clientPlayer;
    private final ICountry country;
    private final ITurn turn;

    public class MoveClickListener implements ActionListener {
        /**
         * Invoked when an action occurs.
         *
         * @param event
         */
        @Override
        public void actionPerformed(ActionEvent event) {
            ICountry to    = JCountryPlaceMenuItem.this.getCountry();

            int numberOfArmyies;
            int min = 1;
            int max = 1; //Dient nur zur Clientseitigen prüfen, wird auf Serverseite nochmal geprüft und als Exception abgefangen
            String message;

            try {
                max = turn.getNewArmysSize();
                message = String.format("Bitte geben Sie an, wieviele Armeen Sie auf " + to.getName() + " plazieren möchten");
            }catch (RemoteException e){
                new JExceptionDialog(JCountryPlaceMenuItem.this,e);
                return;
            }


            try {
                numberOfArmyies = JModalDialog.showAskIntegerModal(JCountryPlaceMenuItem.this, "Anzahl Armeen", message, min, max);
            }catch (UserCanceledException e){
                //Benutzer hat abgebrochen, kein move durchführen
                JModalDialog.showInfoDialog(JCountryPlaceMenuItem.this,"Abbruch", "Sie haben die Aktion abgebrochen, es wurden keine Armeen plaziert");
                return;
            }


            if(numberOfArmyies == 0){
                JModalDialog.showInfoDialog(JCountryPlaceMenuItem.this,"Nicht geügend Armeen", "Sie haben keine Armeen mehr zum plazieren");
            }

            try {
                turn.placeNewArmy(to,numberOfArmyies, clientPlayer);
            }catch (TurnNotAllowedStepException | ToManyNewArmysException | NotYourTurnException | TurnNotInCorrectStepException | NotEnoughNewArmysException | RemoteException | RemoteCountryNotFoundException | NotTheOwnerException e ){
                new JExceptionDialog(JCountryPlaceMenuItem.this,e);
                return;
            }

        }
    }
    public JCountryPlaceMenuItem(ICountry country, ITurn turn, IPlayer clientPlayer){
        super("Place");
        this.country = country;
        this.clientPlayer = clientPlayer;
        this.turn = turn;
        this.addActionListener(new MoveClickListener());
    }
    public ICountry getCountry(){
        return country;
    }


}
