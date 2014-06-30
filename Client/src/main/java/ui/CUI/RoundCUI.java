package ui.CUI;

import interfaces.IRound;
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
	private final IRound round;

    public class NextPlayerCommandListener extends CommandListener {

        public NextPlayerCommandListener() {
            super("next");
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                round.setNextTurn();
            }catch (TurnNotCompleteException | RemoteException e){
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

	public RoundCUI(IRound fight, CUI parent){
		super(fight, parent);
		this.round= fight;
        this.addCommandListener(new NextPlayerCommandListener());

	}

    @Override
    protected void goIntoChildContext(LinkedHashMap<String, CommandListenerArgument> args) {
        TurnCUI turn;
        try {
            turn = new TurnCUI(round.getCurrentTurn(), this);
        }catch (RemoteException e){
            IO.println(e.getMessage());
            return;
        }
        super.goIntoChildContext(turn);

    }
	
}
