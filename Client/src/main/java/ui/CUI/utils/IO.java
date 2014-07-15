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

package ui.CUI.utils; /**
 * macht das Einlesen von Einfachen Daten einfacher 
 * durch Verdecken des Exception-Handlings
*/


import java.io.*;

public class IO {

	public static final BufferedReader input
          = new BufferedReader(new InputStreamReader(System.in));
	public static String eingabe = "";

	// Einlesen eines char
	public static char readChar() {
		try {
			eingabe = input.readLine();
			return eingabe.charAt(0);
		}
		catch(Exception e) {
			return '\0';
		}
	}

	// Einlesen eines short
	public static short readShort() {
		try {
			eingabe = input.readLine();
			Integer string_to_short = new Integer(eingabe);
			return (short)string_to_short.intValue();
		}
		catch (Exception e) {
			return 0;
		}
	}	

	// Einlesen eines int
	public static int readInt() {
		try {
			eingabe = input.readLine();
			Integer string_to_int = new Integer(eingabe);
			return string_to_int.intValue();
		}
		catch (Exception e) {
		  return 0;
		}
	}

	// Einlesen eines long
	public static long readLong() {
		try {
			eingabe = input.readLine();
			Long string_to_long = new Long(eingabe);
			return string_to_long.longValue();
		}
		catch (Exception e) {
			return 0L;
		}
	}

	// Einlesen eines float
	public static float readFloat() {
		try {
			eingabe = input.readLine();
			Float string_to_float = new Float(eingabe);
			return string_to_float.floatValue();
		}
		catch (Exception e) {
			return 0.0F;
		}
	}

	// Einlesen eines double
	public static double readDouble() {
		try {
			eingabe = input.readLine();
			Double string_to_double = new Double(eingabe);
			return string_to_double.doubleValue();
		}
		catch (Exception e) {
			return 0.0;
		}
	}

	// Einlesen eines string
	public static String readString() {
		try {
			return input.readLine();
		}
		catch (Exception e) {
			return "";
		}
	}
	
	// Ausgabe
	public static void println(String s){
		System.out.println(s);
	}
    // Ausgabe
    public static void printHeadline(String s){
        System.out.println("====="+s+"====");
    }

    public static void print(String s){
		System.out.print(s);
	}
	
}

