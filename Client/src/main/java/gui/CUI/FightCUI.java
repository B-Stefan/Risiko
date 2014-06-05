package main.java.gui.CUI;

import java.awt.event.ActionEvent;
import java.util.LinkedHashMap;

import main.java.gui.CUI.exceptions.InvalidCommandListernArgumentException;
import main.java.gui.CUI.utils.CUI;
import main.java.gui.CUI.utils.CommandListener;
import main.java.gui.CUI.utils.CommandListenerArgument;
import main.java.gui.CUI.utils.IO;
import main.java.logic.Fight;
import main.java.logic.exceptions.*;
import main.java.logic.utils.*;

public class FightCUI extends CUI {
	
	/**
	 * Der momentane Fight
	 */
	private final Fight fight;
	
	public class attackingCommandListener extends CommandListener{
		/**
         * Konstruktor, der Name und Argumente des Befehls festlegen
         */		
		public attackingCommandListener() {
			super("attack", "Zum angreifen des zuvor ausgewählten Landes");
			this.addArgument(new CommandListenerArgument("noOfArmies"));
		}


		@Override
		/**
		 * Wird ausgeführt, wenn der Befehl eingegeben wurde
		 * Printet das Würfelergebniss des Angreifers
		 */
		public void actionPerformed(ActionEvent arg0) {
			final int noOfArmies;
			
			try {
                noOfArmies = this.getArgument("noOfArmies").toInt();
            }catch (InvalidCommandListernArgumentException e){
                IO.println(e.getMessage());
                return;
            }
			
			try{
				fight.attacking(noOfArmies);
			}catch (NotEnoughArmiesToAttackException e){
                IO.println(e.getMessage());
                return;
            }catch (InvalidAmountOfArmiesException e){
                IO.println(e.getMessage());
                return;
            } catch (AlreadyDicedException e) {
				IO.println(e.getMessage());
				return;
			} catch (InvalidFightException e) {
				IO.println(e.getMessage());
				return;
			}
			IO.println("");
			IO.println("Angreifer Würfel:");
			for(Dice d : fight.getAgressorsDice()){
				IO.println(d.toString());
			}
			IO.println("");
		}		
	}
	
	public class defendingCommandListener extends CommandListener{
		/**
         * Konstruktor, der Name und Argumente des Befehls festlegen
         */
		public defendingCommandListener() {
			super("defend", "Zum Verteidigen eines Angriffs");
			this.addArgument(new CommandListenerArgument("noOfArmies"));
		}

		@Override
		/**
		 * Wird ausgeführt, wenn der Befehl eingegeben wird
		 * Printet die Würfel des Verteidigers 
		 * Printet das Ergebnis
		 */
		public void actionPerformed(ActionEvent arg0) {
			final int noOfArmies;
			
			try {
                noOfArmies = this.getArgument("noOfArmies").toInt();
            }catch (InvalidCommandListernArgumentException e){
                IO.println(e.getMessage());
                return;
            }
			
			try{
				fight.defending(noOfArmies);
			}catch (NotEnoughArmiesToDefendException e){
                IO.println(e.getMessage());
                return;
            }catch (InvalidAmountOfArmiesException e){
                IO.println(e.getMessage());
                return;
            }catch (CountriesNotConnectedException e){
            	IO.println(e.getMessage());
            	return;
            } catch (AlreadyDicedException e) {
				IO.println(e.getMessage());
				return;
			} catch (TurnNotAllowedStepException e) {
				IO.println(e.getMessage());
				return;
			} catch (TurnNotInCorrectStepException e) {
				IO.println(e.getMessage());
				return;
			} catch (ArmyAlreadyMovedException e) {
				IO.println(e.getMessage());
				return;
			} catch (InvalidFightException e) {
				IO.println(e.getMessage());
				return;
			}
			IO.println("");
			IO.println("Verteidiger Würfel:");
			for(Dice d : fight.getDefendersDice()){
				IO.println(d.toString());
			}
			IO.println("");
			//Erste Zeile Angreifer, zweite Zeile Verteidiger, dritte Zeile übernommen ja (1) & nein (0)
			int[] result = fight.getResult();
			IO.println("Der Angreifer hat " + result[0] + " verloren");
			IO.println("Der Verteidiger hat " + result[1] + " verloren");
			IO.println("");
			if(result[2] == 1){
				IO.println("Der Angreifer hat " + fight.getTo().getName() + " erobert");
				IO.println("");
			}
		}
		
	}
	
	
	/**
	 * Listener werden registriert und Fight Attribut wird gesetzt
	 * @param fight
	 * @param parent
	 */
	
	protected FightCUI(Fight fight, CUI parent) {
		super(fight, parent);
		this.fight = fight;
		this.addCommandListener(new defendingCommandListener());
		this.addCommandListener(new attackingCommandListener());		
	}


	@Override
	protected void goIntoChildContext(LinkedHashMap<String, CommandListenerArgument> args) {
		// TODO Auto-generated method stub
	}



}

