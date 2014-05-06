package main.java.gui.CUI;
import java.awt.event.ActionEvent;
import java.util.LinkedHashMap;

import main.java.gui.CUI.exceptions.InvalidCommandListernArgumentException;
import main.java.logic.*;
import main.java.gui.CUI.utils.*;
import main.java.logic.exceptions.*;

public class FightCUI extends CUI {
	private final Fight fight;
	
	public class continueFightingCommandListener extends CommandListener{
		
		public continueFightingCommandListener(){
			super("fight" , "Führt einen(weiteren) Angriff aus");
            this.addArgument(new CommandListenerArgument("numberOfArmys"));
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
            final int numberOfArmies;
            try {
                numberOfArmies = this.getArgument("numberOfArmys").toInt();

            }catch (InvalidCommandListernArgumentException e){
                IO.println(e.getMessage());
                return;
            }
            try {
                fight.armyVsArmy(numberOfArmies);
            }catch (NotEnoughArmiesToAttackException e){
                IO.println(e.getMessage());
                return;
            }catch (NotEnoughArmiesToDefendException e){
                IO.println(e.getMessage());
                return;
            }catch (InvalidAmountOfArmiesException e){
                IO.println(e.getMessage());
                return;
            }catch (InvalidPlayerException e){
                IO.println(e.getMessage());
                return;
            }catch (CountriesNotConnectedException e){
                IO.println(e.getMessage());
                return;
            }catch (ToManyArmiesToAttackException e ){
                IO.println(e.getMessage());
                return;
            }
		}
	}

	public class stopFightingCommandListener extends CommandListener{
		public stopFightingCommandListener(){
			super("stop", "Beendet den aktuellen Kampf");
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			IO.println("Du hast den Kampf beendet");
            goIntoParentContext();
			
		}
		
	}
	
	public FightCUI(Fight fight, CUI parent){
		super(fight, parent);
		this.fight = fight;
		this.addCommandListener(new continueFightingCommandListener());
		this.addCommandListener(new stopFightingCommandListener());
	}

    @Override
    protected void goIntoChildContext(LinkedHashMap<String, CommandListenerArgument> args) {

    }
	
}
