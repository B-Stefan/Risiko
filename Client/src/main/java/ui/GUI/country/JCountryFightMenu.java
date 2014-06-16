package main.java.ui.GUI.country;
import main.java.logic.Turn;
import main.java.logic.data.Country;
import main.java.logic.exceptions.*;
import main.java.ui.GUI.utils.JExceptionDialog;
import main.java.ui.GUI.utils.JModalDialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Stefan on 09.06.14.
 */
public class JCountryFightMenu extends JCountryNeighborsMenu {

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
                new JExceptionDialog(JCountryFightMenu.this,"Nicht implementiert bis jetzt");
                //TODO Erstellen
            }
        }
    }
    public JCountryFightMenu(final Country country, final Turn turn){
        super("Fight",country);
        this.turn = turn;
        this.addActionListener(new NeighborActionListener());

    }
}
