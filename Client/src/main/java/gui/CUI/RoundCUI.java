package main.java.gui.CUI;

import main.java.gui.CUI.core.CUI;
import main.java.gui.CUI.core.CommandListener;
import main.java.gui.CUI.core.CommandListenerArgument;
import main.java.gui.CUI.core.IO;
import main.java.gui.CUI.core.exceptions.InvalidCommandListernArgumentException;
import main.java.logic.Fight;
import main.java.logic.Round;
import main.java.logic.exceptions.GameAllreadyStartedException;
import main.java.logic.exceptions.RoundCompleteException;
import main.java.logic.exceptions.ToManyNewArmysException;
import main.java.logic.exceptions.TurnNotCompleteException;

import java.awt.event.ActionEvent;
import java.util.LinkedHashMap;

public class RoundCUI extends CUI {
	private final Round round;

    public class NextPlayerCommandListener extends CommandListener {

        public NextPlayerCommandListener() {
            super("next");
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                round.setNextTurn();
            }catch (TurnNotCompleteException e){
                IO.println("Der Zug ist nicht nicht abgeschlossen wechsel mit cd in den Turn und gebe dir mit state den status aus");
            }catch (ToManyNewArmysException e){
                IO.println(e.getMessage());
                IO.println("Wechseln Sie dazu in den Turn mit cd und plazieren Sie auf Ihren Ländern Einheiten. ");
            }catch (RoundCompleteException e){
                IO.println(e.getMessage());
                IO.println("Bitte geben Sie next ein um die nächste Runde zu starten");
                goIntoParentContext();
            }
        }
    }

	public RoundCUI(Round fight, CUI parent){
		super(fight, parent);
		this.round= fight;
        this.addCommandListener(new NextPlayerCommandListener());

	}

    @Override
    protected void goIntoChildContext(LinkedHashMap<String, CommandListenerArgument> args) {
        TurnCUI turn = new TurnCUI(round.getCurrentTurn(), this);
        super.goIntoChildContext(turn);
    }
	
}
