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

package server.persistence.objects;

import commons.exceptions.PersistenceEndpointIOException;
import server.persistence.PersistenceManager;

import java.io.Serializable;
import java.util.UUID;
/**
 * Data-Object Klasse, die zur Umwandlung eines Logic-Objects in ein Persistierbares Object.
 * Dies dient dazu unnötige Informationen von benötigten Informationen zu trennen.
 * (normalisierung, der Daten )
 *
 * z.B. Die Klasse Country enhält ein Attribut Owner, genauso wie der Player eine Liste der Länder besitzt.
 * Die Zuordnung von Player zu Country ist also doppelt und kann somit verlustfrei reduziert werden.
 *
 * Diese Abstrakte Klasse dient zur festlegung der Schnittstelle. Abstrakte Klasse anstatt ein Interface deswegen weil
 * durch den Konstruktor sichergestellt werden sollte, dass die entsprechde Aktion durchgeführt wird,
 *
 * @param <T> Das Logik-Objekt, das abgespeichert werden soll
 */
public abstract class PersitenceObject<T> implements Serializable {

    PersitenceObject(T obj, PersistenceManager manager) throws PersistenceEndpointIOException{}
    public abstract UUID getID();
    public abstract T convertToSourceObject(PersistenceManager manager) throws PersistenceEndpointIOException;

}
