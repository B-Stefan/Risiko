package ui.CUI;

import exceptions.NotYourTurnException;
import interfaces.IRound;
import interfaces.data.IPlayer;
import ui.CUI.utils.CUI;
import ui.CUI.utils.CommandListener;
import ui.CUI.utils.CommandListenerArgument;
import ui.CUI.utils.IO;
import exceptions.RoundCompleteException;
import exceptions.ToManyNewArmysException;
import exceptions.TurnNotCompleteException;

import java.awt.event.ActionEvent;
import java.rmi.RemoteException;
import java.util.LinkedHashMap;

public class RoundCUI extends CUI {
    /**
     * Round from server
     */
	private final IRound round;

    /**
     * You as Palyer
     */
    private final IPlayer player;


    public class NextPlayerCommandListener extends CommandListener {

        public NextPlayerCommandListener() {
            super("next");
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                round.setNextTurn(RoundCUI.this.player);
            }catch (TurnNotCompleteException | NotYourTurnException | RemoteException e){
                IO.println(e.getMessage());
                return;
            }catch (ToManyNewArmysException e){
                IO.println(e.getMessage());
                IO.println("Wechseln Sie dazu in den Turn mit cd und plazieren Sie auf Ihren Ländern Einheiten. ");
                return;
            }catch (RoundCompleteException e){
                IO.println(e.getMessage());
                IO.println("Bitte geben Sie next ein um die nächste Runde zu starten");
                goIntoParentContext();
                return;
            }
            goIntoChildContext();
        }
    }

    /**
     * Verwaltet die Eingaben einer Runde
     * @param round - Server Objekt
     * @param parent - Für CUI, der übergeordnete Punkt
     * @param player - Der Spieler der gerade vor der Konsole sitzt
     */
	public RoundCUI(IRound round, CUI parent,IPlayer player){
		super(round, parent);
        this.player = player;
		this.round= round;
        this.addCommandListener(new NextPlayerCommandListener());

	}

    @Override
    protected void goIntoChildContext(LinkedHashMap<String, CommandListenerArgument> args) {
        TurnCUI turn;
        try {
            turn = new TurnCUI(round.getCurrentTurn(), this,this.player);
        }catch (RemoteException e){
            IO.println(e.getMessage());
            return;
        }
        super.goIntoChildContext(turn);

    }
	
}
