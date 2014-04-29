package main.java.gui;

import main.java.logic.exceptions.*;
import main.java.logic.Game;
import main.java.logic.Player;


/**
 * Verwaltet die Benutzerschnittstelle
 * @author Stefan Bieliauskas
 */
public class GameCUI extends CUI {

    private final Game game;

    /**
     * Verwaltet die Benutzerschnittstelle
     * @param game - Das spiel, das die GUI betrifft
     * @throws Exception
     */
    public GameCUI(final Game game) {
        super(game);
        this.game = game;
    }


    @Override
    public void listenConsole() throws Exception {
        if ( this.game.getCurrentGameState() == Game.gameStates.WAITING){
            IO.println("Willkommen bei Risiko mit dem command help erhalten Sie eine Übersicht über die Möglichkeiten");
        }
        super.listenConsole();
    }

    /**
     * Diese Mehtode beschreibt was passieren soll wenn der User eine Eben nach unten geht.
     * @throws Exception
     */
    protected void goIntoChildContext() throws Exception{
        TurnCUI turn = new TurnCUI(game.getCurrentRound().getCurrentTurn(), this);
        this.setChild(turn);
        this.setCurrentState(states.SILENT);
        this.getChild().listenConsole();
    }





    protected void CUIAddPlayer (String[] args) {
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
                    game.onPlayerAdd(name);

            }
        }
        catch (GameAllreadyStartedException e ){
            IO.println(e.getMessage());
        }

    }


    protected void CUIStartGame (String[] args) throws Exception{

        //Error Handling, wenn zu wenig/viele Spieler
        try {
            game.onGameStart();
        } catch (final NotEnoughPlayerException e) {
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
        this.goIntoChildContext();
    }

    protected void CUIShowPlayers(String[] args){
        this.printPlayers();
    }



    private void printPlayers (){
        for (Player player : game.getPlayers()) {
            int index = (game.getPlayers().indexOf(player) + 1);
            IO.println(index + ". Player: " + player.toString());
        }
    }



}
