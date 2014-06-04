package main.java.gui.CUI;

import main.java.gui.CUI.exceptions.InvalidCommandListernArgumentException;
import main.java.gui.CUI.utils.CUI;
import main.java.gui.CUI.utils.CommandListener;
import main.java.gui.CUI.utils.CommandListenerArgument;
import main.java.gui.CUI.utils.IO;
import main.java.GameManager;
import main.java.logic.Game;
import java.awt.event.ActionEvent;
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
    private final GameManager gameManager;

    public class NextPlayerCommandListener extends CommandListener {

        public NextPlayerCommandListener() {
            super("show","Gibt die Liste der gespeicherten Spiele aus");
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            int i = 0;
            for(Game game: gameManager.getGameList()){
                i++;
                IO.println(i + ". "+game);
            }
        }
    }


    /**
     * Verwaltet die Benutzerschnittstelle
     * @param gameManager - Das spiel, das die GUI betrifft
     */
    public GameManagerCUI(final GameManager gameManager) {
        super(gameManager);
        this.gameManager = gameManager;
        this.addCommandListener(new NextPlayerCommandListener());

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
        List<Game> games = this.gameManager.getGameList();
        if(index < 0 || games.size() > (index+1)){
            IO.println("Bitte geben Sie einen gültigen Index ein");
        }
        Game game = games.get(index);
        super.goIntoChildContext(new GameCUI(game));
    }


}
