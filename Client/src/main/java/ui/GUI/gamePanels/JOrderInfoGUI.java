package ui.GUI.gamePanels;

import javax.swing.*;

import exceptions.GameNotStartedException;
import ui.GUI.utils.JExceptionDialog;
import interfaces.IGame;
import interfaces.data.IPlayer;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

public class JOrderInfoGUI extends JPanel {

	private final IGame game;
	private JButton hide = new JButton("Uncover Order");
	private boolean hidden = true;
	private final JTextArea info = new JTextArea("");

	
	private class HideActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			try {
				if(hidden){
					hide.setText("Hide Order");
					setInfo();
					hidden = false;
				}else if(!hidden){
					hide.setText("Uncover Order");
					if(game.getCurrentGameState() == IGame.gameStates.RUNNING){
		                info.setText("");
					}
					hidden = true;
				}
			} catch (RemoteException | GameNotStartedException e) {
				new JExceptionDialog(e);
				return;
			}
		}
		
	}
	
	public JOrderInfoGUI(IGame game){
		super();
		this.info.setWrapStyleWord(true);
		this.setLayout(new GridLayout(2,1));
		this.info.setLineWrap(true);
		this.game = game;
		this.hide.addActionListener(new HideActionListener());
		setContext();
	}
	
	private void setInfo() throws RemoteException, GameNotStartedException{
		if(this.game.getCurrentGameState() == IGame.gameStates.RUNNING){
                this.info.setText(this.game.getCurrentRound().getCurrentPlayer().getOrder().toStringRemote());
		}
	}
	private void setContext(){
		this.add(this.info);
		this.add(this.hide);
	}
	
	public void update() throws RemoteException, GameNotStartedException{
		setContext();
	}

}
