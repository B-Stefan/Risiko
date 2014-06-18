package main.java.ui.GUI.country;

import main.java.logic.Fight;
import main.java.logic.exceptions.*;
import main.java.logic.utils.Dice;
import main.java.ui.GUI.utils.JExceptionDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Stack;

/**
 * Created by Stefan on 18.06.14.
 */
public class JFightSide extends Panel {

    public static enum sides {
        DEFENDER,
        AGGRESSOR
    }
    private final Fight fight;
    private final sides side;
    private final JTextField numberOfArmiesText;
    private final JTextArea thrownDiceText;
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
            }catch (NumberFormatException e){
                new JExceptionDialog(frame,"Bitte geben Sie eine gültige Zahl ein");
                return;
            }


            int max = 3; // Attacker
            if (JFightSide.this.side == sides.DEFENDER){
                max = 2;
            }
            if (numberOfArmies < 1 || numberOfArmies > max){
                new JExceptionDialog(frame,"Bitte geben Sie eine Anzahl zwischen 1 und "+max+" ein");
                return;
            }

            try {
                if(JFightSide.this.side == sides.DEFENDER){
                    JFightSide.this.fight.defending(numberOfArmies);

                }
                else if (JFightSide.this.side == sides.AGGRESSOR){
                    JFightSide.this.fight.attacking(numberOfArmies);
                }

            }catch (NotEnoughArmysToMoveException | ToManyNewArmysException | NotEnoughArmiesToDefendException |InvalidAmountOfArmiesException | CountriesNotConnectedException | AlreadyDicedException | TurnNotAllowedStepException | TurnNotInCorrectStepException | ArmyAlreadyMovedException  | NotEnoughArmiesToAttackException| InvalidFightException | NotTheOwnerException e ){
                new JExceptionDialog(frame,e);
                return;
            }

            JFightSide.this.update();
        }
    }

    public JFightSide(Fight fight, sides side ){
        this.fight = fight;
        this.side = side;
        this.numberOfArmiesText = new JTextField(SwingConstants.RIGHT);
        this.thrownDiceText = new JTextArea();
        this.setLayout(new GridLayout(5,1));

        if(this.side == sides.AGGRESSOR){
            this.add(new JLabel("Angreifer:"));
            this.add(new JLabel(fight.getAggressor().getName()));
        }
        else if (this.side == sides.DEFENDER){
            this.add(new JLabel("Verteidiger:"));
            this.add(new JLabel(fight.getDefender().getName()));
        }


        this.thrownDiceText.setRows(3);
        this.thrownDiceText.setEnabled(false);
        this.numberOfArmiesText.transferFocus();
        this.add(this.thrownDiceText);
        this.add(this.numberOfArmiesText);
        JButton throwDice = new JButton("Würfeln");
        throwDice.addActionListener(new ThrowDiceListener());
        this.add(throwDice);

    }
    public void update (){

        Window root = SwingUtilities.getWindowAncestor(this);
        if(root!= null){
            root.repaint();//Neuzeichnen der gesamten karte, falls der Owner sich geändert hat oder die Anzahl der Armeen
        }

        Stack<Dice> dices;
        if(this.side == sides.AGGRESSOR){
            dices = this.fight.getAgressorsDice();
        }
        else{
            dices = this.fight.getDefendersDice();
        }
        String str = "";
        while (!dices.empty()){
            Dice dice = dices.pop();
            str += String.format(dice.getDiceNumber() + "%n" );
        }
        this.thrownDiceText.setText(str);
    }

}
