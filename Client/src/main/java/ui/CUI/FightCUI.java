package ui.CUI;

import java.awt.event.ActionEvent;
import java.rmi.RemoteException;
import java.util.LinkedHashMap;
import java.util.List;

import exceptions.*;
import interfaces.IFight;
import interfaces.data.IPlayer;
import interfaces.data.utils.IDice;
import ui.CUI.exceptions.InvalidCommandListernArgumentException;
import ui.CUI.utils.CUI;
import ui.CUI.utils.CommandListener;
import ui.CUI.utils.CommandListenerArgument;
import ui.CUI.utils.IO;


public class FightCUI extends CUI {
	
	/**
	 * Der momentane Fight
	 */
	private final IFight fight;

    /**
     * Der Spieler der die Konsole steuert
     */
    private final IPlayer player;
	
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
		public void actionPerformed(ActionEvent arg) {
			final int noOfArmies;

			try {
                noOfArmies = this.getArgument("noOfArmies").toInt();
            }catch (InvalidCommandListernArgumentException e){
                IO.println(e.getMessage());
                return;
            }

			try{
				fight.attacking(noOfArmies, FightCUI.this.player);
			}catch (NotEnoughArmiesToAttackException | InvalidFightException | InvalidAmountOfArmiesException | AlreadyDicedException | RemoteException | YouCannotAttackException e)
            {
                IO.println(e.getMessage());
                return;
            }
			IO.println("");
			IO.println("Angreifer Würfel:");
            List<? extends IDice> agressorDices;
            try {
                agressorDices = fight.getAgressorsDice();
                for(IDice d : agressorDices){
                    IO.println(d.toStringRemote());
                }
            }catch (RemoteException e){
                e.printStackTrace();
                IO.println(e.getMessage());
                return;
            }
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
		public void actionPerformed(ActionEvent arg) {
			final int noOfArmies;
			
			try {
                noOfArmies = this.getArgument("noOfArmies").toInt();
            }catch (InvalidCommandListernArgumentException e){
                IO.println(e.getMessage());
                return;
            }
			
			try{
				fight.defending(noOfArmies,FightCUI.this.player);
			}catch (NotEnoughArmiesToDefendException e){
                IO.println(e.getMessage());
                return;
            }catch (InvalidAmountOfArmiesException e){
                IO.println(e.getMessage());
                return;
            }catch (NotEnoughArmysToMoveException e){
                IO.println(e.getMessage());
                return;
            }
            catch (CountriesNotConnectedException e){
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
			}catch (NotTheOwnerException | ToManyNewArmysException | AggessorNotThrowDiceException | RemoteException | RemoteCountryNotFoundException | CountryNotInListException | YouCannotDefendException e) {
                IO.println(e.getMessage());
                return;
            }
            try  {
                IO.println("");
                IO.println("Verteidiger Würfel:");
                for(IDice d : fight.getDefendersDice()){
                    IO.println(d.toStringRemote());
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
            }catch (RemoteException e){
                e.printStackTrace();
                IO.println(e.getMessage());
                return;
            }
		}
		
	}
	
	
	/**
	 * Listener werden registriert und Fight Attribut wird gesetzt
	 * @param fight
	 * @param parent
	 */
	
	protected FightCUI(IFight fight, CUI parent,IPlayer player) {
		super(fight, parent);
		this.fight = fight;
        this.player = player;
		this.addCommandListener(new defendingCommandListener());
		this.addCommandListener(new attackingCommandListener());		
	}


	@Override
	protected void goIntoChildContext(LinkedHashMap<String, CommandListenerArgument> args) {
		// TODO Auto-generated method stub
	}



}

