package main.java.ui.GUI.utils;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Stefan on 11.06.14.
 */
public class JExceptionDialog {

    private final Window window;
    private final Exception e;
    public JExceptionDialog(Component com,Exception e ){
        this.window = SwingUtilities.getWindowAncestor(com);
        this.e = e;
        this.openModal();
    }
    public JExceptionDialog(Component com,String message){
        this.window = SwingUtilities.getWindowAncestor(com);
        this.e = new Exception(message);
        this.openModal();
    }

    private void openModal (){
        JOptionPane.showMessageDialog(window,
                e.getMessage(),
                "An Error occurred",
                JOptionPane.ERROR_MESSAGE);
    }

}
