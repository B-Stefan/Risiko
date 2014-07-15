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



package Client.ui.GUI.utils;

import commons.interfaces.data.ICountry;
import commons.interfaces.data.IMap;
import org.magicwerk.brownies.collections.GapList; //Sehr schnelle Implementeirung für Array-Listen

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;

/**
 * Klasse, die zum laden einer Klarte gedacht ist
 */
public class MapLoader {

    /**
     * Map Object von der GameEngine
     * @see commons.interfaces.data.IMap
     */
    private final IMap map;

    /**
     * Karte, die dem Benutzer angezeigt wird
     */
    private final Image frontImage;
    /**
     * Karte die für die klickbaren Bereiche benötigt wird
     */
    private final BufferedImage backgroundCountryBorderImg;
    /**
     * Karte, die für die Anzeigepunkte benötigt wird.
     */
    private final BufferedImage backgroundCountryInfoImg;

    /**
     * Klasse zum laden der Map
     * @param map Map Objekt von der IGame Logic
     */
    public MapLoader(IMap map){
       this.map = map;
       Dimension dim = new Dimension(400,400); // Dimension für die Background Bilder ‚

       //Load Userinterface Bild
       this.frontImage = (new ImageIcon(getClass().getResource("/Map_Vg.png"))).getImage();

       //Load Border Background
       this.backgroundCountryBorderImg = new BufferedImage(dim.width, dim.height, BufferedImage.TYPE_INT_ARGB);
       this.backgroundCountryBorderImg.getGraphics().drawImage(new ImageIcon(getClass().getResource("/Map_Bg.png")).getImage(), 0, 0, dim.width, dim.height, null);


       //Load Point
       this.backgroundCountryInfoImg = new BufferedImage(dim.width, dim.height, BufferedImage.TYPE_INT_ARGB);
       this.backgroundCountryInfoImg.getGraphics().drawImage(new ImageIcon(getClass().getResource("/Map_Bg_Info.png")).getImage(), 0, 0, dim.width, dim.height, null);
    }

    /**
     * Gibt die Punkte pro Land raus anhand des Hintergrundbildes, absolut zur width, height
     * @param width Höhe auf die die Punkte skaliert werden sollen
     * @param height Breite auf die die Punkte skaliert werden sollen
     * @return Pro Punkt ein Land
     */
    public HashMap<Point,ICountry> getCountryInfoCoordinates(int width, int height) throws RemoteException{
        BufferedImage img = this.backgroundCountryInfoImg;
        HashMap<Point,ICountry> positions = new HashMap<Point,ICountry>();
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

                                ICountry country = map.getCountry(col);
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
    public ICountry getCountry(int x, int y, int width, int height) throws RemoteException{

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

