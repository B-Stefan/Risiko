package ui.GUI.utils;

import javax.swing.*;
import java.awt.*;

/**
 * Klasse zum anzeigen einer Exception
 */
public class JExceptionDialog {

    /**
     * Root Fenster in dem die Exception angezeigt wird
     */
    private final Window window;

    /**
     * Exception die angezeigt werden soll
     */
    private final Exception e;

    /**
     * Klasse zur Anzeige einer Exception
     * @param com Komponente, parent für das Dialogfenster
     * @param e Exception die angezeigt werden soll
     */
    public JExceptionDialog(Component com,Exception e ){
        this.window = SwingUtilities.getWindowAncestor(com);
        this.e = e;
        this.openModal();
    }
    /**
     * Klasse zur Anzeige einer Exception
     * @param com Komponente, parent für das Dialogfenster
     * @param message Nachricht, die direkt angezeigt werden soll
     */
    public JExceptionDialog(Component com,String message){
        this.window = SwingUtilities.getWindowAncestor(com);
        this.e = new Exception(message);
        this.openModal();
    }

    /**
     * Klasse zum anzeigen einer Exception
     * @param e
     */
    public JExceptionDialog(Exception e){
        this(new JWindow(),e);
    }

    /**
     * Klasse zum anzeigen einer Exception
     * @param e
     */
    public JExceptionDialog(String e){
        this(new JWindow(),e);
    }

    /**
     * Öffnet den modalen Dialog
     */
    private void openModal (){
        JOptionPane.showMessageDialog(window,
                e.getMessage(),
                "An Error occurred",
                JOptionPane.ERROR_MESSAGE);
    }

}
