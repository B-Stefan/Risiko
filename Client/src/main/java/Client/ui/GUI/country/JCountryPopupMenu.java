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

import commons.interfaces.ITurn;
import commons.interfaces.data.ICountry;
import commons.interfaces.data.IPlayer;
import Client.logic.ClientEventProcessor;

import javax.swing.*;

import java.awt.*;
import java.rmi.RemoteException;

/***
 * Wird aufgerufen, wenn man auf ein Land Klick
 * Allerdings nur wenn auf der Karte auch ein Land gefunden wurde.
 *
 */
public class JCountryPopupMenu extends JPopupMenu{

    /**
     * Spieler der die Aktion ausführen möchte
     */
	private final IPlayer clientPlayer;
    /**
     * Land für das Popup Menü
     */
    private final ICountry country;
    /**
     * Server-Objekt auf dem Aktionen ausgeührt werden
     */
    private final ITurn turn;
    /**
     * EventManager der die Server-Events verwaltet
     */
    private final ClientEventProcessor remoteEventProcessor;

    /**
     * Dient zur Anzeige eines Popup menues
     * @param country Land für das Menü
     * @param turn Server-Objekt für die Aktionen
     * @param remoteEventProcessor Client-Manager für RemoteEvents
     * @param cPlayer Spielder der die Aktionen ausführen möchte
     * @throws RemoteException
     */
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
