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

import server.persistence.PersistenceManager;
import server.persistence.objects.PersitenceObject;

import java.io.*;
import java.util.HashMap;
import java.util.UUID;

/**
 * Diese Klasse stellt die Möglichkeit zur Speicherung durch einfache Serialisierung zur Verfügung.
 * @param <T>
 */
public class SerializableFileEndpoint<T> extends AbstractFileEndpoint<T> {



    public SerializableFileEndpoint(Class<T> sourceClass, Class<? extends PersitenceObject<T>> dataClass, PersistenceManager manager) {
        super(sourceClass, dataClass, manager);
    }

    /**
     * Liest den File und packt alle Objekte des Files in die Liste chacedObjects
     */
    @Override
    protected void readFile() {
        {
            AbstractFileEndpoint.createDir();
            try {
                ObjectInputStream reader = new ObjectInputStream(new FileInputStream(AbstractFileEndpoint.convertFileNameToPath(this.fileName)));
                this.chachedObjects = (HashMap<UUID, PersitenceObject<T>>) reader.readObject();
            }catch (ClassNotFoundException | ClassCastException e){
                throw new RuntimeException(e);
            }
            catch (EOFException | InvalidClassException e){
                //File ist leer

            } catch (FileNotFoundException e){
                this.writeFile(); // File erstellen, wenn nicht vorhanden
            }
            catch (IOException e){
                //@Todo Besseres Exception Handling
                e.printStackTrace();
            }

        }
    }

    /**
     * Schreibt die Liste chachedObjetcts in die Datei
     */
    @Override
    protected void writeFile() {
        {
            try {
                FileOutputStream fos  = new FileOutputStream(AbstractFileEndpoint.convertFileNameToPath(this.fileName),false);
                ObjectOutputStream  writer = new ObjectOutputStream(fos);
                writer.writeObject(this.chachedObjects);
                writer.close();
            }
            catch (IOException e){
                e.printStackTrace();
            }

        }
    }
}
