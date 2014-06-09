package main.java.ui.CUI;

import main.java.ui.CUI.utils.CUI;
import main.java.ui.CUI.utils.CommandListener;
import main.java.ui.CUI.utils.CommandListenerArgument;
import main.java.ui.CUI.utils.IO;
import main.java.ui.CUI.exceptions.InvalidCommandListernArgumentException;
import main.java.logic.exceptions.*;
import main.java.logic.Game;
import main.java.logic.data.Player;
import main.java.logic.Round;

import java.awt.event.ActionEvent;
import java.util.LinkedHashMap;


/**
 * Verwaltet die Benutzerschnittstelle für ein Spiel
 * @author Stefan Bieliauskas
 */
public class GameCUI extends CUI {

    /**
     * Bildet das Spiel ab für die die CUI erstellt wird
     */
    private final Game game;


    /**
     * Klasse für Event-Listener zum hinzufügen eines Spielers
     */
    public class addPlayerCommand extends CommandListener {

        /**
         * Konstruktor, der Name und Argumente des Befehls festlegen
         */
        public addPlayerCommand() {
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
                GameCUI.this.game.onPlayerAdd(name);
            }
            catch (GameAllreadyStartedException e ){
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
           }catch (RoundNotCompleteException e){
               IO.println(e.getMessage());
               return;
           }catch (ToManyNewArmysException e){
               IO.println(e.getMessage());
               return;
           }catch (GameNotStartedException e){
               IO.println(e.getMessage());
               return;
           }catch (GameIsCompletedException e){
               IO.printHeadline("Spielende");

               IO.println("Das Spiel wurde gewonnen, ein Spieler hat seinen Auftrag erfüllt");

               IO.printHeadline("AND");
               IO.printHeadline("The");
               IO.printHeadline("Winner");
               IO.printHeadline("IS");

               IO.printHeadline("");
               IO.printHeadline(game.getWinner().toString());
               IO.printHeadline("");
               return;
           }
           goIntoChildContext();
        }

    }


    /**
     * Klasse für Event-Listenerz zum Starten des Spiels
     */
    public class startGameCommand extends CommandListener {

        /**
         * Legt den Befehl fest, der zum starten des Spiels verwendet wird
         */
        public startGameCommand() {
            super("startGame", "Started das Spiel");

        }

        /**
         * Gibt alle Player auf der Konsole aus
         */
        private void printPlayers (){
            for (Player player : GameCUI.this.game.getPlayers()) {
                int index = (GameCUI.this.game.getPlayers().indexOf(player) + 1);
                IO.println(index + ". Player: " + player.toString());
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
            }catch (final PlayerAlreadyHasAnOrderException e){
                IO.println(e.getMessage());
                return;
            }


            IO.println("Willkommen bei Risiko, nun gehts los");
            IO.println("Spielerliste");


            this.printPlayers(); //Gibt alle Spieler aus
            GameCUI.this.goIntoChildContext(); //Bewegt die Konsole in den Untergeordneten Kontext


        }
    }


    /**
     * Verwaltet die Benutzerschnittstelle
     * @param game - Das spiel, das die GUI betrifft
     * @throws Exception
     */
    public GameCUI(final Game game,CUI parent) {
        super(game,parent);
        this.game = game;

        //Hinzufügen der Listener
        this.addCommandListener(new addPlayerCommand());
        this.addCommandListener(new startGameCommand());
        this.addCommandListener(new NextRoundCommandListener());
    }

    /**
     * Wird ausgeführt, wenn die Klasse auf eingaben aus der Konsole "horchen" soll
     */
    @Override
    public void listenConsole()  {
        if ( this.game.getCurrentGameState() == Game.gameStates.WAITING){
            IO.println("Willkommen bei Risiko mit dem command help erhalten Sie eine Übersicht über die Möglichkeiten");
        }
        super.listenConsole();
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
        final Round round;
        try {
            round = game.getCurrentRound();

        }
        catch (GameNotStartedException e ){
            IO.println(e.getMessage());
            return;
        }

        RoundCUI roundCUI = new RoundCUI(round, this);
        super.goIntoChildContext(roundCUI);
    }


}
