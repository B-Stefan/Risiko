package main.java.ui.GUI.utils;

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