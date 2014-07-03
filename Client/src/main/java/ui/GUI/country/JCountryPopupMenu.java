package ui.GUI.country;

import interfaces.ITurn;
import interfaces.data.ICountry;
import interfaces.data.IPlayer;
import server.logic.ClientEventProcessor;

import javax.swing.*;

import java.awt.*;
import java.rmi.RemoteException;

/**
 * Created by Stefan on 09.06.14.
 */
public class JCountryPopupMenu extends JPopupMenu{

	private final IPlayer clientPlayer;
    private final ICountry country;
    private final ITurn turn;
    private final ClientEventProcessor remoteEventProcessor;

    public JCountryPopupMenu(final ICountry country, final ITurn turn,final ClientEventProcessor remoteEventProcessor, IPlayer cPlayer) throws RemoteException{
        super();
        this.country = country;
        this.turn = turn;
        this.clientPlayer = cPlayer;
        this.remoteEventProcessor = remoteEventProcessor;


        JMenuItem countryName = new JMenuItem(this.country.getName());
        countryName.setFont(new Font ("Monospaced", Font.BOLD | Font.ITALIC, 14));
        this.add(countryName);
        this.add(new Separator());
        this.add(new JMenuItem("Armies:" + this.country.getNumberOfArmys()));
        this.add(new JMenuItem("Owner: " + this.country.getOwner().toStringRemote()));
        this.add(new Separator());
        this.add(new JCountryPlaceMenuItem(country,turn, this.clientPlayer));
        this.add(new JCountryFightMenu(country,turn,remoteEventProcessor, this.clientPlayer));
        this.add(new JCountryMoveMenu(country,turn, this.clientPlayer));
    }
}
