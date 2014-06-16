package main.java.ui.GUI.utils;

import main.java.logic.exceptions.UserCanceledException;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Stefan on 11.06.14.
 */
public class JModalDialog extends JDialog {

    public static int showAskIntegerModal (final Component com, final String title, String message, int min, final int max) throws UserCanceledException{

        Frame  frame = (Frame) SwingUtilities.getWindowAncestor(com);
        if(min > max){
            min = max;
        }
        boolean validInput = false;
        int number = 0;
        do {
            String result = JOptionPane.showInputDialog(frame,message,title);
            try {
                number = Integer.parseInt(result);
                if (number >= min && number <= max){
                    validInput = true;
                }
                else {
                     message = result + " ist keine gültige Anzahl, bitte geben Sie eine gültige Anzahl ein, die zwischen " + min + " und " + max + " liegt.";
                }
            }catch (NumberFormatException e){
                validInput = false;
                message = result + " ist keine gültige Zahl, Bitte geben Sie eine Zahl zwischen "+ min + " und " + max  + " ein.";
                //Benutzer Klick auf Abbrechen
                if(result == null) {
                    break;
                }
            }

        }while (!validInput);

        if(validInput == false){
            throw new UserCanceledException();
        }
        return number;

    }

    public static String showInputDialog(Frame frame,String message,String title ){
        return JOptionPane.showInputDialog(frame,message,title);
    }
    public JModalDialog (JPanel panel,String title, ModalityType type){
        super(SwingUtilities.getWindowAncestor(panel),title,type);
        Dimension dim = new Dimension(300,300);
        this.setMinimumSize(dim);
        this.setPreferredSize(dim);
    }
}
