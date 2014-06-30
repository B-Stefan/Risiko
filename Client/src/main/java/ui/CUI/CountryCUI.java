package ui.CUI;

import exceptions.*;
import interfaces.IFight;
import interfaces.ITurn;
import interfaces.data.ICountry;
import ui.CUI.utils.CUI;
import ui.CUI.utils.CommandListener;
import ui.CUI.utils.CommandListenerArgument;
import ui.CUI.utils.IO;
import ui.CUI.exceptions.InvalidCommandListernArgumentException;


import java.awt.event.ActionEvent;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.LinkedHashMap;

/**
 * Created by Stefan on 30.04.14.
 */
public class CountryCUI extends CUI {
    private ITurn turn;
    private ICountry country;

    public class ShowListener extends CommandListener {

        public ShowListener() {
            super("show", "Zeigt Ihnend as Land an ");
        }

        @Override
        public void actionPerformed(final ActionEvent actionEvent) {
            IO.println("Das Land gehört " + country.getOwner() + " und ist mit: " + country.getNumberOfArmys() + " Armeen besetzt");
            IO.println("Nachfolgend die Nachbarn des Landes");
            for(ICountry c : country.getNeighbors()){
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
                catch (ToManyNewArmysException | TurnNotInCorrectStepException | NotTheOwnerException | RemoteException e ){
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

            IFight fight = null;
            ICountry found = country.getNeighbor(target);
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
                }catch (ToManyNewArmysException | NotTheOwnerException | RemoteException e) {
                    IO.println(e.getMessage());
                    return;
                }
            }
            CUI fightCUI = new FightCUI(fight, CountryCUI.this);
            goIntoChildContext(fightCUI);
        }
    }
    public CountryCUI(ITurn turn, ICountry context, CUI parent) {
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
