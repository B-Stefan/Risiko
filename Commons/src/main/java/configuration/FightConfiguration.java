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
 * Klasse für Konfiguration des Fights
 */
public class FightConfiguration {

    /**
     * Legt fest wieviele Einheiten auf dem Land verbleiben sollen und somit nicht an einem Kampf beteiligt sein können
     */
    public static int NUMBER_OF_ARMIES_EXCLUDE_FROM_FIGHT = 1;
    /**
     * Maximale Anzahl der Armee, mit denen man verteidigen kann
     */
    public static int DEFENDER_MAX_NUMBER_OF_ARMIES_TO_DEFEND = 2;
    /**
     * Maximale Anzahl der Armeen mit denen man angreifen kann
     */
    public static int AGGRESSOR_MAX_NUMBER_OF_ARMIES_TO_ATTACK = 3;

    /**
     * Anzahl der Armeen die mindestens auf ein neu erobertes Land gezogen werden müssen
     */
    public static int NUMBER_OF_ARMIES_TO_OCCUPIED_COUNTRY = 1;
}
