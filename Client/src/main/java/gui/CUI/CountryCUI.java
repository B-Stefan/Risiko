package main.java.gui.CUI;

import main.java.gui.CUI.core.CUI;
import main.java.gui.CUI.core.CommandListener;
import main.java.gui.CUI.core.IO;
import main.java.logic.Country;
import main.java.logic.Turn;
import main.java.logic.exceptions.NotEnoughNewArmysException;
import main.java.logic.exceptions.TurnNotAllowedStepException;
import main.java.logic.exceptions.TurnNotInCorrectStepException;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.awt.event.ActionEvent;

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
            final String[]  args = this.getArguments();
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
        }

        @Override
        public void actionPerformed(final ActionEvent actionEvent) {
            final String[]  args = this.getArguments();
            final String target = args[0];
            int numberOfArmys = 1;


            if(target == null){
                IO.println("Bitte geben Sie ein Land an ");
                return;
            }
            else if (target == "") {
                IO.println("Bitte geben Sie ein gültiges Land ein");
            }

            if(args[1] == null){
                IO.println("Bitte geben Sie die Anzahl an mit der Sie angfreifen möchten");
                return;
            }
            else {
                try {
                    numberOfArmys = Integer.parseInt(args[1]);
                } catch (NumberFormatException e ){
                    IO.println("Bitte geben Sie eine gültige Anzahl an Armeen ein");
                    return;
                }
            }

            Country found = country.getNeighbor(target);
            if (found == null){
                IO.println("Leider konnte Ihr Land " + target + " nicht gefunden werden");
            } else {
                try {
                    turn.fight(country,found,numberOfArmys);
                }catch (TurnNotAllowedStepException e){
                    IO.println(e.getMessage());
                }catch (TurnNotInCorrectStepException e){
                    IO.println(e.getMessage());
                }
            }
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
    protected void goIntoChildContext() {

    }

    @Override
    protected void goIntoChildContext(String[] args) {

    }
}
