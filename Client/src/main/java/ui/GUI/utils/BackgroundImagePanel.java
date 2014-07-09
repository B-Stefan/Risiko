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



package ui.GUI.utils;

import javax.swing.*;
import java.awt.*;

/**
 * Klasse zum anzeigen eines Hintergrundbildes
 */
public class BackgroundImagePanel extends JPanel {

    /**
     * Bild das angezeigt werden soll
     */
    private final Image bg;

    /**
     * Klasse zur anzeige eines Hintergrundbildes
     * @param url Relative url zum Project root
     */
    public BackgroundImagePanel(String url){
        this.bg = new ImageIcon(getClass().getResource(url)).getImage();
    }

    /**
     * Paint override
     * @param g Zeichenstift
     */
    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
        super.paintComponent(g);
    }
}