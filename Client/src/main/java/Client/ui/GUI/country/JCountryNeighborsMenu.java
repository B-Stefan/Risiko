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

package Client.ui.GUI.country;

import commons.interfaces.data.ICountry;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

/**
 *  Diese Klasse dient zur Anzeige von Nachbarn eines Landes in einem JMenue
 *  Dieser wird für das Popup-Menü bei einem Klick aufs Land gebraucht.
 *  Hierbei wird die Klasse für den Move und den Fight benötigt
 *  @see Client.ui.GUI.country.JCountryMoveMenu
 *  @see Client.ui.GUI.country.JCountryFightMenu
 */
public abstract class JCountryNeighborsMenu extends JMenu {

    /**
     * Land von dem die Nachbarn angezigt werden sollen
     */
    private final ICountry country;
    /**
     * Wird gesetzt, wenn auf einen Eintrag geklickt wird
     */
    private JCountryNeighborsMenuItem selectedNeighborsMenuItem = null;

    /**
     * Wird ausgelöst, wenn auf einen Eintrag geklcikt wird
     */
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

    /**
     * Dient zur Anzeige der Nachbarn eines Landes
     * @param title Angezeigter titel für den Menüeintrag
     * @param country Land von dem die Nachbarn angezeigt werden sollen
     * @throws RemoteException
     */
    public JCountryNeighborsMenu(final String title, final ICountry country) throws RemoteException{
        super(title);
        this.country = country;
        for(ICountry neighbor: this.country.getNeighbors()){
            JMenuItem item = new JCountryNeighborsMenuItem(neighbor);
            item.addActionListener(new NeighborsActionListener());
            this.add(item);
        }
    }
    /**
     * Gibt das Ausgewählte Land zurück
     * @return Land das ausgewählt wurde, wenn keins dann == null
     */
    public JCountryNeighborsMenuItem getSelectedNeighborsMenuItem(){
        return this.selectedNeighborsMenuItem;
    }

    /**
     * Setzt das ausgewählte Item
     * @param item Land das gesetzt werden soll
     */
    public void setSelectedNeighborsMenuItem(JCountryNeighborsMenuItem item){
        this.selectedNeighborsMenuItem = item;
    }


    /**
     * Gibt das Land zurück, von dem die Länder angezegit werden sollen
     * @return Land von dem die Nachbarn angezeigt werden sollen
     */
    public ICountry getCountry(){
        return this.country;
    }

}
