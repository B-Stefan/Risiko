package main.java.gui;

import main.java.logic.exceptions.*;
import main.java.logic.*;
import main.resources.IGameGUI;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Verwaltet die Benutzerschnittstelle
 * @author Stefan Bieliauskas
 */
public class GameCUI {

    private IGameGUI game;

    /**
     * Verwaltet die Benutzerschnittstelle
     * @param game - Das spiel, das die GUI betrifft
     * @throws Exception
     */
    public GameCUI(final IGameGUI game) throws Exception {
        this.game = game;

    }

    /**
     * Initiiert das 'horchen'
     * @throws Exception
     */
    public void init() throws Exception {
        IO.println("Willkommen bei Risiko mit dem command help erhalten Sie eine Übersicht über die Möglichkeiten");
        String command = "";
        while (true) {
            command = IO.readString();
            parseCommand(command);
        }
    }

    /**
     * Überprüft das Command nach Aktionen und führt diese anschließend aus.
     * @param commands
     * @throws Exception
     */
    public void parseCommand(String commands) throws Exception {
        String action = commands.toUpperCase();
        String params = commands;


        if (action.contains("ADD PLAYER")) {
            String playerName = params.substring(params.indexOf("Player") + 7, params.length());
            game.onPlayerAdd(playerName);
        } else if (action.contains("REMOVE PLAYER")) {
            //@todo Remove Player einbauen
            throw new NotImplementedException();

        } else if (action.contains("START GAME")) {

            //Error Handling, wenn zu wenig/viele Spieler
            try {
                game.onGameStart();
            } catch (NotEnoughPlayerException e) {
                IO.println(e.getMessage());
                return;
            } catch (TooManyPlayerException e) {
                IO.println(e.getMessage());
                return;
            }

            IO.println("Willkommen bei Risiko, nun gehts los");
            IO.println("Spielerliste");

            for (Player player : game.getPlayers()) {
                int index = (game.getPlayers().indexOf(player) + 1);
                IO.println(index + ". Player: " + player.getName());
            }
        }
    }
}
