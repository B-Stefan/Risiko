package main.java.gui.CUI;

import main.java.gui.CUI.core.CUI;
import main.java.gui.CUI.core.CommandListener;
import main.java.gui.CUI.core.IO;
import main.java.logic.Country;
import main.java.logic.Turn;

import java.awt.event.ActionEvent;

/**
 * Created by Stefan on 30.04.14.
 */
public class CountryCUI extends CUI {
    private Turn turn;
    private Country country;


    public class SetArmyListener extends CommandListener {

        public SetArmyListener() {
            super("setArmy", "Setzt n Armeen auf das angegebene Land, default 1 ");
        }

        @Override
        public void actionPerformed(final ActionEvent actionEvent) {
            final String[]  args = this.getArguments();
            int numberOfArmys = 1 ;
            if(args[0]!=null){
                try {
                    numberOfArmys = Integer.parseInt(args[0]);
                } catch (NumberFormatException e ){
                    IO.println("Bitte geben Sie eine gültige Anzahl an Armeen ein");
                }
            }
            for (int i = 0; i < numberOfArmys; i++){
                turn.setNewArmy(country);
            }
            IO.println("Es wurden " + numberOfArmys + " Armeen auf " + country + " gesetzt");
        }
    }
    public CountryCUI(Turn turn, Country context, CUI parent) {
        super(context, parent);
        this.turn = turn;
        this.country = context;
        this.addCommandListener(new SetArmyListener());
    }

    @Override
    protected void goIntoChildContext() {

    }

    @Override
    protected void goIntoChildContext(String[] args) {

    }
}
