package ui.GUI.country;

//import com.sun.codemodel.internal.JMod;
import interfaces.IFight;
import server.logic.ClientEventProcessor;
import server.logic.IFightActionListener;
import ui.GUI.utils.JExceptionDialog;
import ui.GUI.utils.JModalDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

/**star
 * Created by Stefan on 18.06.14.
 */
public class JFightGUI extends JModalDialog {
    private final IFight fight;
    private final JFightSide aggressorSide;
    private final JFightSide defenderSide;
    private final ClientEventProcessor remoteEventsProcessor;
    private final ActionListener fightUpdateUIListener;

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
            }catch (RemoteException e){
                new JExceptionDialog(JFightGUI.this,e);
            }
        }
    }
    private class CloseBtnListener implements ActionListener {

        /**
         * Invoked when an action occurs.
         *
         * @param e
         */
        @Override
        public void actionPerformed(ActionEvent e) {

            JFightGUI.this.remoteEventsProcessor.removeUpdateUIListener(JFightGUI.this.fightUpdateUIListener);
            JFightGUI.this.dispose();

        }
    }
    public JFightGUI(final Component parent, final IFight fight, final ClientEventProcessor remoteEventsProcessor) throws RemoteException {
        super(parent,"Fight",ModalityType.APPLICATION_MODAL);
        this.fight = fight;
        this.setLayout(new BorderLayout(5,5));
        this.aggressorSide  = new JFightSide(this.fight, JFightSide.sides.AGGRESSOR);
        this.defenderSide  = new JFightSide(this.fight, JFightSide.sides.DEFENDER);
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


}
