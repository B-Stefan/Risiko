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

package server.persistence.dataendpoints;

import commons.exceptions.PersistenceEndpointIOException;
import server.persistence.PersistenceManager;
import server.persistence.objects.PersitenceObject;

import javax.management.InstanceNotFoundException;

import java.util.List;
import java.util.UUID;

/**
 * @author Jennifer Theloy, Stefan Bieliauskas
 *
 * Diese Klasse dient zur Vereinheitlichung aller Endpunkte zum Speichern von Spielen, Spielern, Ländern usw.
 *
 * @see server.persistence.PersistenceManager
 * @param <T> Enhält die Logik-Klasse z.b. IGame oder Player, die gepeichert werden soll
 */
public abstract class PersistenceEndpoint<T> {

    /**
     * Datenklasse, diese Klasse muss serelisierbar sein
     */
    protected final Class dataClass;

    /**
     * Logik-Klasse, die abgespeichert werden soll. Im Prinzip nicht notwendig diese nochmal ab zu speichern, jedoch trägt dies zur Wartbarkeit und Sicherheit bei der Ausführung bei.
     */
    protected final Class<T> sourceClass;

    /**
     * Manager, der alle Endpoints verwaltet
     */
    protected final PersistenceManager manager;

    /**
     * Diese Klasse dient zur Vereinheitlichung der verschiedenen Endpunkte
     * @param sourceClass Klasse, die abgespeichert werden soll
     * @param dataClass Klasse, die zur Serialisierung dient
     * @param manager Manager, der alle anderen Endpunkte zur Speicherung verwaltet
     */
    public PersistenceEndpoint(final Class<T> sourceClass, final Class<? extends PersitenceObject<T>> dataClass, final PersistenceManager manager){
        this.dataClass = dataClass;
        this.sourceClass = sourceClass;
        this.manager = manager;
    }

    /**
     * Speichert eine neue Instanz
     * @param newObject
     * @return True, wenn Speicherung erfolgreich
     * @throws PersistenceEndpointIOException
     */
    public abstract boolean save(T newObject) throws PersistenceEndpointIOException;

    /**
     * Löscht die angegebene Instanz
     * @param removeObject Objekt, das zu löschen ist
     * @return true, wenn gelöscht
     * @throws PersistenceEndpointIOException
     * @throws InstanceNotFoundException
     */
    public abstract boolean remove(T removeObject) throws PersistenceEndpointIOException, InstanceNotFoundException;

    /**
     * Gibt die sourceClass einer bestimmten UUID zurück
     * @param id UUID des Objects
     * @return Objekt, null wenn nicht gefunden
     * @throws PersistenceEndpointIOException
     */
    public abstract T get(UUID id) throws PersistenceEndpointIOException;

    /**
     * Gibt die sourceClass einer bestimmten UUID zurück, dabei wird die UUID als String übergeben
     * @param id UUID als String
     * @return Objekt oder null, wenn nicht gefunden
     * @throws PersistenceEndpointIOException
     */
    public abstract T get(String id) throws PersistenceEndpointIOException;

    /**
     * Gibt alle Instancen des sourceClass zurück als Liste
     * @return Liste aller Instanzen der sourceCLass
     * @throws PersistenceEndpointIOException
     */
    public abstract List<T> getAll() throws PersistenceEndpointIOException;
}
