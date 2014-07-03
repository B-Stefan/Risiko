package ui.GUI.country;

import interfaces.data.ICountry;

import javax.swing.*;
import java.rmi.RemoteException;

/**
 * Created by Stefan on 09.06.14.
 */
public class JCountryNeighborsMenuItem extends JMenuItem {


    private final ICountry country;
    public JCountryNeighborsMenuItem (final ICountry country) throws RemoteException{
        super(country.getName() + " (" + country.getShortName() + ")");
        this.country = country;
    }
    public ICountry getCountry() {
        return country;
    }
}
