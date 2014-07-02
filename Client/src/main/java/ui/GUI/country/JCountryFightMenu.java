package ui.GUI.country;
import interfaces.IFight;
import interfaces.ITurn;
import interfaces.data.ICountry;
import interfaces.data.IPlayer;
import server.logic.ClientEventProcessor;
import ui.GUI.utils.JExceptionDialog;
import ui.GUI.utils.JModalDialog;
import exceptions.*;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

/**
 * Created by Stefan on 09.06.14.
 */
public class JCountryFightMenu extends JCountryNeighborsMenu {

    private final ITurn turn;
    private final ICountry country;
	private final IPlayer clientPlayer;
    private final ClientEventProcessor remoteEventProcessor;
    public class NeighborActionListener implements ActionListener{

        /**
         * Invoked when an action occurs.
         *
         * @param event
         */
        @Override
        public void actionPerformed(ActionEvent event){
            if(event.getActionCommand() == "onCountryClick"){
                ICountry from    =  JCountryFightMenu.this.country;
                ICountry to      =  JCountryFightMenu.this.getSelectedNeighborsMenuItem().getCountry();
                IFight fight;
                try {
                    fight = JCountryFightMenu.this.turn.fight(from, to, clientPlayer);
                }catch (TurnNotInCorrectStepException | NotYourTurnException | TurnNotAllowedStepException | ToManyNewArmysException | NotTheOwnerException | RemoteException | RemoteCountryNotFoundException e ){
                    new JExceptionDialog(JCountryFightMenu.this,e);
                    return;
                }
                JPopupMenu menu = (JPopupMenu) JCountryFightMenu.this.getParent();
                try {
                    JModalDialog modal = new JFightGUI(menu.getInvoker(),fight,JCountryFightMenu.this.remoteEventProcessor, clientPlayer);
                    SwingUtilities.invokeLater(modal);
                }catch (RemoteException e){
                    new JExceptionDialog(JCountryFightMenu.this,e);
                    return;
                }

            }
        }
    }
    public JCountryFightMenu(final ICountry country, final ITurn turn, final ClientEventProcessor remoteEventProcessor, IPlayer clientPlayer) throws RemoteException{
        super("Fight",country);
        this.country = country;
        this.clientPlayer = clientPlayer;
        this.turn = turn;
        this.remoteEventProcessor = remoteEventProcessor;
        this.addActionListener(new NeighborActionListener());

    }
}
