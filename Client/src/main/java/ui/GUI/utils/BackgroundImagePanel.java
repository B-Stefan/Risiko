package main.java.ui.GUI.utils;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Stefan on 11.06.14.
 */
public class BackgroundImagePanel extends JPanel {

    private final Image bg;

    public BackgroundImagePanel(String url){
        this.bg = new ImageIcon(getClass().getResource(url)).getImage();
    }

    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
    }
}