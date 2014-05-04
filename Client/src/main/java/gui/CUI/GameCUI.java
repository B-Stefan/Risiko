package main.java.gui.CUI;

import main.java.gui.CUI.utils.CUI;
import main.java.gui.CUI.utils.CommandListener;
import main.java.gui.CUI.utils.CommandListenerArgument;
import main.java.gui.CUI.utils.IO;
import main.java.gui.CUI.exceptions.InvalidCommandListernArgumentException;
import main.java.logic.exceptions.*;
import main.java.logic.Game;
import main.java.logic.Player;
import main.java.logic.Round;

import java.awt.event.ActionEvent;
import java.util.LinkedHashMap;


/**
 * Verwaltet die Benutzerschnittstelle
 * @author Stefan Bieliauskas
 */
public class GameCUI extends CUI {

    private final Game game;


    public class addPlayerCommand extends CommandListener {

        public addPlayerCommand() {
            super("addPlayer");
            this.addArgument(new CommandListenerArgument("playerName"));
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            final String name;
            try {
                name = this.getArgument("playerName").toStr();
            }catch (InvalidCommandListernArgumentException e){
                IO.println(e.getMessage());
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
    public class NextRoundCommandListener extends CommandListener {

        public NextRoundCommandListener() {
            super("next");
            this.addArgument(new CommandListenerArgument("playerName"));
        }

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
           }
           goIntoChildContext();
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
        this.addCommandListener(new NextRoundCommandListener());
    }

    @Override
    public void listenConsole()  {
        if ( this.game.getCurrentGameState() == Game.gameStates.WAITING){
            IO.println("Willkommen bei Risiko mit dem command help erhalten Sie eine Übersicht über die Möglichkeiten");
        }
        super.listenConsole();
    }


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
