/*
 * RISIKO-JAVA - Game, Copyright 2014  Jennifer Theloy, Stefan Bieliauskas  -  All Rights Reserved.
 * Hochschule Bremen - University of Applied Sciences
 *
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 * Contact:
 *     Jennifer Theloy: jTheloy@stud.hs-bremen.de
 *     Stefan Bieliauskas: sBieliauskas@stud.hs-bremen.de
 *
 * Web:
 *     https://github.com/B-Stefan/Risiko
 *
 */



package Client.ui.GUI.gamePanels;

import javax.swing.*;

import commons.exceptions.GameNotStartedException;
import commons.interfaces.IGame;
import commons.interfaces.ITurn;
import commons.interfaces.data.ICountry;
import commons.interfaces.data.IMap;
import commons.interfaces.data.IPlayer;
import Client.logic.ClientEventProcessor;
import Client.ui.GUI.country.JCountryInfo;
import Client.ui.GUI.country.JCountryPopupMenu;
import Client.ui.GUI.utils.JExceptionDialog;
import Client.ui.GUI.utils.MapLoader;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.rmi.RemoteException;
import java.util.HashMap;

/**
 * Klasse Die zur Darstellung der Karte gedacht ist
 */
public class JMapGUI extends JComponent {

    /**
     * Kartenobjekt der GameEngeine
     * @see commons.interfaces.data.IMap
     */
    private final IMap map;

    /**
     * Spiel aus der GameEngine
     * @see commons.interfaces.IGame
     */
    private final IGame game;

    /**
     * Die Map Loader Klasse
     */
    private final MapLoader mapLoader;

    /**
     * Dient zum empfangen von Server events
     */
    private final ClientEventProcessor remoteEventProcessor;

    /**
     * Klasse, die beim Anklicken der Karte ausgelöst wird
     * @see java.awt.event.ActionListener
     */
    
	private final IPlayer clientPlayer;
	
    public class OnCountryClickActionListener extends MouseAdapter  {

        @Override
        public void mouseClicked(MouseEvent event) {
            int x = event.getX();
            int y = event.getY();
            ICountry country;

            try {
                country = JMapGUI.this.getCountry(x,y);
            }catch (RemoteException e){
                new JExceptionDialog(JMapGUI.this,e);
                return;
            }

            if (country == null){
                new JExceptionDialog(JMapGUI.this,"Es konnte an dieser Position kein Land gefunden werden");
            }
            else
            {
                ITurn currentTurn;
                try {
                   currentTurn =  game.getCurrentRound().getCurrentTurn();
                }catch (GameNotStartedException | RemoteException e){
                    new JExceptionDialog(JMapGUI.this,e);
                    return;
                }

                try {
                    JCountryPopupMenu countryGUI= new JCountryPopupMenu(country, currentTurn,remoteEventProcessor, clientPlayer);
                    countryGUI.show(event.getComponent(),x,y);
                }catch (RemoteException e){
                    new JExceptionDialog(JMapGUI.this,e);
                    return;
                }

            }


        }
    }


    /**
     * Klasse, die zur Darstellung einer Karte dient
     * @param game Spiel des der GameEngine
     *             @see commons.interfaces.IGame
     */
    public JMapGUI(IGame game,final ClientEventProcessor remoteEventProcessor, IPlayer cPlayer) throws RemoteException{
        super();
        this.game = game;
        this.clientPlayer = cPlayer;
        this.map = game.getMap();
        this.remoteEventProcessor = remoteEventProcessor;
        this.mapLoader = new MapLoader(this.map);
        inititize();
    }

    /**
     * Initialisiert die Karte
     *
     */
    private void inititize (){
        Dimension dim = new Dimension(600,400);
        this.addMouseListener(new OnCountryClickActionListener());

        this.setPreferredSize(dim);
        this.setMaximumSize(dim);
        this.setMinimumSize(dim);
        this.setSize(dim);
        this.setLayout(null);

    }

    /**
     * Beschreibt wie die Karte gezeichnet werden soll
     * @param g
     */
    @Override
    public void paint(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);


        //Land Infos

        //Immer wieder erneut löschen und hinzufügen ist am performantesten und auch am besten Wartbar
        this.removeAll();
        HashMap<Point, ICountry> postions;
        try {
            postions = this.mapLoader.getCountryInfoCoordinates(this.getWidth(), this.getHeight());
        }catch (RemoteException e){
            new JExceptionDialog(e);
            Window root = SwingUtilities.getWindowAncestor(this);
            if(root != null){
                root.dispose();
            }
            return;
        }
        for(java.util.Map.Entry entry : postions.entrySet()){
            ICountry country = (ICountry) entry.getValue();
            Point point = (Point) entry.getKey();
            JCountryInfo info = new JCountryInfo(country);
            info.setSize(new Dimension(50,50));
            info.setPreferredSize(new Dimension(50,50));
            info.setLocation(point.getLocation());
            this.add(info);
        }


        //Paint Karte
        g2.drawImage(this.mapLoader.getFrontImage(), 0, 0, this.getWidth(), this.getHeight(), null);
        super.paint(g);

    }

    /**
     * Gett für Country an einer bestimmten x,y Position
     * @param x Position auf der X-Achse
     * @param y Position auf der Y-Achse
     * @return Land das sich an der (x,y) Position befindet.
     */
    public ICountry getCountry(int x, int y) throws RemoteException{
        return this.mapLoader.getCountry(x,y, this.getWidth(),this.getHeight());
    }


}
