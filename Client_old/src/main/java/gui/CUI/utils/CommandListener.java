package main.java.gui.CUI.utils;

import java.awt.event.ActionListener;
import java.util.LinkedHashMap;

/**
 * Diese Klasse dient zur verarbeitung von Befehlen, die auf der Console eingegeben werden
 *
 */
public abstract class CommandListener implements ActionListener {
    /**
     * Befehl, bei dem der Listener ausgelöst werden sill
     */
    private String command;
    /**
     * Hilfe-Text der dem User angezeigt werden kann
     */
    private String helpText = "No HelpText";
    /**
     * Bildet die Liste erforderlicher bzw. auch optionaler argumente ab.
     * Dabei wird eine HasMap verwendet, da so sehr leicht auf die Einträge per Key zugegriffen werden kann.
     * Die LinkedHasMap wurde gewählt, da diese Implementierung der HashMap die Einträge beim Interieren in der Reihnfolge durchgeht in der Sie hinzugefügt wurden.
     * Es wird hier also eine OrderedHashMap verlangt.
     * @see java.util.LinkedHashMap
     */
    private LinkedHashMap<String, CommandListenerArgument> arguments = new LinkedHashMap<String, CommandListenerArgument>();

    /**
     * ERzeugt eine Instanz
     * @param command - Der Befehl bei dem der Listener ausgelöst werden soll
     */
    public CommandListener(String command){
        super();
        this.command = command;
    }

    /**
     * Erweiterter Constructor zum erstellen eines Listeners
     * @param command - Befehl, bei dem der Listener ausgelöst werden soll
     * @param helpText - Text der dem Benutzer beschreibt was dieser Listener für eine Funktion bereitstellt
     */
    public CommandListener(String command, String helpText){
        this(command);
        this.helpText = helpText;
    }

    /**
     * Fügt dem Listener ein Argument hinzu, sodass er diese Argument beim auslösen der Aktion abfragen kann.
     * @param a - Arguemnt das hinzugefügt werden soll
     */
    protected void addArgument(CommandListenerArgument a){
        if(!this.arguments.containsKey(a.getName())){
            this.arguments.put(a.getName(), a);
        }
    }

    /**
     * Gibt ein Argument anhand seines Namens zurück
     * @see main.java.gui.CUI.utils.CommandListenerArgument
     * @param name - Name, den das Argument innehält
     * @return Argument, das dem name entsprecht
     */
    protected CommandListenerArgument getArgument(String name){
        return this.arguments.get(name);
    }


    /**
     * Gibt den Befehl zurück
     * @return - Befehl bei dem der Listener ausgelöst wird.
     */
    public String getCommand(){
        return command;
    }

    /**
     * Gibt die LinkedHashMap der Argument wieder, die der Listener enhält
     * @return - Argumente des Listeners
     */
    public LinkedHashMap<String, CommandListenerArgument> getArguments(){
        return this.arguments;
    }


    /**
     * Gibt den Hilfetext für den Benutzer zurück
     * @return - Hilfetext des Benutzers
     */
    public String getHelpText() {
        return helpText;
    }

    /**
     * Setzt den Hilfetext für den Benutzer
     * @param helpText - Text für den Benutzer
     */
    public void setHelpText(String helpText) {
        this.helpText = helpText;
    }

}
