package main.java.ui.GUI;

import javax.swing.*;

import main.java.ui.CUI.utils.IO;
import main.java.logic.data.Country;
import main.java.logic.data.Map;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import java.awt.RenderingHints;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Color;
import java.util.HashMap;

/**
 * Created by Stefan on 09.06.14.
 */
public class JMapGUI extends JPanel {

    private Image mapImage;
    private BufferedImage mapBgImg;
    private final Map map;
    private final HashMap<Color,Country> countrys = new HashMap<Color,Country>();


    public class OnCountryClickActionListener extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            int x = (int) e.getX();
            int y = (int) e.getY();
            Color col = new Color(getMapBgImg().getRGB(x, y));

            Country country = getCountry(col);
            if (country == null){
                IO.println("Country nicht gefunden");
                IO.println("R" + col.getRed() + " G" + col.getGreen() + " B" + col.getBlue());
            }
            else
            {
                IO.println(country.toString());
            }


        }
    }



    public JMapGUI(Map map){
        super();
        Dimension dim = new Dimension(643,180);
        this.map = map;
        for(Country country : map.getCountries()){
            this.countrys.put(country.getColor(), country);
        }
        inititize();
    }

    private void inititize (){
        Dimension dim = new Dimension(600,400);

        this.mapImage = (new ImageIcon(getClass().getResource("/resources/Map_Vg.png"))).getImage();
        this.mapBgImg = new BufferedImage(dim.width, dim.height, BufferedImage.TYPE_INT_RGB);
        this.mapBgImg.getGraphics().drawImage(new ImageIcon(getClass().getResource("/resources/Map_Bg.png")).getImage(), 0, 0, dim.width, dim.height, this);
        this.addMouseListener(new OnCountryClickActionListener());

        this.setPreferredSize(dim);
        this.setMaximumSize(dim);
        this.setMinimumSize(dim);
        this.setSize(dim);
    }
    public void paint(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);

        super.paint(g);

        //Paint Karte
        g.drawImage(mapImage, 0, 0, this.getWidth(), this.getHeight(), this);

    }

    public BufferedImage getMapBgImg(){
        return this.mapBgImg;
    }

    public Country getCountry(Color col){
        return countrys.get(col);
    }
}
