package main.java.ui.GUI.utils;

import main.java.logic.data.Country;
import main.java.logic.data.Map;
import main.java.ui.CUI.utils.IO;
import org.magicwerk.brownies.collections.GapList;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Created by Stefan on 11.06.14.
 */
public class MapLoader {

    private final Map map;
    private Image frontImage;
    private BufferedImage backgroundCountryBorderImg;
    private BufferedImage backgroundCountryInfoImg;

    public MapLoader(Map map){
       this.map = map;
       Dimension dim = new Dimension(400,400);
       this.frontImage = (new ImageIcon(getClass().getResource("/resources/Map_Vg.png"))).getImage();
       this.backgroundCountryBorderImg = new BufferedImage(dim.width, dim.height, BufferedImage.TYPE_INT_ARGB);
       this.backgroundCountryBorderImg.getGraphics().drawImage(new ImageIcon(getClass().getResource("/resources/Map_Bg.png")).getImage(), 0, 0, dim.width, dim.height, null);


       this.backgroundCountryInfoImg = new BufferedImage(dim.width, dim.height, BufferedImage.TYPE_INT_ARGB);
       this.backgroundCountryInfoImg.getGraphics().drawImage(new ImageIcon(getClass().getResource("/resources/Map_Bg_Info.png")).getImage(), 0, 0, dim.width, dim.height, null);
    }
    public HashMap<Point,Country> getCountryInfoCoordinates(int width, int height){
        BufferedImage img = this.backgroundCountryInfoImg;
        HashMap<Point,Country> positions = new HashMap<Point,Country>();
        List colorList = GapList.create();

        double xFactor = width / (double)img.getWidth()  ;
        double yFactor =  height / (double)img.getHeight();
        Color col = Color.BLACK;
        for (int y=0; y!=img.getHeight(); y=y+4){
            if (y < img.getHeight()){
                for(int x=0; x!= img.getWidth(); x=x+4){
                    if (x < img.getWidth()) {
                        Color newcol =  new Color(img.getRGB(x,y));
                        if(!newcol.equals(col)){
                            col = newcol;
                            if(!col.equals(Color.BLACK)){
                                Country country = map.getCountry(col);
                                if(country != null && !colorList.contains(col.getRGB()) ){
                                    int xCalc = (int) Math.round(x*xFactor);
                                    int yCalc = (int) Math.round(y*yFactor);
                                    positions.put(new Point(xCalc,yCalc),country);
                                    colorList.add(col.getRGB());
                                }
                            }
                        }
                    }
                }
            }
        }
        return positions;
    }

    public Country getCountry(int x, int y, int width, int height){
        //Nur resize wenn größte sich veröndert hat
        if (this.backgroundCountryBorderImg.getHeight() != height || this.backgroundCountryBorderImg.getWidth() != width){
            this.backgroundCountryBorderImg = this.getScaledImage(this.backgroundCountryBorderImg, width,height);
        }
        Color col = new Color(this.backgroundCountryBorderImg.getRGB(x,y));
        return this.map.getCountry(col);
    }
    public Image getFrontImage(){
        return this.frontImage;
    }
    public BufferedImage getBackgroundCountryBorderImg(int width, int height){
        this.backgroundCountryBorderImg =  this.getScaledImage(this.backgroundCountryBorderImg,width,height);
        return this.backgroundCountryBorderImg;
    }
    public BufferedImage getBackgroundCountryInfoImg(int width, int height){
        this.backgroundCountryInfoImg =  this.getScaledImage(this.backgroundCountryInfoImg,width,height);
        return this.backgroundCountryInfoImg;
    }
    private BufferedImage getScaledImage(BufferedImage image, int width, int height){
        int type=0;
        type = image.getType() == 0? BufferedImage.TYPE_INT_ARGB : image.getType();
        BufferedImage resizedImage = new BufferedImage(width, height,type);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(image, 0, 0, width, height, null);
        g.dispose();
        return resizedImage;
    }
}

