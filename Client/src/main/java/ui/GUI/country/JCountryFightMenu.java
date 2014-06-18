package main.java.ui.GUI.country;
import main.java.logic.Fight;
import main.java.logic.Turn;
import main.java.logic.data.Country;
import main.java.logic.exceptions.*;
import main.java.ui.GUI.utils.JExceptionDialog;
import main.java.ui.GUI.utils.JModalDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Stefan on 09.06.14.
 */
public class JCountryFightMenu extends JCountryNeighborsMenu {

    private final Turn turn;
    private final Country country;
    public class NeighborActionListener implements ActionListener{

        /**
         * Invoked when an action occurs.
         *
         * @param event
         */
        @Override
        public void actionPerformed(ActionEvent event) {
            if(event.getActionCommand() == "onCountryClick"){
                Country from    = JCountryFightMenu.this.country;
                Country to      =  JCountryFightMenu.this.getSelectedNeighborsMenuItem().getCountry();
                Fight fight;
                try {
                    fight = JCountryFightMenu.this.turn.fight(from, to);
                }catch (TurnNotInCorrectStepException | TurnNotAllowedStepException | ToManyNewArmysException | NotTheOwnerException e ){
                    new JExceptionDialog(JCountryFightMenu.this,e);
                    return;
                }
                JPopupMenu menu = (JPopupMenu) JCountryFightMenu.this.getParent();
                JModalDialog modal = new JFightGUI(menu.getInvoker(),fight);

            }
        }
    }
    public JCountryFightMenu(final Country country, final Turn turn){
        super("Fight",country);
        this.country = country;
        this.turn = turn;
        this.addActionListener(new NeighborActionListener());

    }
}
