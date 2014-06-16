package main.java.ui.GUI.country;

import main.java.logic.Turn;
import main.java.logic.data.Country;
import main.java.logic.exceptions.*;
import main.java.ui.GUI.utils.JExceptionDialog;
import main.java.ui.GUI.utils.JModalDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by Stefan on 11.06.14.
 */
public class JCountryPlaceMenuItem extends JMenuItem {



    private final Country country;
    private final Turn turn;

    public class MoveClickListener implements ActionListener {
        /**
         * Invoked when an action occurs.
         *
         * @param e
         */
        @Override
        public void actionPerformed(ActionEvent event) {
            Country to    = JCountryPlaceMenuItem.this.getCountry();

            int numberOfArmyies;
            int min = 1;
            int max = turn.getNewArmysSize(); //Dient nur zur Clientseitigen prüfen, wird auf Serverseite nochmal geprüft und als Exception abgefangen
            String message = String.format("Bitte geben Sie an, wieviele Armeen Sie auf " + to.getName() + " plazieren möchten");


            try {
                numberOfArmyies = JModalDialog.showAskIntegerModal(JCountryPlaceMenuItem.this, "Anzahl Armeen", message, min, max);
            }catch (UserCanceledException e){
                //Benutzer hat abgebrochen, kein move durchführen
                new JExceptionDialog(JCountryPlaceMenuItem.this,e);
                return;
            }


            try {
                turn.placeNewArmy(to,numberOfArmyies);
            }catch (TurnNotAllowedStepException | TurnNotInCorrectStepException | NotEnoughNewArmysException | NotTheOwnerException e ){
                new JExceptionDialog(JCountryPlaceMenuItem.this,e);
                return;
            }
        }
    }
    public JCountryPlaceMenuItem(Country country, Turn turn){
        super("Place");
        this.country = country;
        this.turn = turn;
        this.addActionListener(new MoveClickListener());
    }
    public Country getCountry(){
        return country;
    }


}
