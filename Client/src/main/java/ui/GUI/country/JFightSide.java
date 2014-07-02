package ui.GUI.country;

import configuration.FightConfiguration;
import interfaces.IFight;
import interfaces.data.ICountry;
import interfaces.data.IPlayer;
import interfaces.data.utils.IDice;
import exceptions.*;
import ui.GUI.JGameGUI;
import ui.GUI.utils.JExceptionDialog;
import ui.GUI.utils.JModalDialog;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.Stack;

/**
 * Created by Stefan on 18.06.14.
 */
public class JFightSide extends Panel {

    public static enum sides {
        DEFENDER,
        AGGRESSOR
    }

    private final IFight fight;
    private final sides side;
    private final JTextField numberOfArmiesText;
    private final JTextArea thrownDiceText;
	private final IPlayer clientPlayer;

    private class ThrowDiceListener implements ActionListener {

        /**
         * Invoked when an action occurs.
         *
         * @param event
         */
        @Override
        public void actionPerformed(ActionEvent event) {

            int numberOfArmies;

            Component frame = (Component) JFightSide.this.getParent(); // Der Frame in dem die Exceptions dargestellt werden

            try {
                numberOfArmies = Integer.parseInt(JFightSide.this.numberOfArmiesText.getText());
            } catch (NumberFormatException e) {
                new JExceptionDialog(frame, "Bitte geben Sie eine gültige Zahl ein");
                return;
            }


            int max = FightConfiguration.DEFENDER_MAX_NUMBER_OF_ARMIES_TO_DEFEND; // Attacker
            if (JFightSide.this.side == sides.DEFENDER) {
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

            } catch (AggessorNotThrowDiceException | YouCannotAttackException | YouCannotDefendException | NotEnoughArmysToMoveException | ToManyNewArmysException | NotEnoughArmiesToDefendException | InvalidAmountOfArmiesException | CountriesNotConnectedException | AlreadyDicedException | TurnNotAllowedStepException | TurnNotInCorrectStepException | ArmyAlreadyMovedException | NotEnoughArmiesToAttackException | InvalidFightException | NotTheOwnerException | RemoteException | RemoteCountryNotFoundException e) {
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
                return FightConfiguration.DEFENDER_MAX_NUMBER_OF_ARMIES_TO_DEFEND;
            }
            return 1;
        }
    }

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
