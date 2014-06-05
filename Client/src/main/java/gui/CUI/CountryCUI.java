package main.java.gui.CUI;

import main.java.gui.CUI.utils.CUI;
import main.java.gui.CUI.utils.CommandListener;
import main.java.gui.CUI.utils.CommandListenerArgument;
import main.java.gui.CUI.utils.IO;
import main.java.gui.CUI.exceptions.InvalidCommandListernArgumentException;
import main.java.logic.data.Country;
import main.java.logic.Fight;
import main.java.logic.Turn;
import main.java.logic.exceptions.*;

import java.awt.event.ActionEvent;
import java.util.LinkedHashMap;

/**
 * Created by Stefan on 30.04.14.
 */
public class CountryCUI extends CUI {
    private Turn turn;
    private Country country;

    public class ShowListener extends CommandListener {

        public ShowListener() {
            super("show", "Zeigt Ihnend as Land an ");
        }

        @Override
        public void actionPerformed(final ActionEvent actionEvent) {
            IO.println("Das Land gehört " + country.getOwner() + " und ist mit: " + country.getNumberOfArmys() + " Armeen besetzt");
            IO.println("Nachfolgend die Nachbarn des Landes");
            for(Country c : country.getNeighbors()){
                IO.println(c.toString());
            }
        }
    }
    public class PlaceArmyListener extends CommandListener {

        public PlaceArmyListener() {
            super("place", "Setzt n Armeen auf das angegebene Land, default 1 ");
            this.addArgument(new CommandListenerArgument("numberOfArmys"));
        }

        @Override
        public void actionPerformed(final ActionEvent actionEvent) {
            final int numberOfArmys;
            try {
                numberOfArmys = this.getArgument("numberOfArmys").toInt() ;
            }catch (InvalidCommandListernArgumentException e ){
                IO.println(e.getMessage());
                return;
            }

            for (int i = 0; i < numberOfArmys; i++){
                try {
                    turn.placeNewArmy(country);
                }catch (NotEnoughNewArmysException e){
                    IO.println("Es konnten leider nur " + i + " Armeen auf dem Land (" + country + ") gesetzt werden");
                    return;
                }
                catch (TurnNotAllowedStepException e ){
                    IO.println(e.getMessage());
                    return;
                }
                catch (TurnNotInCorrectStepException e ){
                    IO.println(e.getMessage());
                    return;
                }
            }
            IO.println("Es wurden " + numberOfArmys + " Armeen auf (" + country + ") gesetzt");
        }
    }
    public class FightListener extends CommandListener {

        public FightListener() {
            super("fight", "Angriff auf ein Land");
            this.addArgument(new CommandListenerArgument("country", "Bitte geben Sie ein Gültiges Land ein"));
            this.addArgument(new CommandListenerArgument("numberOfArmys", "Bitte geben Sie eine gülte Anzhal an Armeen ein"));
        }

        @Override
        public void actionPerformed(final ActionEvent actionEvent){
            final String target;

            try {
                target = this.getArgument("country").toStr();

            }catch (InvalidCommandListernArgumentException e){
                IO.println(e.getMessage());
                return;
            }

            Fight fight = null;
            Country found = country.getNeighbor(target);
            if (found == null){
                IO.println("Leider konnte Ihr Land " + target + " nicht gefunden werden");
                return;
            } else {
                try {
                    fight = turn.fight(country,found);
                }catch (TurnNotAllowedStepException e){
                    IO.println(e.getMessage());
                    return;
                }catch (TurnNotInCorrectStepException e){
                    IO.println(e.getMessage());
                    return;
                }catch (ToManyNewArmysException e) {
                    IO.println(e.getMessage());
                    return;
                }
            }
            CUI fightCUI = new FightCUI(fight, CountryCUI.this);
            goIntoChildContext(fightCUI);
        }
    }
    public CountryCUI(Turn turn, Country context, CUI parent) {
        super(context, parent);
        this.turn = turn;
        this.country = context;
        this.addCommandListener(new ShowListener());
        this.addCommandListener(new PlaceArmyListener());
        this.addCommandListener(new FightListener());
    }



    @Override
    protected void goIntoChildContext(LinkedHashMap<String, CommandListenerArgument> args) {

        IO.println("In dieser Ebene ist cd nicht verfügbar");
        fireCommandEvent(new HelpListener());
    }
}
