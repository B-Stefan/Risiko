package main.java.ui.GUI.country;
import logic.Turn;
import logic.data.Country;
import exceptions.*;
import main.java.ui.GUI.utils.JExceptionDialog;
import main.java.ui.GUI.utils.JModalDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Stefan on 09.06.14.
 */
public class JCountryMoveMenu extends JCountryNeighborsMenu {

    private final Turn turn;
    public class NeighborActionListener implements ActionListener{

        /**
         * Invoked when an action occurs.
         *
         * @param event
         */
        @Override
        public void actionPerformed(ActionEvent event) {
            if(event.getActionCommand() == "onCountryClick"){
                Country from    = JCountryMoveMenu.this.getCountry();
                Country to      = JCountryMoveMenu.this.getSelectedNeighborsMenuItem().getCountry();

                int numberOfArmyies;
                int min = 1;
                int max = from.getNumberOfArmys()-1; //-1, da noch eine Einheit da bleiben muss, Dient hier aber nur zur Clientseitigen prüfen wird auch Serverseite auch nochnal geprüft.
                String message = String.format("Bitte geben Sie an, wieviele Armeen Sie von " + from.getName() + " nach " + to.getName() + " verschieben möchten. %n Sie können maximal " +  max);


                try {
                    numberOfArmyies = JModalDialog.showAskIntegerModal(JCountryMoveMenu.this,"Anzahl Armeen",message,min,max);
                }catch (UserCanceledException e){
                    //Benutzer hat abgebrochen, kein move durchführen
                    JModalDialog.showInfoDialog(JCountryMoveMenu.this,"Abbruch", "Sie haben die Aktion abgebrochen, es wurden keine Armeen bewegt");
                    return;
                }


                try {
                    turn.moveArmy(from,to,numberOfArmyies);
                }catch ( ToManyNewArmysException | NotEnoughArmysToMoveException |  TurnNotAllowedStepException | TurnNotInCorrectStepException | CountriesNotConnectedException | ArmyAlreadyMovedException | NotTheOwnerException e ){
                   new JExceptionDialog(JCountryMoveMenu.this,e);
                   return;
                }

                //Repaint the whole map
                JPopupMenu menu = (JPopupMenu) JCountryMoveMenu.this.getParent();
                Component com = menu.getInvoker();
                if(com != null){
                    com.repaint();
                }
            }
        }
    }
    public JCountryMoveMenu (final Country country, final Turn turn){
        super("Move",country);
        this.turn = turn;
        this.addActionListener(new NeighborActionListener());

    }
}
