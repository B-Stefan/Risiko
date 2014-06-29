package main.java.ui.GUI.country;

import interfaces.ITurn;
import interfaces.data.ICountry;
import exceptions.*;
import main.java.ui.GUI.utils.JExceptionDialog;
import main.java.ui.GUI.utils.JModalDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

/**
 * Created by Stefan on 11.06.14.
 */
public class JCountryPlaceMenuItem extends JMenuItem {



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
            String message = String.format("Bitte geben Sie an, wieviele Armeen Sie auf " + to.getName() + " plazieren möchten");

            try {
                max = turn.getNewArmysSize();
            }catch (RemoteException e){
                new JExceptionDialog(JCountryPlaceMenuItem.this,e);
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
                turn.placeNewArmy(to,numberOfArmyies);
            }catch (TurnNotAllowedStepException | ToManyNewArmysException | TurnNotInCorrectStepException | NotEnoughNewArmysException | RemoteException | NotTheOwnerException e ){
                new JExceptionDialog(JCountryPlaceMenuItem.this,e);
                return;
            }
            JPopupMenu menu = (JPopupMenu) JCountryPlaceMenuItem.this.getParent();
            Component com = menu.getInvoker();
            if(com != null){
                com.repaint();
            }
        }
    }
    public JCountryPlaceMenuItem(ICountry country, ITurn turn){
        super("Place");
        this.country = country;
        this.turn = turn;
        this.addActionListener(new MoveClickListener());
    }
    public ICountry getCountry(){
        return country;
    }


}
