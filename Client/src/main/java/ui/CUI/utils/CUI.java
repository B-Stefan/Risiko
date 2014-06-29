package main.java.ui.CUI.utils;

import main.java.ui.CUI.exceptions.InvalidCommandListernArgumentException;

import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.*;

/**
 * @author Stefan
 *         Diese Klasse ist für die Verwaltung der einzelnen Console Line Ebenen zuständig. Eine Instanz dieser Klasse stellt eine Ebene da.
 *         Beispiel: "IGame > Map >" In diesem Beispiel wäre IGame eine Instanze einer Klasse, die CUI implementiert und Map anlaog zu IGame.
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
     *@see main.java.ui.CUI.utils.CommandListener
     */
    public class ChangeDirListener extends CommandListener {
        public ChangeDirListener() {
            super("cd", "Wechselt die Ebene");
            this.addArgument(new CommandListenerArgument("parent"));
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            String gotParentArg = "";
            try {
                gotParentArg = this.getArgument("parent").toStr();
            }catch (InvalidCommandListernArgumentException e){
                //Ignor exception weil auch ein leer String gültig ist
            }
            CUI cui = CUI.this;

            if (gotParentArg.equals(new String(".."))) {
                cui.goIntoParentContext();
                return;

            }
            else {
               cui.goIntoChildContext(this.getArguments());
            }



        }
    }
    /**
     * Bietet die Funktion zum anzeigend er Hilfe
     *@see main.java.ui.CUI.utils.CommandListener
     */
    public class HelpListener extends CommandListener {
        public HelpListener() {
            super("help", "Gibt hilfe zu den Befehlen aus");
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            IO.printHeadline("Hilfe");
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
    protected abstract void goIntoChildContext(LinkedHashMap<String,CommandListenerArgument> args);

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
     * Dient zum wechseln in den untergeordneten Kontext und führt den wechsel auch tatsächlich durch.
     */
    protected  void goIntoChildContext(){
       this.goIntoChildContext(new LinkedHashMap<String, CommandListenerArgument>());
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

                this.fireCommandEvent(rawCommand);

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
    protected void fireCommandEvent(final String command, final String[] args)  {

        if (!commands.containsKey(command)) {
            IO.println("Es konnte keine Aktion mit dem Namen '" + command + "' gefunden werden verwende 'help' um eine Auswahl der Möglichkeiten zu erhalten.");
            return;
        }

        CommandListener currentCommandListener = commands.get(command);

        //Set Arguments
        Iterator requiredArgumentsInterator = currentCommandListener.getArguments().entrySet().iterator();
        for (int i = 0;  requiredArgumentsInterator.hasNext(); i++){
            java.util.Map.Entry<String, CommandListenerArgument> requiredCommandEntry = (java.util.Map.Entry<String,CommandListenerArgument>) requiredArgumentsInterator.next();
            CommandListenerArgument requiredCommand  = requiredCommandEntry.getValue();
            //Wenn keine Argumete mehr vorhanden sind
            if(args.length == i ){
                break;
            }
            //Wenn argumente für den CommandListenerArgument vorhanden sind value setzten
            else{
                requiredCommand.setValue(args[i]);
            }
        }

        fireCommandEvent(currentCommandListener);

    }

    /**
     * Versucht eine Eingabe eines Nutzers zu formatieren.
     * @param rawInput - Unformtierter Input vom User
     * @throws Exception
     */
    protected void fireCommandEvent(String rawInput)  {
        this.fireCommandEvent(this.normalizeCommand(rawInput), this.normalizeArguments(rawInput));
    }

    /**
     * Versucht eine Eingabe eines Nutzers zu formatieren.
     * @param listener - Unformtierter Input vom User
     * @throws Exception
     */
    protected void fireCommandEvent(CommandListener listener)  {
        listener.actionPerformed(new ActionEvent(this, new Integer(42), listener.getCommand()));
    }

    /**
     * Führt einen Befehlt programmgesteuert aus.
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

    /**
     * Gibt den Aktuellen status der CUI zurück
     * @return
     */
    public states getCurrentState() {
        return currentState;
    }

    /**
     * Setzt den status der CUI
     * @param currentState Status der gesetzt werden soll
     */
    public void setCurrentState(states currentState) {
        this.currentState = currentState;
    }

    /**
     * Gib die untergeordnete CUI zurück
     * @return Untergeordnete CUI,  null wenn nicht vorhanden
     */
    protected CUI getChild() {
        return child;
    }

    /**
     * Setzt die untergeornete CUI auf den param child
     * @param child Neue untergeordnete CUI
     */
    protected void setChild(CUI child) {

        this.child = child;
    }
}
