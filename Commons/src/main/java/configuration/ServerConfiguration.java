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

package configuration;

/**
 * Konfigurationsklasse für den Server
 */
public class ServerConfiguration {


    /**
     * Der Default Port für den Server
     */
    public final static int DEFAULT_PORT = 6789;

    /**
     * Der Default Host für den Server
     *
     */
    public final static String DEFAULT_HOST = "localhost";


    /**
     * Name des Service für den Risiko Server
     */
    public final static String DEFAULT_GAME_SERVICE_NAME = "GameManagerService";

    /**
     * Default Server Konfiguration
     * port
     * host
     * serviceName
     */
    public final static ServerConfiguration DEFAULT = new ServerConfiguration(DEFAULT_PORT,DEFAULT_HOST,DEFAULT_GAME_SERVICE_NAME);


    /**
     * Erstellt aus einem String array String eine Server Konfiguration
     * @param args Stringarray
     *             [0] - HOST
     *             [1] - PORT
     *             [2] - GAME_SERVICE_NAME
     * @return
     * @throws IllegalArgumentException
     * @throws ClassCastException
     */
    public static ServerConfiguration fromArgs(String[] args) throws IllegalArgumentException, ClassCastException{
        if(args.length < 3){
            throw new IllegalArgumentException("Es müssen 3 argumente übergeben werden. Es waren: " + args.toString());
        }
        return new ServerConfiguration(Integer.parseInt(args[0]),args[1],args[2]);

    }

    /**
     * Die Portnummer auf der der Server läuft
     */
    public final int     PORT;

    /**
     * Der Host unter der dem der Server erreichbar ist
     */
    public final String  SERVER_HOST;

    /**
     * Der ServiceName für den GameManager
     */
    public final String  SERVICE_NAME;

    /**
     *
     * @param PORT Port für den Server
     * @param SERVER_HOST Host für den Server bsp.: localhost
     * @param SERVICE_NAME Name des Service für den GameManager
     */
    public ServerConfiguration(int PORT, String SERVER_HOST, String SERVICE_NAME){
        this.PORT = PORT;
        this.SERVER_HOST = SERVER_HOST;
        this.SERVICE_NAME = SERVICE_NAME;
    }
}
