/*
 * RISIKO-JAVA - Game, Copyright 2014  Jennifer Theloy, Stefan Bieliauskas  -  All Rights Reserved.
 * Hochschule Bremen - University of Applied Sciences
 *
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 * Contact:
 *     Jennifer Theloy: jTheloy@stud.hs-bremen.de
 *     Stefan Bieliauskas: sBieliauskas@stud.hs-bremen.de
 *
 * Web:
 *     https://github.com/B-Stefan/Risiko
 *
 */



package ui.GUI.country;

import interfaces.IFight;
import interfaces.data.IPlayer;
import server.logic.ClientEventProcessor;
import server.logic.IFightActionListener;
import ui.CUI.FightCUI;
import ui.GUI.utils.JExceptionDialog;
import ui.GUI.utils.JModalDialog;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;


public class JFightGUI extends JModalDialog {
    private final IFight fight;
    private final JFightSide aggressorSide;
    private final JFightSide defenderSide;
    private final ClientEventProcessor remoteEventsProcessor;
    private final ActionListener fightUpdateUIListener;
	private final IPlayer clientPlayer;

    private class UpdateUIFightListener implements ActionListener{


        /**
         * Invoked when an action occurs.
         *
         * @param event
         */
        @Override
        public void actionPerformed(ActionEvent event) {
            try{
                update();

                if(JFightGUI.this.fight.getDefendersDice().size()>0){
                    JFightGUI.this.showResult();
                }

            }catch (RemoteException e){
                new JExceptionDialog(JFightGUI.this,e);
            }
        }
    }
    private class CloseBtnListener implements ActionListener {

        /**
         * Invoked when an action occurs.
         *
         * @param event
         */
        @Override
        public void actionPerformed(ActionEvent event) {

            boolean isValid;
            try{
                isValid = JFightGUI.this.fight.isValidToClose();
            }catch (RemoteException e){
                new JExceptionDialog(JFightGUI.this,e);
                return;
            }

            if(isValid) {
                JFightGUI.this.dispose();
            }else {
                JModalDialog.showInfoDialog(JFightGUI.this,"Info", "Der Fight ist noch nicht abgeschlossen");
            }
        }
    }
    public void showResult(){
        int[] result;
        try {
            result = JFightGUI.this.fight.getResult();
        }catch (RemoteException e){
            new JExceptionDialog(JFightGUI.this,e);
            return;
        }
        int defenderLostArmies = result[1];
        int aggressorLostArmies = result[0];
        int aggresorWon = result[2];
        if (aggresorWon == 1){
            JModalDialog.showInfoDialog(JFightGUI.this, "Angriff erfolgreich", "Der Angreifer hat das Land übernommen");
            //Close Window
            JFightGUI.this.dispose();
        }else{
            String str = String.format("Der Angreifer hat " + aggressorLostArmies + " Armeen verloren %n");
            str  += String.format("Der Verteidiger hat " + defenderLostArmies + " Armeen verloren %n");
            str  += "Der Kampf geht weiter ";
            JModalDialog.showInfoDialog(JFightGUI.this, "Erfolgreich verteidigt", str);
        }

    }
    public JFightGUI(final Component parent, final IFight fight, final ClientEventProcessor remoteEventsProcessor, IPlayer clientPlayer) throws RemoteException {
        super(parent,"Fight",ModalityType.APPLICATION_MODAL);
        this.fight = fight;
        this.clientPlayer = clientPlayer;
        this.setLayout(new BorderLayout(5,5));
        this.aggressorSide  = new JFightSide(this.fight, JFightSide.sides.AGGRESSOR, this.clientPlayer);
        this.defenderSide  = new JFightSide(this.fight, JFightSide.sides.DEFENDER, this.clientPlayer);
        this.remoteEventsProcessor = remoteEventsProcessor;
        this.fightUpdateUIListener = new UpdateUIFightListener();
        this.remoteEventsProcessor.addUpdateUIListener(fightUpdateUIListener);

        JPanel centerPanel = new JPanel();

        centerPanel.add(this.aggressorSide);
        centerPanel.add(this.defenderSide);

        centerPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        this.add(centerPanel,BorderLayout.CENTER);

        //Close btn
        JButton closeBtn = new JButton("Kampf verlassen");
        closeBtn.addActionListener(new CloseBtnListener());
        this.add(closeBtn,BorderLayout.SOUTH);

    }
    public void update() throws RemoteException{
        this.aggressorSide.update();
        this.defenderSide.update();
    }

    /**
     * Override the dispose method
     */
    @Override
    public void dispose(){
        this.remoteEventsProcessor.removeUpdateUIListener(this.fightUpdateUIListener);
        super.dispose();
    }

}
