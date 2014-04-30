package main.java.gui.CUI.core;

import main.java.logic.Map;

import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.*;

/**
 * @author Stefan
 *         Diese Klasse ist für die Verwaltung der einzelnen Console Line Ebenen zuständig. Eine Instanz dieser Klasse stellt eine Ebene da.
 *         Beispiel: "Game > Map >" In diesem Beispiel wäre Game eine Instanze einer Klasse, die CUI implementiert und Map anlaog zu Game.
 */
public abstract class CUI {

    /*
    * Steuert den Status des horchen auf der Konsole
     */
    public static enum states {
        SILENT,
        LISTEN
    }

    /**
     * Bildet den aktuellen Status der CL ab
     */
    private states currentState = states.SILENT;
    /**
     * Die Übergeordnete instanc der command line
     */
    private CUI parent;

    /**
     * Die untergeordnete Instanz
     *
     */
    private CUI child;

    /**
     * Der aktuelle Kontext in dem sich die CUI bewegt
     */

    private Object context;

    /**
     * Enthält alle möglichkeiten, die er User ausführen kann
     */
    private HashMap<String, CommandListener> commands = new HashMap<String, CommandListener>();


    /**
     * Bietet die Funktion zum wechseln der Ebene
     *@see main.java.gui.CUI.core.CommandListener
     */
    public class ChangeDirListener extends CommandListener {
        public ChangeDirListener() {
            super("cd");
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            String[] args = this.getArguments();
            CUI cui = CUI.this;
            if (args[0] != null) {
                if (args[0].equals(new String(".."))) {

                    cui.goIntoParentContext();
                    return;

                }
            }
            if(args[0] == null ){
                cui.goIntoChildContext();
            }
            else{
                cui.goIntoChildContext(args);
            }



        }
    }
    /**
     * Bietet die Funktion zum anzeigend er Hilfe
     *@see main.java.gui.CUI.core.CommandListener
     */
    public class HelpListener extends CommandListener {
        public HelpListener() {
            super("help");
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            IO.println("Nachfolgend die Hilfe für " + CUI.this.context.toString());
            for (java.util.Map.Entry<String, CommandListener>  c : CUI.this.commands.entrySet()){
                IO.println(" - " + c.getKey() + ": " + c.getValue().getHelpText());
            }

        }
    }
    /**
     *
     * @param context - Der Kontext für den eine CUI erzeugt werden soll
     */
    protected CUI(Object context) {
        this.context = context;
        this.addCommandListener(new ChangeDirListener());
        this.addCommandListener(new HelpListener());

    }

    /**
     *
     * @param context  - Der Kontext für den eine CUI erzeugt werden soll
     * @param parent - übergeordnete CUI
     */
    public CUI(Object context, CUI parent) {
        this(context);
        this.parent = parent;
    }

    /**
     * Dient zum wechseln in den untergeordneten Kontext
     */
    protected abstract void goIntoChildContext();

    /**
     * Dient zum wechseln in den untergeordneten Kontext
     */
    protected abstract void goIntoChildContext(String[] args);

    /**
     * Dient zum wechseln in den untergeordneten Kontext und führt den wechsel auch tatsächlich durch.
     * @param context
     */
    protected  void goIntoChildContext(CUI context){
        this.setChild(context);
        this.setCurrentState(states.SILENT);
        this.getChild().listenConsole();
    }

    /**
     * Wechselt in den übergeordneten Kontext
     */
    protected void goIntoParentContext() {
        if (this.parent != null) {
            this.setCurrentState(states.SILENT);
            this.parent.listenConsole();
        }
    }

    /**
     * Horcht auf der Konsole auf neue Eingaben, solange der Stauts der CUI LISTEN lautet
     */
    public void listenConsole() {
        String rawCommand = "";
        this.setCurrentState(states.LISTEN);
        while (this.getCurrentState() == states.LISTEN) {
            IO.print(this.toString()); //Print Context
            rawCommand = IO.readString();
            try {
                this.parseCommand(rawCommand);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private String[] normalizeRawInput(final String rawInput) {
        return this.normalizeRawInput(rawInput, " ");
    }

    private String[] normalizeRawInput(String rawInput, final String split) {
        rawInput = rawInput.trim();
        String[] splitString = rawInput.split(split);

        //Wenn input nur show oder ähnliches
        if (splitString.length == 1 || splitString.length == 0) {
            String[] re = {rawInput};
            return re;
        }
        //Ansonsten gesmates Array zurückgeben
        else {
            return splitString;
        }
    }

    /**
     * Normalisiert ein Befehl, sodass nur der Befehl zurück gegeben wird
     * @param rawInput - Unfomratierter input
     * @return - Befehl
     */
    protected String normalizeCommand(final String rawInput) {
        return this.normalizeRawInput(rawInput)[0];
    }

    /**
     * Normalisiert die Argumente, sodass nur ein String Array mit den Argumenten zurück gegeben wird
     * @param rawInput - Unformatierter Input
     * @return
     */
    protected String[] normalizeArguments(final String rawInput) {
        String[] splits = this.normalizeRawInput(rawInput);
        if (splits.length == 1) {
            String[] re = new String[1];
            return re;
        } else {
            return Arrays.copyOfRange(splits, 1, splits.length);
        }
    }

    /**
     * Versucht einen Befehl und die Argumente auszuführen.
     * @param command - Der Befehl
     * @param args - Die Argumente als String Array
     * @throws Exception
     */
    protected void parseCommand(final String command, final String[] args) throws Exception {

        Class c = this.getClass();
        Class noparams[] = {String[].class};


        if (!commands.containsKey(command)) {
            IO.println("Es konnte keine Aktion mit dem Namen '" + command + "' gefunden werden ");
            return;
        }

        CommandListener currentCommand = commands.get(command);
        currentCommand.setArguments(args);
        currentCommand.actionPerformed(new ActionEvent(this, new Integer(42), command));

    }

    /**
     * Versucht eine Eingabe eines Nutzers zu formatieren.
     * @param rawInput - Unformtierter Input vom User
     * @throws Exception
     */
    protected void parseCommand(String rawInput) throws Exception {
        this.parseCommand(this.normalizeCommand(rawInput), this.normalizeArguments(rawInput));
    }

    /**
     * Für einen Befehls-Listener hinzu
     * @param c - Der neue Listener
     */
    protected void addCommandListener(CommandListener c) {
        if (!this.commands.containsKey(c.getCommand())) {
            this.commands.put(c.getCommand(), c);
        }
    }

    /**
     * Gibt rekrusive den Kontext aus
     * @return Kontext der CUI
     */
    public String toString() {
        String prefix = "";
        if (this.parent != null) {
            prefix += this.parent.toString();
        }
        return prefix + context.toString() + " > ";
    }

    public states getCurrentState() {
        return currentState;
    }

    public void setCurrentState(states currentState) {
        this.currentState = currentState;
    }

    protected CUI getChild() {
        return child;
    }

    protected void setChild(CUI child) {

        this.child = child;
    }
}
