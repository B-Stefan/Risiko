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

package ui.CUI;

/**
 * Created by Stefan on 30.04.14.
 */
import exceptions.NotYourTurnException;
import exceptions.PlayerNotExsistInGameException;
import interfaces.ITurn;
import interfaces.data.ICountry;
import interfaces.data.IPlayer;
import interfaces.data.Orders.IOrder;
import ui.CUI.utils.CUI;
import ui.CUI.utils.CommandListener;
import ui.CUI.utils.CommandListenerArgument;
import ui.CUI.utils.IO;

import exceptions.ToManyNewArmysException;
import exceptions.TurnCompleteException;
import ui.CUI.exceptions.InvalidCommandListernArgumentException;


import java.awt.event.ActionEvent;
import java.rmi.RemoteException;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Diese Klasse dient ist zur Steuerung eines einzlenen Zuges über die Konsole gedacht
 */
public class TurnCUI extends CUI {


    /**
     * Zug der betreutt werden soll
     */
    private  final ITurn turn;

    /**
     * Der Spieler der die Konsole steuert
     */
    private final IPlayer player;

    /**
     *
     * Event-Listener für das darstellen der Spielerkarte
     */
    public class ShowCountriesCommandListener extends CommandListener {

        /**
         * Setzt Befehl und Hilfetext
         */
        public ShowCountriesCommandListener() {
            super("show", "Gibt die aktuelle Karte des Players aus");
        }


        /**
         * Beschreibt die Aktion, die beim auslösen ausgeführt wird
         * @param actionEvent Quelle des Events
         */
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            IPlayer currentPlayer;
            try{
                currentPlayer = turn.getPlayer();
                for(ICountry c : currentPlayer.getCountries()){
                    IO.println(c.toStringRemote());
                }
            }catch (RemoteException e){
                e.printStackTrace();
                IO.println(e.getMessage());
                return;
            }

        }
    }


    /**
     * Event-Listener für das wechseln in die nächste Stufe
     */
    public class NextStepCommandListener extends CommandListener {

        /**
         * Legt den Befehl und Hilfetext fest
         */
        public NextStepCommandListener() {
            super("next", "Hiermit kannst du deinen Zug in den nächsten Status bringen");
        }

        /**
         * Legt die Aktion fest, die beim aufruf des Komandos next passieren soll
         * @param actionEvent Quelle des Events
         */
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                turn.setNextStep(TurnCUI.this.player);
            }
            catch (RemoteException | NotYourTurnException e){
                e.printStackTrace();
                IO.println(e.getMessage());
                return;
            }
            catch (TurnCompleteException e){
                IO.println("Dein Zug scheint beendet zu sein");
                IO.println("Es wird autmatisch in die Runde gewechselt und mit dem Befehl next kannst du an den nächsten Spieler weitergeben.");
                goIntoParentContext();
            }catch (ToManyNewArmysException e){
                int newArmySize;
                try {
                    newArmySize = turn.getNewArmysSize();
                }catch (RemoteException ex){
                    ex.printStackTrace();
                    IO.println(ex.getMessage());
                    return;
                }
                IO.println("Sie müssen noch " + newArmySize + " Einheiten verteilen bevor sie in den nächsten step wechseln können ");
            }
        }
    }

    /**
     * Event-Listener für das darstellen des Zug-Status
     */
    public class StateCommandListener extends CommandListener {

        /**
         * Legt Befehl und Hilfetext fest
         */
        public StateCommandListener() {
            super("state", "Gibt den aktuellen Status deines Zuges aus");
        }

        /**
         * Gibt an was bei der Eingabe state passieren soll
         * @param actionEvent
         */
        @Override
        public void actionPerformed(ActionEvent actionEvent) {

            try {
                ITurn.steps currentStep = turn.getCurrentStep();

                IO.printHeadline("Status des Zugs");
                IO.println("Es ist " + turn.getPlayer() + " an der Reihe");
                IO.println("Dein Zug befindet sich im status " + currentStep);

                if (currentStep == ITurn.steps.DISTRIBUTE) {
                    if (turn.getNewArmysSize() > 0) {
                        IO.println("Du musst noch Einheiten auf deinen Ländern verteilen. Du darfst noch: " + turn.getNewArmysSize() + " Einheiten verteilen");
                    } else {
                        IO.println("Du hast keine Einheiten mehr zu verteilen wechsel mit next in die nächste Stufe ");
                    }
                } else if (currentStep == ITurn.steps.FIGHT) {
                    IO.println("Du befindest dich im Kampfmodus, greife Länder an!");

                } else if (currentStep == ITurn.steps.MOVE) {
                    IO.println("Du darfst noch einheiten bewegen.");

                }

                IO.printHeadline("Aufgabe des Spielers");
                IOrder playerOrder = turn.getPlayer().getOrder();
                IO.println(playerOrder.toString());

                IO.printHeadline("Befehle des Turns");
                IO.println("Dir stehen dazu folgende Komandos zur verfügung: ");
                fireCommandEvent(new HelpListener());
            }catch (RemoteException e){
                e.printStackTrace();
                IO.println(e.getMessage());
                return;
            }

        }
    }

    /**
     * Klasse zur grafischen Verwaltung eines Zuges
     * @param turn Der Zug der verwaltet werden soll
     * @param parent Die übergeordnete CUI Instanz
     */

    public TurnCUI(ITurn turn, CUI parent, IPlayer player){
        super(turn,parent);
        this.player = player;
        this.turn = turn;

        //Hinzufügen der Listener
        this.addCommandListener(new ShowCountriesCommandListener());
        this.addCommandListener(new NextStepCommandListener());
        this.addCommandListener(new StateCommandListener());
    }


    /**
     * Gibt an was passieren soll wenn das Komando cd eingeben werden soll
     * Dies ist ähnlich zu verstehen wie ein Ordnerwechsel in Linux
     * @param args Argument, die dem cd befehl übergeben wurden
     *             Für den CD-Befehl gibt es standardmäßig ein Argument.
     *             Diese kann mit args.get("parent") aus der HashMap geholet werden.
     */
    protected void goIntoChildContext(LinkedHashMap<String, CommandListenerArgument> args){
        final String countryName;
        final IPlayer currentPlayer;
        //Versuch einen namen des Landes als Argument zu holen
        try {
            countryName = args.get("parent").toStr();
        }catch (InvalidCommandListernArgumentException e){
            IO.println(e.getMessage());
            return;
        }

        ICountry found;

        try {
            currentPlayer = this.turn.getPlayer();
            found = currentPlayer.getCountry(countryName);
        }catch (RemoteException e){
            e.printStackTrace();
            IO.println(e.getMessage());
            return;
        }
        //Versuch das Land zu finden
        if(found == null){
            IO.println("Ihr Land " + countryName + " konnte nicht gefunden werden.");
        }
        else {
            //Erzeugen des Child
            CUI child = new CountryCUI(turn,found, this, this.player);
            super.goIntoChildContext(child);
        }
    }

}
