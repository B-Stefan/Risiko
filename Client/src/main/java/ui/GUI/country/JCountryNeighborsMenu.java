package ui.GUI.country;

import interfaces.data.ICountry;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

/**
 * Created by Stefan on 09.06.14.
 */
public abstract class JCountryNeighborsMenu extends JMenu {

    private final ICountry country;
    private JCountryNeighborsMenuItem selectedNeighborsMenuItem = null;
    private class NeighborsActionListener implements ActionListener {
        /**
         * Invoked when an action occurs.
         *
         * @param e
         */
        @Override
        public void actionPerformed(final ActionEvent e) {
            if(e.getSource() instanceof JCountryNeighborsMenuItem) {
                JCountryNeighborsMenuItem menuItem = (JCountryNeighborsMenuItem) e.getSource();
                JCountryNeighborsMenu.this.setSelectedNeighborsMenuItem(menuItem);
                JCountryNeighborsMenu.this.fireActionPerformed(new ActionEvent(menuItem,20000,"onCountryClick"));
            }
        }
    }

    public JCountryNeighborsMenuItem getSelectedNeighborsMenuItem(){
        return this.selectedNeighborsMenuItem;
    }
    public void setSelectedNeighborsMenuItem(JCountryNeighborsMenuItem item){
        this.selectedNeighborsMenuItem = item;
    }
    public JCountryNeighborsMenu(final String title, final ICountry country) throws RemoteException{
        super(title);
        this.country = country;
        for(ICountry neighbor: this.country.getNeighbors()){
            JMenuItem item = new JCountryNeighborsMenuItem(neighbor);
            item.addActionListener(new NeighborsActionListener());
            this.add(item);
        }
    }

    public ICountry getCountry(){
        return this.country;
    }

}
