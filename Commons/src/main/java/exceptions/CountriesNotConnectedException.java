package exceptions;

import interfaces.data.ICountry;

public class CountriesNotConnectedException extends Exception {

    public CountriesNotConnectedException(ICountry from, ICountry to ){
        super("Die Länder " + from +" und " + to + "sind nicht verbunden ");
    }
}
