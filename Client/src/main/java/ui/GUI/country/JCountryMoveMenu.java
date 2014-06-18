package main.java.ui.GUI.country;
import main.java.logic.Turn;
import main.java.logic.data.Country;
import main.java.logic.exceptions.*;
import main.java.ui.CUI.utils.IO;
import main.java.ui.GUI.utils.JExceptionDialog;
import main.java.ui.GUI.utils.JModalDialog;

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

                if(numberOfArmyies == 1){
                    JModalDialog.showInfoDialog(JCountryMoveMenu.this,"Nicht geügend Armeen", "Sie haben keine Armeen mehr zum plazieren");
                }


                try {
                    turn.moveArmy(from,to,numberOfArmyies);
                }catch (NotEnoughArmysToMoveException |  TurnNotAllowedStepException | TurnNotInCorrectStepException | CountriesNotConnectedException | ArmyAlreadyMovedException | NotTheOwnerException e ){
                   new JExceptionDialog(JCountryMoveMenu.this,e);
                    return;
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
