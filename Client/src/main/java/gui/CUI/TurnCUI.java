package main.java.gui.CUI;

/**
 * Created by Stefan on 30.04.14.
 */
import main.java.gui.CUI.utils.CUI;
import main.java.gui.CUI.utils.CommandListener;
import main.java.gui.CUI.utils.CommandListenerArgument;
import main.java.gui.CUI.utils.IO;
import main.java.gui.CUI.exceptions.InvalidCommandListernArgumentException;
import main.java.logic.Country;
import main.java.logic.Turn;
import main.java.logic.exceptions.ToManyNewArmysException;
import main.java.logic.exceptions.TurnCompleteException;

import java.awt.event.ActionEvent;
import java.util.LinkedHashMap;

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
    public class NextStepCommandListener extends CommandListener {

        public NextStepCommandListener() {
            super("next", "Hiermit kannst du in den nächsten Step wechseln");
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                turn.setNextStep();
            }catch (TurnCompleteException e){
                IO.println("Dein Zug scheint beendet zu sein");
                IO.println("Es wird autmatisch in die Runde gewechselt und mit dem Befehl next kannst du an den nächsten Spieler weitergeben.");
                goIntoParentContext();
            }catch (ToManyNewArmysException e){
                IO.println("Sie müssen noch " + turn.getNewArmysSize() + " Einheiten verteilen bevor sie in den nächsten step wechseln können ");
            }
        }
    }
    public class StateCommandListener extends CommandListener {

        public StateCommandListener() {
            super("state", "Gibt den aktuellen Status deines Zuges aus");
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            Turn.steps currentStep = turn.getCurrentStep();
            IO.println("Es ist " + turn.getPlayer() + "an der Reihe");
            IO.println("Dein Zug befindet sich im status " + currentStep);
            if(currentStep == Turn.steps.DISTRIBUTE){
                IO.println("Du musst noch Einheiten auf deinen Ländern verteilen. Du darfst noch: "+ turn.getNewArmysSize() + " Einheiten verteilen");
            }
            else if(currentStep == Turn.steps.FIGHT){
                IO.println("Du befindest dich im Kampfmodus, greife Länder an!");

            }
            else if(currentStep == Turn.steps.MOVE){
                IO.println("Du darfst noch einheiten bewegen.");

            }

            IO.println("Dir stehen dazu folgende Komandos zur verfügung: ");
            fireCommandEvent(new HelpListener());

        }
    }

    public TurnCUI(Turn turn, CUI parent){
        super(turn,parent);
        this.turn = turn;
        this.addCommandListener(new ShowCountriesCommandListener());
        this.addCommandListener(new NextStepCommandListener());
        this.addCommandListener(new StateCommandListener());
    }
    protected void goIntoChildContext(){
        IO.println("Bitte gebe einen land als Paramenter ein");
        this.fireCommandEvent(new ShowCountriesCommandListener());
    }
    protected void goIntoChildContext(LinkedHashMap<String, CommandListenerArgument> args){
        final String countryName;
        try {
            countryName = args.get("parent").toStr();
        }catch (InvalidCommandListernArgumentException e){
            IO.println(e.getMessage());
            return;
        }

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
