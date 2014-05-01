package main.java.gui.CUI;
import java.awt.event.ActionEvent;

import main.java.logic.*;
import main.java.gui.CUI.core.*;

public class FightCUI extends CUI {
	private final Fight fight;
	
	public class continueFightingCommandListener extends CommandListener{
		
		public continueFightingCommandListener(){
			super("fight" , "Führt einen(weiteren) Angriff aus");
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			
		}
	}

	public class stopFightingCommandListener extends CommandListener{
		public stopFightingCommandListener(){
			super("stop", "Beendet den aktuellen Kampf");
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	public FightCUI(Fight fight, CUI parent){
		super(fight, parent);
		this.fight = fight;
		this.addCommandListener(new continueFightingCommandListener());
		this.addCommandListener(new stopFightingCommandListener());
	}

	@Override
	protected void goIntoChildContext() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void goIntoChildContext(String[] args) {
		// TODO Auto-generated method stub
		
	}
	
}
