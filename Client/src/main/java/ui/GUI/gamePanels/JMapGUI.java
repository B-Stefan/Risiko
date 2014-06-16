package main.java.ui.GUI.gamePanels;

import javax.swing.*;

import main.java.logic.exceptions.GameNotStartedException;
import main.java.logic.data.Country;
import main.java.logic.data.Map;
import main.java.logic.Turn;
import main.java.logic.Game;
import main.java.ui.GUI.country.JCountryGUI;
import main.java.ui.GUI.utils.MapLoader;
import main.java.ui.GUI.utils.JExceptionDialog;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;

/**
 * Created by Stefan on 09.06.14.
 */
public class JMapGUI extends JComponent {

    private final Map map;
    private final Game game;
    private final MapLoader mapLoader;

    public class OnCountryClickActionListener extends MouseAdapter  {

        @Override
        public void mouseClicked(MouseEvent event) {
            int x = (int) event.getX();
            int y = (int) event.getY();
            Country country = JMapGUI.this.getCountry(x,y);
            if (country == null){
                new JExceptionDialog(JMapGUI.this,"Es konnte an dieser Position kein Land gefunden werden");
            }
            else
            {
                Turn currentTurn;
                try {
                   currentTurn =  game.getCurrentRound().getCurrentTurn();
                }catch (GameNotStartedException e){
                    new JExceptionDialog(JMapGUI.this,e);
                    return;
                }
                JCountryGUI countryGUI= new JCountryGUI(country, currentTurn);
                countryGUI.show(event.getComponent(),x,y);

            }


        }
    }


    public JMapGUI(Game game){
        super();
        this.game = game;
        this.map = game.getMap();
        this.mapLoader = new MapLoader(this.map);
        inititize();
    }

    private void inititize (){
        Dimension dim = new Dimension(600,400);
        this.addMouseListener(new OnCountryClickActionListener());

        this.setPreferredSize(dim);
        this.setMaximumSize(dim);
        this.setMinimumSize(dim);
        this.setSize(dim);
        this.setLayout(null);
    }
    public void paint(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);


        //Land Infos

        this.removeAll();
        HashMap<Point,Country> postions = this.mapLoader.getCountryInfoCoordinates(this.getWidth(),this.getHeight());
        for(java.util.Map.Entry entry : postions.entrySet()){
            Country country = (Country) entry.getValue();
            Point point = (Point) entry.getKey();
            JLabel info = new JLabel(country.getName());
            info.setSize(new Dimension(50,50));
            info.setPreferredSize(new Dimension(50,50));
            info.setLocation(point.getLocation());
            this.add(info);
        }


        //Paint Karte
        g2.drawImage(this.mapLoader.getFrontImage(), 0, 0, this.getWidth(), this.getHeight(), null);
        super.paint(g);


    }

    public Country getCountry(int x, int y){
        return this.mapLoader.getCountry(x,y, this.getWidth(),this.getHeight());
    }


}
