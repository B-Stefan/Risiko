package main.java.gui.CUI;

/**
 * Created by Stefan on 30.04.14.
 */
import main.java.gui.CUI.CUI;
import main.java.gui.CUI.IO;
import main.java.logic.Country;
import main.java.logic.Turn;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class TurnCUI extends CUI {


    private  final  Turn turn;
    public TurnCUI(Turn turn, CUI parent){
        super(turn,parent);
        this.turn = turn;
    }
    protected void goIntoChildContext() throws Exception{

        throw new NotImplementedException();

    }

    protected void CUIShow (String[] args){
        IO.println("Nachfolgend die entsprechenden Ländern für" + this.turn.getPlayer().toString());
        for(Country c : this.turn.getPlayer().getCountries()){
            IO.println(c.toString());
        }
    }
}
