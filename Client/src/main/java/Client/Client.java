
package Client;
import commons.configuration.ServerConfiguration;
import commons.interfaces.IGameManager;
import Client.ui.GUI.utils.JExceptionDialog;
import Client.ui.GUI.JGameManagerGUI;

import javax.swing.*;
import java.lang.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * Hauptklasse, die zum Starten des Spiels verwendet wird.
 *
 */
public class Client {


    /*
    * Erstellt einen client
    * @param args ServerConfiguration
    *             args[0] - HOST - Der Host, der verwendet werden soll
    *             args[1] - PORT - Der Port der verwendet werden soll
    *             args[2] - GAME_MANAGER_SERVICE_NAME - Servicename für den GameManager
    *
    */
    public Client(String[] args){
        /**
         * Konfiguration über Argument erzeugt, ansonstne default
         */
        ServerConfiguration serverConfiguration;

        if(args.length > 0){
            try {
                serverConfiguration = ServerConfiguration.fromArgs(args);
            }catch (IllegalArgumentException | ClassCastException e){
                new JExceptionDialog("Ihre Argumente sind nicht gültig. Bitte passen Sie diese an. Argumente: " + args);
                return;
            }
        }else {
            serverConfiguration = ServerConfiguration.DEFAULT;
        }
        this.openClient(serverConfiguration);

    }

    /**
     * Öffnet einen client auf Basis einer Serverkonfiguration
     * @param configuration - Serverkonfiguration
     * @see commons.configuration.ServerConfiguration
     */
    public Client(ServerConfiguration configuration){
        this.openClient(configuration);
    }

    /**
     * Verbindet den client und öffnet die GameManagerGUI
     * @param serverConfiguration Serverkonfiguration
     */
    private void openClient(ServerConfiguration serverConfiguration){

        /**
         * Mac style
         */
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

        /**
         * Servercommunikation
         */
        ServerEngine serverEngine = new ServerEngine(serverConfiguration);              //ServerEngine erstellen

        try{
            IGameManager manager = serverEngine.getGameMangerService();                 //GameManager vom Server holen
            new JGameManagerGUI(manager);
        }catch (RemoteException | NotBoundException e){
            new JExceptionDialog("Beim Verbindungsaufbau zum Server: rmi://" + serverConfiguration.SERVER_HOST + ":" + serverConfiguration.PORT + " ist ein Fehler aufgetreten");
        }


    }

    /*
    * @param args ServerConfiguration
    *             args[0] - HOST - Der Host, der verwendet werden soll
    *             args[1] - PORT - Der Port der verwendet werden soll
    *             args[2] - GAME_MANAGER_SERVICE_NAME - Servicename für den GameManager
    *
    */
    public static void main(String[] args) {
        //Erstellen des Clients
        new Client(args);
    }
}
