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

package commons.interfaces.data;

import commons.interfaces.IToStringRemote;

import java.awt.*;
import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.*;
import java.util.List;

/**
 * Created by Stefan on 29.06.14.
 */
public interface IMap extends Remote, Serializable, IToStringRemote {
    /**
     * Gibt die Liste aller Countries zurück
     * @return Liste aller Counties
     */
    public List<? extends ICountry> getCountries() throws RemoteException;

    /**
     * Vergleicht die Namen der L�nder mit �bergebenem String
     * @param n String (name des zu suchenden Landes)
     * @return das zu suchende Land
     */
    public ICountry getCountry(String n) throws RemoteException;

    /**
     * Vergleicht die Farbe mit der übergebenen Farbe
     * @param col Color (Farbe des zu suchenden Landes)
     * @return das zu suchende Land
     */
    public ICountry getCountry(Color col)throws RemoteException;

    /**
     *
     * @return Alle Kontinente dieser Karte
     */
    public List<? extends IContinent> getContinents()throws RemoteException;

    /**
     * Getter für die ID
     */
    public UUID getId()throws RemoteException;

}
