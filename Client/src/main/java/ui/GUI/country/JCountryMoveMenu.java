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
 * Created by Stefan on 09.06.14.
 */
public class JCountryMoveMenu extends JCountryNeighborsMenu {

    private final ITurn turn;
	private final IPlayer clientPlayer;
    public class NeighborActionListener implements ActionListener{

        /**
         * Invoked when an action occurs.
         *
         * @param event
         */
        @Override
        public void actionPerformed(ActionEvent event) {
            if(event.getActionCommand() == "onCountryClick"){
                ICountry from    = JCountryMoveMenu.this.getCountry();
                ICountry to      = JCountryMoveMenu.this.getSelectedNeighborsMenuItem().getCountry();

                int numberOfArmyies;
                int min = 1;
                int max = 1; //-1, da noch eine Einheit da bleiben muss, Dient hier aber nur zur Clientseitigen prüfen wird auch Serverseite auch nochnal geprüft.
                String message = "";
                try {
                    max = from.getNumberOfArmys()-1;
                    message = String.format("Bitte geben Sie an, wieviele Armeen Sie von " + from.getName() + " nach " + to.getName() + " verschieben möchten. %n Sie können maximal " +  max);
                }catch (RemoteException e){
                    new JExceptionDialog(JCountryMoveMenu.this,e);
                    return;
                }


                try {
                    numberOfArmyies = JModalDialog.showAskIntegerModal(JCountryMoveMenu.this,"Anzahl Armeen",message,min,max);
                }catch (UserCanceledException e){
                    //Benutzer hat abgebrochen, kein move durchführen
                    JModalDialog.showInfoDialog(JCountryMoveMenu.this,"Abbruch", "Sie haben die Aktion abgebrochen, es wurden keine Armeen bewegt");
                    return;
                }


                try {
                    turn.moveArmy(from,to,numberOfArmyies, clientPlayer);
                }catch ( ToManyNewArmysException | NotEnoughArmysToMoveException |  NotYourTurnException |TurnNotAllowedStepException | TurnNotInCorrectStepException | CountriesNotConnectedException | ArmyAlreadyMovedException | NotTheOwnerException  | RemoteCountryNotFoundException | RemoteException e ){
                   new JExceptionDialog(JCountryMoveMenu.this,e);
                   return;
                }

            }
        }
    }
    public JCountryMoveMenu (final ICountry country, final ITurn turn, IPlayer cPlayer) throws RemoteException{
        super("Move",country);
        this.turn = turn;
        this.clientPlayer = cPlayer;
        this.addActionListener(new NeighborActionListener());

    }
}
