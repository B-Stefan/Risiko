package main.java.gui.CUI;

/**
 * Created by Stefan on 30.04.14.
 */
import main.java.gui.CUI.core.CUI;
import main.java.gui.CUI.core.CommandListener;
import main.java.gui.CUI.core.IO;
import main.java.logic.Country;
import main.java.logic.Turn;
import main.java.logic.exceptions.GameAllreadyStartedException;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.awt.event.ActionEvent;

public class TurnCUI extends CUI {


    private  final  Turn turn;

    public class showCountries extends CommandListener {

        public showCountries() {
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
    }
    protected void goIntoChildContext(){

        throw new NotImplementedException();
    }

    protected void CUIShow (String[] args){
        IO.println("Nachfolgend die entsprechenden Ländern für" + this.turn.getPlayer().toString());
        for(Country c : this.turn.getPlayer().getCountries()){
            IO.println(c.toString());
        }
    }
}
