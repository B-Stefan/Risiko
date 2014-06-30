package ui.CUI.exceptions;

import ui.CUI.utils.CommandListenerArgument;

import java.io.IOException;

/**
 * Created by Stefan on 02.05.14.
 */
public class InvalidCommandListernArgumentException extends IOException {
    public InvalidCommandListernArgumentException (CommandListenerArgument c, String type){
        super("Bitte geben Sie für das Argument " + c.getName() + " einen gültigen Wert an. Gültig bedeutet " +  type);
    }
}
