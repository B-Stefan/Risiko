package main.java.logic.exceptions;

import main.java.logic.data.Country;
import main.java.logic.data.Player;

/**
 * Created by Stefan on 11.06.14.
 */
public class NotTheOwnerException extends Exception {
    public  NotTheOwnerException (Player p, Country c ){
        super ("Der Spieler " + p.getName() + " ist nicht der Besitzter des Landes " + c.getName() + " und kann damit diese Aktion nicht durchführen");
    }
}
