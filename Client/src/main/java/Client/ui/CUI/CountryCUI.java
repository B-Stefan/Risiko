/*
 * RISIKO-JAVA - Game, Copyright 2014  Jennifer Theloy, Stefan Bieliauskas  -  All Rights Reserved.
 * Hochschule Bremen - University of Applied Sciences
 *
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 * Contact:
 *     Jennifer Theloy: jTheloy@stud.hs-bremen.de
 *     Stefan Bieliauskas: sBieliauskas@stud.hs-bremen.de
 *
 * Web:
 *     https://github.com/B-Stefan/Risiko
 *
 */

package Client.ui.CUI;

import commons.exceptions.*;
import commons.interfaces.IFight;
import commons.interfaces.ITurn;
import commons.interfaces.data.ICountry;
import commons.interfaces.data.IPlayer;
import Client.ui.CUI.utils.CUI;
import Client.ui.CUI.utils.CommandListener;
import Client.ui.CUI.utils.CommandListenerArgument;
import Client.ui.CUI.utils.IO;
import Client.ui.CUI.exceptions.InvalidCommandListernArgumentException;


import java.awt.event.ActionEvent;
import java.rmi.RemoteException;
import java.util.LinkedHashMap;


public class CountryCUI extends CUI {
    private final ITurn turn;
    private final ICountry country;
    /**
     * Der Spieler der das Spiel steuert
     */
    private final IPlayer player;

    public class ShowListener extends CommandListener {

        public ShowListener() {
            super("show", "Zeigt Ihnend as Land an ");
        }

        @Override
        public void actionPerformed(final ActionEvent actionEvent) {
            try {
                IO.println("Das Land gehört " + country.getOwner() + " und ist mit: " + country.getNumberOfArmys() + " Armeen besetzt");
                IO.println("Nachfolgend die Nachbarn des Landes");
                for(ICountry c : country.getNeighbors()){
                    IO.println(c.toStringRemote());
                }
            }catch (RemoteException e){
                e.printStackTrace();
                IO.println(e.getMessage());
                return;
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
                    turn.placeNewArmy(country,CountryCUI.this.player);
                }catch (NotEnoughNewArmysException e){
                    String name;
                    try {
                        name = country.toStringRemote();
                    }catch (RemoteException ex){
                        ex.printStackTrace();
                        IO.println(ex.getMessage());
                        return;
                    }
                    IO.println("Es konnten leider nur " + i + " Armeen auf dem Land (" + name + ") gesetzt werden");
                    return;
                }
                catch (TurnNotAllowedStepException | ToManyNewArmysException | TurnNotInCorrectStepException | NotTheOwnerException | RemoteException | RemoteCountryNotFoundException | NotYourTurnException e ){
                    IO.println(e.getMessage());
                    return;
                }
            }

            try{
                IO.println("Es wurden " + numberOfArmys + " Armeen auf (" + country.toStringRemote() + ") gesetzt");
            }catch (RemoteException e){
                e.printStackTrace();
                IO.println(e.getMessage());
            }
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
            IFight fight;
            ICountry found;
            try{
                found = country.getNeighbor(target);
            }catch (RemoteException e){
                e.printStackTrace();
                IO.println(e.getMessage());
                return;
            }

            if (found == null){
                IO.println("Leider konnte Ihr Land " + target + " nicht gefunden werden");
                return;
            } else {
                try {
                    fight = turn.fight(country,found,CountryCUI.this.player);
                }catch (TurnNotAllowedStepException | ToManyNewArmysException | NotTheOwnerException | RemoteException | RemoteCountryNotFoundException | NotYourTurnException | TurnNotInCorrectStepException e){
                    IO.println(e.getMessage());
                    return;
                }
            }
            CUI fightCUI = new FightCUI(fight, CountryCUI.this,CountryCUI.this.player);
            goIntoChildContext(fightCUI);
        }
    }
    public CountryCUI(final ITurn turn, final ICountry context,final CUI parent, final IPlayer player) {
        super(context, parent);
        this.player = player;
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
