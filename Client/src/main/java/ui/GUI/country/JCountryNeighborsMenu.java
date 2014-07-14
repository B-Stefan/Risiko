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
