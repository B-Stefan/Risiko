package main.java.ui.GUI.country;

import main.java.logic.data.Country;
import main.java.logic.Turn;
import main.java.logic.exceptions.TurnNotInCorrectStepException;
import main.java.ui.CUI.utils.IO;

import javax.swing.*;
import javax.swing.event.MenuDragMouseEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by Stefan on 09.06.14.
 */
public abstract class JCountryNeighborsMenu extends JMenu {

    private final Country country;
    private JCountryNeighborsMenuItem selectedNeighborsMenuItem = null;
    public class NeighborsActionListener implements ActionListener {
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
    public JCountryNeighborsMenu(final String title, final Country country){
        super(title);
        this.country = country;
        for(Country neighbor: this.country.getNeighbors()){
            JMenuItem item = new JCountryNeighborsMenuItem(neighbor);
            item.addActionListener(new NeighborsActionListener());
            this.add(item);
        }
    }

    public Country getCountry(){
        return this.country;
    }

}
