package main.java.gui.CUI.core;

import java.awt.event.ActionListener;

/**
 * Created by Stefan on 30.04.14.
 */
public abstract class CommandListener implements ActionListener {
    private String command;
    private String[] arguments;
    private String helpText = "No HelpText";

    public CommandListener(String command){
        super();
        this.command = command;
    }
    public CommandListener(String command, String helpText){
        this(command);
        this.helpText = helpText;
    }
    public String getCommand(){
        return command;
    }
    public String[] getArguments(){
        return this.arguments;
    }
    public void setArguments(String[] args){
        this.arguments = args;
    }

    public String getHelpText() {
        return helpText;
    }

    public void setHelpText(String helpText) {
        this.helpText = helpText;
    }

}
