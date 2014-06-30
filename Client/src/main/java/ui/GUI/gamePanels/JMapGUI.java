package ui.GUI.gamePanels;

import javax.swing.*;

import exceptions.GameNotStartedException;
import interfaces.IGame;
import interfaces.ITurn;
import interfaces.data.ICountry;
import interfaces.data.IMap;
import main.java.ntry.JCountryPopupMenu;
import main.java.ui.GUI.coutryInfo;
import main.java.ui.GUI.utils.MapLoad main.java.ui.GUI.utils.JExceptionDialog;

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
     * @see interfaces.data.IMap
     */
    private final IMap map;

    /**
     * Spiel aus der GameEngine
     * @see interfaces.IGame
     */
    private final IGame game;

    /**
     * Die Map Loader Klasse
     */
    private final MapLoader mapLoader;

    /**
     * Klasse, die beim Anklicken der Karte ausgelöst wird
     * @see java.awt.event.ActionListener
     */
    public class OnCountryClickActionListener extends MouseAdapter  {

        @Override
        public void mouseClicked(MouseEvent event) {
            int x = (int) event.getX();
            int y = (int) event.getY();
            ICountry country = JMapGUI.this.getCountry(x,y);
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
                JCountryPopupMenu countryGUI= new JCountryPopupMenu(country, currentTurn);
                countryGUI.show(event.getComponent(),x,y);

            }


        }
    }


    /**
     * Klasse, die zur Darstellung einer Karte dient
     * @param game Spiel des der GameEngine
     *             @see interfaces.IGame
     */
    public JMapGUI(IGame game) throws RemoteException{
        super();
        this.game = game;
        this.map = game.getMap();
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
        HashMap<Point,ICountry> postions = this.mapLoader.getCountryInfoCoordinates(this.getWidth(),this.getHeight());
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
    public ICountry getCountry(int x, int y){
        return this.mapLoader.getCountry(x,y, this.getWidth(),this.getHeight());
    }


}
