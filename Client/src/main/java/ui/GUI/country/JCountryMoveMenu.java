package main.java.ui.GUI.country;

import main.java.logic.Turn;
import main.java.logic.data.Country;
import main.java.logic.exceptions.*;
import main.java.ui.CUI.utils.IO;

import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;

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

                try {
                    turn.moveArmy(from,to,1);
                }catch (NotEnoughArmysToMoveException |  TurnNotAllowedStepException | TurnNotInCorrectStepException | CountriesNotConnectedException | ArmyAlreadyMovedException e ){
                    IO.println(e.getMessage());
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
