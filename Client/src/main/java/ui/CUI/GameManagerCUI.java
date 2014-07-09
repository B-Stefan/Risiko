package ui.CUI;

import exceptions.PersistenceEndpointIOException;
import interfaces.IGame;
import interfaces.IGameManager;
import interfaces.data.IPlayer;
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

    /**
     * Der Spieler der gerade vor dem Bildschirm sitzt
     */
    private final IPlayer player;

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
                gameList = gameManager.getSavedGameList();
                for(IGame game: gameList){
                    i++;
                    IO.println(i + ". "+game.toStringRemote());
                }
            }catch (PersistenceEndpointIOException | RemoteException e ){
                IO.println(e.getMessage());
                return;
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
            try {
                goIntoChildContext(new GameCUI(newGame,GameManagerCUI.this,GameManagerCUI.this.player));
            }catch (RemoteException e){
                IO.println(e.getMessage());
                return;
            }

        }

    }




    /**
     * Verwaltet die Benutzerschnittstelle
     * @param gameManager - Der Manager den die CUI verwalten soll
     * @param player - Der Spieler der vor der Konsole sitzt
     */
    public GameManagerCUI(final IGameManager gameManager, final IPlayer player) {
        super(gameManager);
        this.gameManager = gameManager;
        this.player = player;
        this.addCommandListener(new ShowGamesCommandListener());
        this.addCommandListener(new NewGameCommandListener());

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
            games = this.gameManager.getSavedGameList();
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
        GameCUI gameCUI;
        try {
            gameCUI = new GameCUI(game,this,this.player);
        }catch (RemoteException e){
            e.printStackTrace();
            return;
        }
        super.goIntoChildContext(gameCUI);
    }


}
