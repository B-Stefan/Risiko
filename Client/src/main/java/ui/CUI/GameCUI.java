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

package ui.CUI;

import exceptions.*;
import interfaces.IClient;
import interfaces.IGame;
import interfaces.IRound;
import interfaces.data.IPlayer;
import server.logic.ClientEventProcessor;
import ui.CUI.utils.CUI;
import ui.CUI.utils.CommandListener;
import ui.CUI.utils.CommandListenerArgument;
import ui.CUI.utils.IO;
import ui.CUI.exceptions.InvalidCommandListernArgumentException;


import java.awt.event.ActionEvent;
import java.rmi.RemoteException;
import java.util.LinkedHashMap;


/**
 * Verwaltet die Benutzerschnittstelle für ein Spiel
 * @author Stefan Bieliauskas
 */
public class GameCUI extends CUI implements Runnable {

    /**
     * Bildet das Spiel ab für die die CUI erstellt wird
     */
    private final IGame game;

    /**
     * Bietet die Möglichkeit auf events vom Server zu horchen
     */
    private final IClient remoteEventProcessor;

    /**
     * Listener, um ein Spiel zu speichern.
     */

    private final IPlayer player;

    /**
     * Listener um ein Spiel zu speichern
     */
    public class SaveGameCommandListener extends CommandListener {

        public SaveGameCommandListener() {
            super("save","Speichert ein Spiel");
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                GameCUI.this.game.save();
            }catch (PersistenceEndpointIOException | RemoteException e){
                IO.println(e.getMessage());
                return;
            }
            IO.println("Game wurde gespeichert");
        }

    }
    /**
     * Klasse für Event-Listener zum hinzufügen eines Spielers
     */
    public class AddPlayerCommand extends CommandListener {

        /**
         * Konstruktor, der Name und Argumente des Befehls festlegen
         */
        public AddPlayerCommand() {
            super("addPlayer","Fügt einen Spieler dem Spiel hinzu");
            this.addArgument(new CommandListenerArgument("playerName"));
        }

        /**
         * Wird ausgeführt, wenn der Befehel eingeben wurde
         * @param actionEvent Das Event, das diese Action ausgelöst hat.
         */
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            final String name;
            try {
                name = this.getArgument("playerName").toStr();

            }catch (InvalidCommandListernArgumentException e){
                IO.println(e.getMessage());
                return;
            }

            if(name.trim().length() == 0){
                IO.println("Bitte geben Sie einen gültigen Namen ein");
                return;
            }

            try {
                GameCUI.this.game.addPlayer(name,GameCUI.this.remoteEventProcessor);
            }
            catch (GameAllreadyStartedException | PlayerNameAlreadyChooseException | RemoteException e ){
                IO.println(e.getMessage());
                return;
            }

        }

    }

    /**
     * Klasse für Event-Listener zum wechseln in die nächste Runde
     */
    public class NextRoundCommandListener extends CommandListener {

        /**
         * Gibt den Befehl an und legt die Argumente fest
         */
        public NextRoundCommandListener() {
            super("next", "Startet die nächste Runde");
            this.addArgument(new CommandListenerArgument("playerName"));
        }

        /**
         * Beschreibt was beim auslösen dieses Events passieren soll
         * @param actionEvent Eventquelle
         */
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
           try {
               game.setNextRound();
           }catch (RoundNotCompleteException | RemoteException | ToManyNewArmysException | GameNotStartedException e){
               IO.println(e.getMessage());
               return;
           }catch (GameIsCompletedException e){
               IO.printHeadline("Spielende");

               IO.println("Das Spiel wurde gewonnen, ein Spieler hat seinen Auftrag erfüllt");

               IO.printHeadline("AND");
               IO.printHeadline("The");
               IO.printHeadline("Winner");
               IO.printHeadline("IS");

               try {
                   IO.printHeadline("");
                   IO.printHeadline(game.getWinner().toStringRemote());
                   IO.printHeadline("");
               }catch (RemoteException ex){
                   IO.println(ex.getMessage());
               }
               return;
           }
           goIntoChildContext();
        }

    }


    /**
     * Klasse für Event-Listenerz zum Starten des Spiels
     */
    public class StartGameCommand extends CommandListener {

        /**
         * Legt den Befehl fest, der zum starten des Spiels verwendet wird
         */
        public StartGameCommand() {
            super("startGame", "Started das Spiel");

        }

        /**
         * Gibt alle Player auf der Konsole aus
         */
        private void printPlayers () throws RemoteException{
            for (IPlayer player : GameCUI.this.game.getPlayers()) {
                int index = (GameCUI.this.game.getPlayers().indexOf(player) + 1);
                IO.println(index + ". Player: " + player.toStringRemote());
            }
        }

        /**
         * Gibt an was beim Auslösen des startGame Event-Listeners passieren soll.
         * @param actionEvent - Quelle des Events
         */
        @Override
        public void actionPerformed(ActionEvent actionEvent){
            //Error Handling
            try {
                game.onGameStart();
            } catch (final   NotEnoughPlayerException e) {
                IO.println(e.getMessage());
                return;
            } catch (final TooManyPlayerException e) {
                IO.println(e.getMessage());
                return;
            } catch (final NotEnoughCountriesException e) {
                IO.println(e.getMessage());
                return;
            }
            catch (final GameAllreadyStartedException e ){
                IO.println(e.getMessage());
                return;
            }catch (final PlayerAlreadyHasAnOrderException | RemoteException e){
                IO.println(e.getMessage());
                return;
            }


            IO.println("Willkommen bei Risiko, nun gehts los");
            IO.println("Spielerliste");


            try {
                this.printPlayers(); //Gibt alle Spieler aus
            }catch (RemoteException e){
                IO.println(e.getMessage());
                return;
            }
            GameCUI.this.goIntoChildContext(); //Bewegt die Konsole in den Untergeordneten Kontext


        }
    }


    /**
     * Verwaltet die Benutzerschnittstelle
     * @param game - Das spiel, das die GUI betrifft
     * @throws Exception
     */
    public GameCUI(final IGame game,CUI parent, IPlayer youPlayer) throws RemoteException{
        super(game,parent);
        this.player = youPlayer;
        this.game = game;
        this.remoteEventProcessor = new ClientEventProcessor();

        //Hinzufügen der Listener
        this.addCommandListener(new AddPlayerCommand());
        this.addCommandListener(new StartGameCommand());
        this.addCommandListener(new NextRoundCommandListener());
        this.addCommandListener(new SaveGameCommandListener());
    }

    /**
     * Wird ausgeführt, wenn die Klasse auf eingaben aus der Konsole "horchen" soll
     */
    @Override
    public void listenConsole()  {
        IGame.gameStates currentState;
        try {
            currentState = this.game.getCurrentGameState();
        }catch (RemoteException e){
            IO.println(e.getMessage());
            return;
        }

        if ( currentState == IGame.gameStates.WAITING){
            IO.println("Willkommen bei Risiko mit dem command help erhalten Sie eine Übersicht über die Möglichkeiten");
        }
        super.listenConsole();
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used to create a thread, starting the thread
     * causes the object's <code>run</code> method to be called in that separately executing thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        this.listenConsole();
    }
    /**
     * Gibt an was passieren soll wenn das Komando cd eingeben werden soll
     * Dies ist ähnlich zu verstehen wie ein Ordnerwechsel in Linux
     * @param args Argument, die dem cd befehl übergeben wurden
     *             Für den CD-Befehl gibt es standardmäßig ein Argument.
     *             Diese kann mit args.get("parent") aus der HashMap geholet werden.
     */
    @Override
    protected void goIntoChildContext(LinkedHashMap<String, CommandListenerArgument> args) {
        final IRound round;
        try {
            round = game.getCurrentRound();

        }
        catch (GameNotStartedException | RemoteException  e ){
            IO.println(e.getMessage());
            return;
        }

        RoundCUI roundCUI = new RoundCUI(round, this, this.player);
        super.goIntoChildContext(roundCUI);
    }


}
