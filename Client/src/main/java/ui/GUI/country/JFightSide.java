package ui.GUI.country;

import interfaces.IFight;
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

            }catch (AggessorNotThrowDiceException | NotEnoughArmysToMoveException | ToManyNewArmysException | NotEnoughArmiesToDefendException |InvalidAmountOfArmiesException | CountriesNotConnectedException | AlreadyDicedException | TurnNotAllowedStepException | TurnNotInCorrectStepException | ArmyAlreadyMovedException  | NotEnoughArmiesToAttackException| InvalidFightException | NotTheOwnerException | RemoteException e ){
                new JExceptionDialog(frame,e);
                return;
            }

            /**
             * Try to update GUI
             */
            try {
                JFightSide.this.update();
            }catch (RemoteException e){
             new JExceptionDialog(JFightSide.this,e);
            }


            if(JFightSide.this.side == sides.DEFENDER){
                int[] result;
                try {
                    result = JFightSide.this.fight.getResult();
                }catch (RemoteException e){
                    new JExceptionDialog(JFightSide.this,e);
                    return;
                }
                int defenderLostArmies = result[1];
                int aggressorLostArmies = result[0];
                int aggresorWon = result[2];
                if (aggresorWon == 1){
                    JModalDialog.showInfoDialog(JFightSide.this, "Angriff erfolgreich", "Der Angreifer hat das Land übernommen");
                    //Close Window
                    Window JDialogRoot = SwingUtilities.getWindowAncestor(JFightSide.this);
                    JDialogRoot.dispose();
                }else{
                    String str = String.format("Der Angreifer hat " + aggressorLostArmies + "Armeen verloren %n");
                    str  += String.format("Der Verteidiger hat " + defenderLostArmies + "Armeen verloren %n");
                    str  += "Der Kampf geht weiter ";
                    JModalDialog.showInfoDialog(JFightSide.this, "Erfolgreich verteidigt", str);
                }

            }
        }
    }

    public JFightSide(IFight fight, sides side ) throws RemoteException{
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
        this.thrownDiceText.setText(String.format("%n %n %n")); // Default 3 leerzeichen, sieht besser aus
        this.thrownDiceText.setEnabled(false);
        this.numberOfArmiesText.transferFocus();
        this.add(this.thrownDiceText);
        this.add(this.numberOfArmiesText);
        JButton throwDice = new JButton("Würfeln");
        throwDice.addActionListener(new ThrowDiceListener());
        this.add(throwDice);

    }
    public void update () throws RemoteException{

        final Window root = SwingUtilities.getWindowAncestor(this);
        if(root!= null){
            final Window mainRoot = SwingUtilities.getWindowAncestor(root);
            try {
                final JGameGUI gameGUI = (JGameGUI) SwingUtilities.getWindowAncestor(root);
                gameGUI.update();
            }catch (ClassCastException e){
                new JExceptionDialog(this,e);
            }
            mainRoot.repaint();

        }

        final Stack<IDice> dices = new Stack<IDice>();
        final Stack<IDice> ds;
        if(this.side == sides.AGGRESSOR){
            ds = this.fight.getAgressorsDice();

        }else{
            ds = this.fight.getDefendersDice();

        }
        //Copy entries
        for(IDice d: ds){
            dices.push(d);
        }

        String str = "";
        while (!dices.empty()){
            final IDice dice = dices.pop();
            str += String.format(dice.getDiceNumber() + "%n" );
        }
        this.thrownDiceText.setText(str);
    }

}
