package exceptions;

import interfaces.data.IPlayer;
import interfaces.data.ICountry;


/**
 * Created by Stefan on 11.06.14.
 */
public class NotTheOwnerException extends Exception {
    public  NotTheOwnerException (IPlayer p, ICountry c ){
        super ("Der Spieler " + p.getName() + " ist nicht der Besitzter des Landes " + c.getName() + " und kann damit diese Aktion nicht durchführen");
    }
}
