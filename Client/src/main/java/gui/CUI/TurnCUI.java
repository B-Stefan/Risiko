package main.java.gui.CUI;

/**
 * Created by Stefan on 30.04.14.
 */
import main.java.gui.CUI.core.CUI;
import main.java.gui.CUI.core.CommandListener;
import main.java.gui.CUI.core.IO;
import main.java.logic.Country;
import main.java.logic.Turn;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.awt.event.ActionEvent;

public class TurnCUI extends CUI {


    private  final  Turn turn;

    public class ShowCountriesCommandListener extends CommandListener {

        public ShowCountriesCommandListener() {
            super("show", "Gibt die aktuelle Karte des Players aus");
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            for(Country c : turn.getPlayer().getCountries()){
                IO.println(c.toString());
            }

        }
    }

    public TurnCUI(Turn turn, CUI parent){
        super(turn,parent);
        this.turn = turn;
        this.addCommandListener(new ShowCountriesCommandListener());
    }
    protected void goIntoChildContext(){
        IO.println("Bitte gebe einen land als Paramenter ein");
        this.fireCommandEvent(new ShowCountriesCommandListener());
    }
    protected void goIntoChildContext(String[] args){
        String countryName = args[0];

        Country found = this.turn.getPlayer().getCountry(countryName);
        if(found == null){
            IO.println("Ihr Land " + countryName + " konnte nicht gefunden werden.");
        }
        else {
            CUI child = new CountryCUI(turn,found, this);
            super.goIntoChildContext(child);
        }
    }

}
