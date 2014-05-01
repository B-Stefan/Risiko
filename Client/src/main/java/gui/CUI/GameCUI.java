package main.java.gui.CUI;

import main.java.gui.CUI.core.CUI;
import main.java.gui.CUI.core.CommandListener;
import main.java.gui.CUI.core.IO;
import main.java.logic.exceptions.*;
import main.java.logic.Game;
import main.java.logic.Player;
import main.java.logic.Round;

import java.awt.event.ActionEvent;


/**
 * Verwaltet die Benutzerschnittstelle
 * @author Stefan Bieliauskas
 */
public class GameCUI extends CUI {

    private final Game game;


    public class addPlayerCommand extends CommandListener {

        public addPlayerCommand() {
            super("addPlayer");
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            String[] args = this.getArguments();
            if(args.length == 0){
                IO.println("Bitte geben Sie einen Spielernamen ein folgendes Format: AddPlayer <Name> + [<Name>]");
                return;
            }
            else if (args.length == 1 && args[0] == ""){
                IO.println("Bitte geben Sie einen Spielernamen ein folgendes Format: AddPlayer  <Name> + [<Name>]");
                return;
            }

            try {
                for (String name : args){
                    GameCUI.this.game.onPlayerAdd(name);

                }
            }
            catch (GameAllreadyStartedException e ){
                IO.println(e.getMessage());
            }

        }
    }


    public class startGameCommand extends CommandListener {
        public startGameCommand() {
            super("startGame");

        }

        private void printPlayers (){
            for (Player player : GameCUI.this.game.getPlayers()) {
                int index = (GameCUI.this.game.getPlayers().indexOf(player) + 1);
                IO.println(index + ". Player: " + player.toString());
            }
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent){
            //Error Handling, wenn zu wenig/viele Spieler
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
            }

            IO.println("Willkommen bei Risiko, nun gehts los");
            IO.println("Spielerliste");


            this.printPlayers();
            GameCUI.this.goIntoChildContext();


        }
    }


    /**
     * Verwaltet die Benutzerschnittstelle
     * @param game - Das spiel, das die GUI betrifft
     * @throws Exception
     */
    public GameCUI(final Game game) {
        super(game);
        this.game = game;
        this.addCommandListener(new addPlayerCommand());
        this.addCommandListener(new startGameCommand());
    }

    @Override
    public void listenConsole()  {
        if ( this.game.getCurrentGameState() == Game.gameStates.WAITING){
            IO.println("Willkommen bei Risiko mit dem command help erhalten Sie eine Übersicht über die Möglichkeiten");
        }
        super.listenConsole();
    }

    /**
     * Diese Mehtode beschreibt was passieren soll wenn der User eine Eben nach unten geht.
     * @throws Exception
     */
    protected void goIntoChildContext(){
        final Round round;
        try {
            round = game.getCurrentRound();

        }
        catch (Exception e ){
            throw  new RuntimeException(e);
        }

        TurnCUI turn = new TurnCUI(round.getCurrentTurn(), this);
        super.goIntoChildContext(turn);


    }
    protected void goIntoChildContext(String[] args){
        this.goIntoChildContext();
    }


}
