package main.java.ui.GUI.country;

import interfaces.data.ICountry;

import javax.swing.*;

/**
 * Created by Stefan on 09.06.14.
 */
public class JCountryNeighborsMenuItem extends JMenuItem {


    private final ICountry country;
    public JCountryNeighborsMenuItem (final ICountry country){
        super(country.getName());
        this.country = country;
    }
    public ICountry getCountry() {
        return country;
    }



}
