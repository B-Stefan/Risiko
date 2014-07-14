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

import interfaces.ITurn;
import interfaces.data.ICountry;
import interfaces.data.IPlayer;
import server.logic.ClientEventProcessor;

import javax.swing.*;

import java.awt.*;
import java.rmi.RemoteException;


public class JCountryPopupMenu extends JPopupMenu{

	private final IPlayer clientPlayer;
    private final ICountry country;
    private final ITurn turn;
    private final ClientEventProcessor remoteEventProcessor;

    public JCountryPopupMenu(final ICountry country, final ITurn turn,final ClientEventProcessor remoteEventProcessor, IPlayer cPlayer) throws RemoteException{
        super();
        this.country = country;
        this.turn = turn;
        this.clientPlayer = cPlayer;
        this.remoteEventProcessor = remoteEventProcessor;


        JMenuItem countryName = new JMenuItem(this.country.getName());
        countryName.setFont(new Font ("Monospaced", Font.BOLD | Font.ITALIC, 14));
        this.add(countryName);
        this.add(new Separator());
        this.add(new JMenuItem("Armies:" + this.country.getNumberOfArmys()));
        this.add(new JMenuItem("Owner: " + this.country.getOwner().toStringRemote()));
        this.add(new Separator());
        this.add(new JCountryPlaceMenuItem(country,turn, this.clientPlayer));
        this.add(new JCountryFightMenu(country,turn,remoteEventProcessor, this.clientPlayer));
        this.add(new JCountryMoveMenu(country,turn, this.clientPlayer));
    }
}
