package main.java.gui;

import main.java.logic.exceptions.*;
import main.java.logic.*;
import main.resources.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;

/**
 * Verwaltet die Benutzerschnittstelle
 * @author Stefan Bieliauskas
 */
public class GameCUI  {

    private final IGameGUI game;
    private final IMapGUI map;

    /**
     * Verwaltet die Benutzerschnittstelle
     * @param game - Das spiel, das die GUI betrifft
     * @throws Exception
     */
    public GameCUI(final IGameGUI game, final IMapGUI map) throws Exception {
        this.game = game;
        this.map = map;

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
    public void parseCommand(final String commands) throws Exception {
        final String action = commands.toUpperCase();
        final String params = commands;


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
            } catch (final NotEnoughPlayerException e) {
                IO.println(e.getMessage());
                return;
            } catch (final TooManyPlayerException e) {
                IO.println(e.getMessage());
                return;
            } catch (final NotEnoughCountriesException e) {

            return;
        }

            IO.println("Willkommen bei Risiko, nun gehts los");
            IO.println("Spielerliste");

            for (Player player : game.getPlayers()) {
                int index = (game.getPlayers().indexOf(player) + 1);
                IO.println(index + ". Player: " + player.toString());
            }
            this.printMap();
            this.distributeArmys();
        }
        else if (action.contains("SHOW MAP")){
            this.printMap();
        }
    }
    private void distributeArmys() {
        IO.println("Nachfolgend müssen alle Spieler Ihre Einheiten verteilen.");


    }
    private void printMap (){
        ArrayList<Country> countries = map.getCountries();
        IO.println("Aktueller Status der Karte");
        for(Country country: countries){
            IO.println(country.toString());

        }
    }
}
