package main;
import configuration.ServerConstants;
import interfaces.IGameManager;
import main.java.ui.CUI.utils.IO;
import main.java.ui.GUI.JGameManagerGUI;
import main.java.ui.GUI.utils.BackgroundImagePanel;
import main.java.ui.GUI.utils.JExceptionDialog;

import javax.swing.*;
import java.lang.*;
import java.rmi.RemoteException;

/**
 * Hauptklasse, die zum Starten des Spiels verwendet wird.
 *
 */
public class Client {

    /**
     * Client Anweiseung
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

        try{
            IGameManager manager = ServerEngine.getGameMangerService();
            new JGameManagerGUI(manager);
        }catch (RemoteException e){

            JFrame errorFrame = new JFrame();
            errorFrame.setVisible(true);
            errorFrame.add(new BackgroundImagePanel("/resources/noConnectionBg.jpg"));
            errorFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            new JExceptionDialog(errorFrame,"Beim Verbindungsaufbau zum Server" + ServerConstants.DEFAULT_SERVER + ":" + ServerConstants.DEFAULT_PORT + " ist ein Fehler aufgetreten");
        }


    }
}
