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

package interfaces.data;

import interfaces.IToStringRemote;
import interfaces.data.Orders.IOrder;
import interfaces.data.cards.*;

import java.awt.Color;
import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.*;

import exceptions.CountryNotInListException;

/**
 * Created by Stefan on 29.06.14.
 */
public interface IPlayer extends Remote, Serializable, IToStringRemote {

	/**
     * Getter für den Namen des Spielers
     * @return Name des Spielers
     */
    public String getName()throws RemoteException;
    /**
     * Sortiert das eigene Deck und gibt es aus
     * @return
     */
    public List<? extends ICard> getCards()throws RemoteException;

    /**
     * Getter für die Länderliste des Spielers
     * @return
     */
    public List<? extends ICountry> getCountries()throws RemoteException;
    
    /**
     * Setter für die Order des Spielers
     * @param order
     */
    public void setOrder(IOrder order)throws RemoteException;
    
    /**
     * Getter für die Order des Spielers
     * @return
     */
    public IOrder getOrder()throws RemoteException;
    /**
     * Gibt ein Land aus der Liste der Länder des Spielers aus
     * @param n
     * @return
     */
    public ICountry getCountry(String n)throws RemoteException;

    /**
     * Getter für die ID des Spielers
     * @return
     */
    public UUID getId ()throws RemoteException;
    /**
     * Getter für die Farbe des Spielers
     * @return
     */
    
    public Color getColor()throws RemoteException;
    /**
     * Setter für die Farbe des Spielers
     * @param col
     */
    public void setColor(Color col)throws RemoteException;

}
