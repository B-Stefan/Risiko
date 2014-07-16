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
import server.logic.data.Country;
import server.logic.data.Map;
import server.persistence.PersistenceManager;

import java.rmi.RemoteException;
import java.util.*;

/**
 * Data-Object Klasse, die zur Umwandlung eines Logic-Objects in ein Persistierbares Object.
 * Dies dient dazu unnötige Informationen von benötigten Informationen zu trennen.
 * (normalisierung, der Daten )
 *
 * z.B. Die Klasse Country enhält ein Attribut Owner, genauso wie der Player eine Liste der Länder besitzt.
 * Die Zuordnung von Player zu Country ist also doppelt und kann somit verlustfrei reduziert werden.
 *
 * @see server.persistence.objects.PersitenceObject
 */
@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
public class PersistenceMap extends PersitenceObject<Map> {

    public final List<String> players = new ArrayList<String>();
    private final UUID id;
    private final List<String> countries = new ArrayList<String>();
    public PersistenceMap(Map map, PersistenceManager manager) throws PersistenceEndpointIOException, RemoteException{
        super(map,manager);
        this.id = map.getId();
        for (Country country: map.getCountriesReal()){
            this.countries.add(country.getId().toString());
        }

    }

    @Override
    public UUID getID() {
        return this.id;
    }

    @Override
    public Map convertToSourceObject(PersistenceManager manager) throws PersistenceEndpointIOException{
        /*
         * Erstellen eines neuen Map Objects, da die Map nicht dynamisch sondern statisch ist reicht hier einfach das erzeugen.
         * Sollte sicht dies ändern müsste hier das laden der Countries angestoßen werden
         */
        Map newMap;
		try {
			newMap = new Map();
		} catch (RemoteException e) {
			throw new PersistenceEndpointIOException( e);
		}
        return newMap;
    }
}
