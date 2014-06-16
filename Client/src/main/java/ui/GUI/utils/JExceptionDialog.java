package main.java.ui.GUI.utils;

import javafx.scene.layout.Pane;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Stefan on 11.06.14.
 */
public class JExceptionDialog {

    private final Frame frame;
    private final Exception e;
    public JExceptionDialog(Component com,Exception e ){
        this.frame = (Frame) SwingUtilities.getWindowAncestor(com);
        this.e = e;
        this.openModal();
    }
    public JExceptionDialog(Component com,String message){
        this.frame = (Frame) SwingUtilities.getWindowAncestor(com);
        this.e = new Exception(message);
    }

    private void openModal (){
        JOptionPane.showMessageDialog(frame,
                e.getMessage(),
                "An Error occurred",
                JOptionPane.ERROR_MESSAGE);
    }

}
