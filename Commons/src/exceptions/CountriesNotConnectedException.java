package exceptions;

import logic.data.Country;

/**
 * Created by Stefan on 01.05.14.
 */
public class CountriesNotConnectedException extends Exception {

    public CountriesNotConnectedException(Country from, Country to ){
        super("Die Länder " + from +" und " + to + "sind nicht verbunden ");
    }
}
