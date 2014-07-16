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

import commons.configuration.FightConfiguration;
import commons.interfaces.IFight;
import commons.interfaces.data.ICountry;
import commons.interfaces.data.IPlayer;
import commons.interfaces.data.utils.IDice;
import commons.exceptions.*;
import Client.ui.GUI.utils.JExceptionDialog;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.Stack;

/**
 * Repräsentiert eine Seite der FightGUI
 *
 * Diese Klasse wird für den Aggressor und Defender verwendet und passt sich dem entsprechenden an
 *
 * @see Client.ui.GUI.country.JFightGUI
 *
 */
public class JFightSide extends Panel {

    /**
     * Seiten des Fights
     */
    public static enum sides {
        DEFENDER,
        AGGRESSOR
    }

    /**
     * Server-Objet für das ausführen der Aktionen
     */
    private final IFight fight;
    /**
     * Seite dieser instanz
     */
    private final sides side;

    /**
     * Text-Feld für die Anzahl der Armeen
     */
    private final JTextField numberOfArmiesText;
    /**
     * Bereich für Ergebnis der Seite
     */
    private final JTextArea thrownDiceText;
    /**
     * Spieler der die Aktionen durchführen möchte
     */
	private final IPlayer clientPlayer;

    /**
     * Wenn auf Würfeln geklickt wurde wird dieser Listener ausgeführt
     */
    private class ThrowDiceListener implements ActionListener {

        /**
         * Invoked when an action occurs.
         *
         * @param event
         */
        @Override
        public void actionPerformed(ActionEvent event) {

            int numberOfArmies;

            Component frame =  JFightSide.this.getParent(); // Der Frame in dem die Exceptions dargestellt werden

            try {
                numberOfArmies = Integer.parseInt(JFightSide.this.numberOfArmiesText.getText());
            } catch (NumberFormatException e) {
                new JExceptionDialog(frame, "Bitte geben Sie eine gültige Zahl ein");
                return;
            }


            int max = FightConfiguration.DEFENDER_MAX_NUMBER_OF_ARMIES_TO_DEFEND; // Attacker
            if (JFightSide.this.side == sides.AGGRESSOR) {
                max = FightConfiguration.AGGRESSOR_MAX_NUMBER_OF_ARMIES_TO_ATTACK;
            }
            if (numberOfArmies < FightConfiguration.NUMBER_OF_ARMIES_EXCLUDE_FROM_FIGHT || numberOfArmies > max) {
                new JExceptionDialog(frame, "Bitte geben Sie eine Anzahl zwischen 1 und " + max + " ein");
                return;
            }

            try {
                if (JFightSide.this.side == sides.DEFENDER) {
                    JFightSide.this.fight.defending(numberOfArmies, clientPlayer);

                } else if (JFightSide.this.side == sides.AGGRESSOR) {
                    JFightSide.this.fight.attacking(numberOfArmies, clientPlayer);
                }

            } catch (AggessorNotThrowDiceException | YouCannotAttackException | YouCannotDefendException | NotEnoughArmysToMoveException | ToManyNewArmysException | NotEnoughArmiesToDefendException | InvalidAmountOfArmiesException | CountriesNotConnectedException | AlreadyDicedException | TurnNotAllowedStepException | TurnNotInCorrectStepException | ArmyAlreadyMovedException | NotEnoughArmiesToAttackException | InvalidFightException | NotTheOwnerException | RemoteException | RemoteCountryNotFoundException | CountryNotInListException e) {
                new JExceptionDialog(frame, e);
                return;
            }

            /**
             * Try to update GUI
             */
            try {
                JFightSide.this.update();
            } catch (RemoteException e) {
                new JExceptionDialog(JFightSide.this, e);
            }

        }
    }

    /**
     * Klasse zeigt eine Seite des Kampfes an
     * @param fight Server-Objekt
     * @param side Seite für die angezeigt werden soll
     * @param cPlayer Spieler der die Aktion durchühren möchte
     * @throws RemoteException
     */
    public JFightSide(IFight fight, sides side, IPlayer cPlayer) throws RemoteException {
        super();
        this.fight = fight;
        this.side = side;
        this.clientPlayer = cPlayer;
        this.numberOfArmiesText = new JTextField(SwingConstants.RIGHT);
        this.thrownDiceText = new JTextArea();
        this.setLayout(new GridLayout(5, 1));

        if (this.side == sides.AGGRESSOR) {
            this.add(new JLabel("Angreifer:"));
            this.add(new JLabel(fight.getAggressor().getName()));
        } else if (this.side == sides.DEFENDER) {
            this.add(new JLabel("Verteidiger:"));
            this.add(new JLabel(fight.getDefender().getName()));
        }


        this.thrownDiceText.setRows(3);
        this.thrownDiceText.setText(String.format("%n %n %n")); // Default 3 leerzeichen, sieht besser aus
        this.thrownDiceText.setEnabled(false);
        this.numberOfArmiesText.transferFocus();
        this.add(this.thrownDiceText);
        this.add(this.numberOfArmiesText);
        this.numberOfArmiesText.setText("" + calculateDefaultNumberOfArmies());
        JButton throwDice = new JButton("Würfeln");
        throwDice.addActionListener(new ThrowDiceListener());
        this.add(throwDice);

    }

    /**
     * Berechnet die Standard-Anzahl der Amreen, die der Spieler setzten würde
     * @return Standard-Anzahl an Armeen
     */
    private int calculateDefaultNumberOfArmies() {
        int numberOfArmiesOnCountry;
        ICountry country;

        try {
            if (this.side == sides.DEFENDER) {
                country = fight.getTo();
            } else if (this.side == sides.AGGRESSOR) {
                country = fight.getFrom();
            } else {
                new JExceptionDialog(JFightSide.this, "Nicht unterstütze side" + this.side);
                return 0;
            }
            numberOfArmiesOnCountry = country.getArmySize() - FightConfiguration.NUMBER_OF_ARMIES_EXCLUDE_FROM_FIGHT;
        } catch (RemoteException e) {
            new JExceptionDialog(this, e);
            return 0;
        }

        if (this.side == sides.DEFENDER) {
            if(numberOfArmiesOnCountry >= FightConfiguration.DEFENDER_MAX_NUMBER_OF_ARMIES_TO_DEFEND){
                return FightConfiguration.DEFENDER_MAX_NUMBER_OF_ARMIES_TO_DEFEND;
            }
            return 1;
        }else {
            //If Aggressor
            if(numberOfArmiesOnCountry >= FightConfiguration.DEFENDER_MAX_NUMBER_OF_ARMIES_TO_DEFEND){
                return FightConfiguration.AGGRESSOR_MAX_NUMBER_OF_ARMIES_TO_ATTACK;
            }
            return 1;
        }
    }

    /**
     * Aktualsiiert die Ansicht
     * @throws RemoteException
     */
    public void update() throws RemoteException {


        final Stack<? extends IDice> dices;
        if (this.side == sides.AGGRESSOR) {
            dices = this.fight.getAgressorsDice();
        } else {
            dices = this.fight.getDefendersDice();
        }

        String str = "";
        while (!dices.empty()) {
            final IDice dice = dices.pop();
            str += String.format(dice.getDiceNumber() + "%n");
        }
        this.thrownDiceText.setText(str);
    }

}
