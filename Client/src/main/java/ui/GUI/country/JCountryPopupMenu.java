package main.java.ui.GUI.country;

import main.java.logic.data.Country;
import main.java.logic.Turn;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Stefan on 09.06.14.
 */
public class JCountryPopupMenu extends JPopupMenu{


    private final Country country;
    private final Turn turn;

    public JCountryPopupMenu(Country country, Turn turn){
        super();
        this.country = country;
        this.turn = turn;

        JMenuItem countryName = new JMenuItem(this.country.getName());
        countryName.setFont(new Font ("Monospaced", Font.BOLD | Font.ITALIC, 14));
        this.add(countryName);
        this.add(new Separator());
        this.add(new JMenuItem("Armies:" + this.country.getNumberOfArmys()));
        this.add(new JMenuItem("Owner: " + this.country.getOwner().toString()));

        this.add(new Separator());

        this.add(new JCountryPlaceMenuItem(country,turn));
        this.add(new JCountryFightMenu(country,turn));
        this.add(new JCountryMoveMenu(country,turn));
    }
}
