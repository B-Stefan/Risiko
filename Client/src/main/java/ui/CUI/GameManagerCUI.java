package ui.CUI;

import exceptions.PersistenceEndpointIOException;
import interfaces.IGame;
import interfaces.IGameManager;
import ui.CUI.exceptions.InvalidCommandListernArgumentException;
import ui.CUI.utils.CUI;
import ui.CUI.utils.CommandListener;
import ui.CUI.utils.CommandListenerArgument;
import ui.CUI.utils.IO;

import java.awt.event.ActionEvent;
import java.rmi.RemoteException;
import java.util.LinkedHashMap;
import java.util.List;


/**
 * Verwaltet die Benutzerschnittstelle für ein Spiel
 * @author Stefan Bieliauskas
 */
public class GameManagerCUI extends CUI {

    /**
     * Bildet das Spiel ab für die die CUI erstellt wird
     */
    private final IGameManager gameManager;

    public class ShowGamesCommandListener extends CommandListener {

        /**
         * Listener, um alle Spiele anzuzeigen
         */
        public ShowGamesCommandListener() {
            super("show","Gibt die Liste der gespeicherten Spiele aus");
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            int i = 0;
            List<IGame> gameList;
            try {
                gameList = gameManager.getGameList();
            }catch (PersistenceEndpointIOException | RemoteException e ){
                IO.println(e.getMessage());
                return;
            }

            for(IGame game: gameList){
                i++;
                IO.println(i + ". "+game);
            }
        }

    }

    /**
     * Listener, um ein neues Spiel zu starten
     */
    public class NewGameCommandListener extends CommandListener {

        public NewGameCommandListener() {
            super("new","Erstellt ein neues Spiel");
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {

           IGame newGame;
            try {
                newGame = gameManager.addGame();
            }catch (PersistenceEndpointIOException | RemoteException e){
                IO.println(e.getMessage());
                return;
            }
           goIntoChildContext(new GameCUI(newGame,GameManagerCUI.this));
        }

    }

    /**
     * Listener, um ein Spiel zu speichern.
     */
    public class SaveGameCommandListener extends CommandListener {

        public SaveGameCommandListener() {
            super("save","Speichert ein Spiel");
            this.addArgument(new CommandListenerArgument("listNumberOfGameToSave"));
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {

            int index;
            try {
                index = this.getArgument("listNumberOfGameToSave").toInt();
            }catch (InvalidCommandListernArgumentException e){
                IO.println(e.getMessage());
                return;
            }

            index--;

            try {
                gameManager.saveGame(index);
            }catch (PersistenceEndpointIOException | RemoteException e){
                IO.println(e.getMessage());
                return;
            }
            IO.println("Game wurde gespeichert");
        }

    }


    /**
     * Verwaltet die Benutzerschnittstelle
     * @param gameManager - Der Manager den die CUI verwalten soll
     */
    public GameManagerCUI(final IGameManager gameManager) {
        super(gameManager);
        this.gameManager = gameManager;
        this.addCommandListener(new ShowGamesCommandListener());
        this.addCommandListener(new NewGameCommandListener());
        this.addCommandListener(new SaveGameCommandListener());

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
        int index = 0;
        try {
             index = args.get("parent").toInt()-1;
        }catch (InvalidCommandListernArgumentException e){
            IO.println(e.getMessage());
            return;
        }
        List<IGame> games;
        try {
            games = this.gameManager.getGameList();
        }catch (PersistenceEndpointIOException | RemoteException e){
            IO.println(e.getMessage());
            return;
        }


        IGame game;
        try {
            game  = games.get(index);
        }catch (IndexOutOfBoundsException e ){
            IO.println("Bitte geben Sie einen gültigen Index ein");
            return;
        }
        super.goIntoChildContext(new GameCUI(game,this));
    }


}
