package main;
import main.java.GameManager;
import main.java.persistence.PersistenceManager;
import main.java.ui.GUI.JGameManagerGUI;

import javax.swing.*;
import java.lang.*;

/**
 * Hauptklasse, die zum Starten des Spiels verwendet wird.
 *
 */
public class main {

    /**
     * Main Anweiseung
     * @param args - Keine
     * @throws Exception
     */
    public static void main(String[] args) throws Exception{

        try {
            System.setProperty("apple.laf.useScreenMenuBar", "true");
            System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Risiko");
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch(ClassNotFoundException e) {
            System.out.println("ClassNotFoundException: " + e.getMessage());
        }
        catch(InstantiationException e) {
            System.out.println("InstantiationException: " + e.getMessage());
        }
        catch(IllegalAccessException e) {
            System.out.println("IllegalAccessException: " + e.getMessage());
        }
        catch(UnsupportedLookAndFeelException e) {
            System.out.println("UnsupportedLookAndFeelException: " + e.getMessage());
        }

        GameManager manager = new GameManager(new PersistenceManager());
        new JGameManagerGUI(manager);

    }
}
