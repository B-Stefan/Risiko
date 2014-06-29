package main.java.ui.GUI.country;

import logic.data.Country;

import javax.swing.*;

/**
 * Created by Stefan on 09.06.14.
 */
public class JCountryNeighborsMenuItem extends JMenuItem {


    private final Country country;
    public JCountryNeighborsMenuItem (final Country country){
        super(country.getName());
        this.country = country;
    }
    public Country getCountry() {
        return country;
    }



}
