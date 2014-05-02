package main.java.gui.CUI.core;

import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by Stefan on 30.04.14.
 */
public abstract class CommandListener implements ActionListener {
    private String command;
    private String helpText = "No HelpText";
    /**
     * Enthält alle möglichkeiten, die er User ausführen kann
     */
    private LinkedHashMap<String, CommandListenerArgument> arguments = new LinkedHashMap<String, CommandListenerArgument>();

    public CommandListener(String command){
        super();
        this.command = command;
    }
    public CommandListener(String command, String helpText){
        this(command);
        this.helpText = helpText;
    }
    protected void addArgument(CommandListenerArgument a){
        if(!this.arguments.containsKey(a.getName())){
            this.arguments.put(a.getName(), a);
        }
    }
    protected CommandListenerArgument getArgument(String name){
        return this.arguments.get(name);
    }
    public String getCommand(){
        return command;
    }
    public LinkedHashMap<String, CommandListenerArgument> getArguments(){
        return this.arguments;
    }


    public String getHelpText() {
        return helpText;
    }

    public void setHelpText(String helpText) {
        this.helpText = helpText;
    }

}
