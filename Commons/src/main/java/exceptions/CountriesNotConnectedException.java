package exceptions;

import interfaces.data.ICountry;

/**
 * Created by Stefan on 01.05.14.
 */
public class CountriesNotConnectedException extends Exception {

    public CountriesNotConnectedException(ICountry from, ICountry to ){
        super("Die Länder " + from +" und " + to + "sind nicht verbunden ");
    }
}
