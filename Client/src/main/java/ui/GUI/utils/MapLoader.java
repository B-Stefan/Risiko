package main.java.ui.GUI.utils;

import logic.data.Country;
import logic.data.Map;
import org.magicwerk.brownies.collections.GapList; //Sehr schnelle Implementeirung für Array-Listen

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;

/**
 * Klasse, die zum laden einer Klarte gedacht ist
 */
public class MapLoader {

    /**
     * Map Object von der GameEngine
     * @see logic.data.Map
     */
    private final Map map;

    /**
     * Karte, die dem Benutzer angezeigt wird
     */
    private Image frontImage;
    /**
     * Karte die für die klickbaren Bereiche benötigt wird
     */
    private BufferedImage backgroundCountryBorderImg;
    /**
     * Karte, die für die Anzeigepunkte benötigt wird.
     */
    private BufferedImage backgroundCountryInfoImg;

    /**
     * Klasse zum laden der Map
     * @param map Map Objekt von der Game Logic
     */
    public MapLoader(Map map){
       this.map = map;
       Dimension dim = new Dimension(400,400); // Dimension für die Background Bilder ‚

       //Load Userinterface Bild
       this.frontImage = (new ImageIcon(getClass().getResource("/resources/Map_Vg.png"))).getImage();

       //Load Border Background
       this.backgroundCountryBorderImg = new BufferedImage(dim.width, dim.height, BufferedImage.TYPE_INT_ARGB);
       this.backgroundCountryBorderImg.getGraphics().drawImage(new ImageIcon(getClass().getResource("/resources/Map_Bg.png")).getImage(), 0, 0, dim.width, dim.height, null);


       //Load Point
       this.backgroundCountryInfoImg = new BufferedImage(dim.width, dim.height, BufferedImage.TYPE_INT_ARGB);
       this.backgroundCountryInfoImg.getGraphics().drawImage(new ImageIcon(getClass().getResource("/resources/Map_Bg_Info.png")).getImage(), 0, 0, dim.width, dim.height, null);
    }

    /**
     * Gibt die Punkte pro Land raus anhand des Hintergrundbildes, absolut zur width, height
     * @param width Höhe auf die die Punkte skaliert werden sollen
     * @param height Breite auf die die Punkte skaliert werden sollen
     * @return Pro Punkt ein Land
     */
    public HashMap<Point,Country> getCountryInfoCoordinates(int width, int height){
        BufferedImage img = this.backgroundCountryInfoImg;
        HashMap<Point,Country> positions = new HashMap<Point,Country>();
        List colorList = GapList.create(); //Sehr schnelle Implementeirung für eine Array-List

        //Faktoren für Berechung der globalen x,y Koordinaten ermitteln
        double xFactor = width / (double)img.getWidth()  ;
        double yFactor =  height / (double)img.getHeight();


        Color col = Color.BLACK;
        //X,Y Punkte des Hintergrundbildes durchgehen
        for (int y=0; y!=img.getHeight(); y=y+1){

            //Fehlerfall
            if (y < img.getHeight()){

                //X-Koordinaten
                for(int x=0; x!= img.getWidth(); x=x+1){

                    //Fehlerfall
                    if (x < img.getWidth()) {
                        //Neue Farbe, dadurch wird geschaut, ob ich mich gerade "in" einem Punkte befinde => Performancegewinn
                        Color newcol =  new Color(img.getRGB(x,y));
                        //Vergleich, ob ich mich noch im gleichen Punkt wie ein Pixel zurvor befinde
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

    /**
     * Gibt das Land für bestimmte x,y Koordinaten aus
     * @param x x Wert an dem Gesucht werden soll
     * @param y y Wert an dem gesucht werden soll
     * @param width Breite, auf dem der x Wert bassiert
     * @param height Höhe auf dem der Y Wert bassiert
     * @return Land das sich an diesen Koordinaten in Relation zur width und height befindet
     */
    public Country getCountry(int x, int y, int width, int height){

        //Faktoren für Berechung der globalen x,y Koordinaten ermitteln
        double xFactor = width / (double)this.backgroundCountryBorderImg.getWidth()  ;
        double yFactor =  height / (double)this.backgroundCountryBorderImg.getHeight();

        //Berechnen der lokalen Koordinaten
        int xCalc = (int) Math.round(x/xFactor);
        int yCalc = (int) Math.round(y/yFactor);

        // Suchen der Farben auf basis der lokalen koordinaten
        Color col = new Color(this.backgroundCountryBorderImg.getRGB(xCalc,yCalc));
        return this.map.getCountry(col);
    }

    /**
     * Gibt das Bild zur Anzeige des Benutzer zurück
     * @return Karte zur Anzeige
     */
    public Image getFrontImage(){
        return this.frontImage;
    }


}

