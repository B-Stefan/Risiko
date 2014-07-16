/*
 * RISIKO-JAVA - Game, Copyright 2014  Jennifer Theloy, Stefan Bieliauskas  -  All Rights Reserved.
 * Hochschule Bremen - University of Applied Sciences
 *
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 * Contact:
 *     Jennifer Theloy: jTheloy@stud.hs-bremen.de
 *     Stefan Bieliauskas: sBieliauskas@stud.hs-bremen.de
 *
 * Web:
 *     https://github.com/B-Stefan/Risiko
 *
 */

package Client.ui.CUI.utils;

import commons.interfaces.IToStringRemote;
import Client.ui.CUI.exceptions.InvalidCommandListernArgumentException;

import java.awt.event.ActionEvent;
import java.rmi.RemoteException;
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

    private final IToStringRemote context;

    /**
     * Enthält alle möglichkeiten, die er User ausführen kann
     */
    private final HashMap<String, CommandListener> commands = new HashMap<String, CommandListener>();


    /**
     * Bietet die Funktion zum wechseln der Ebene
     *@see Client.ui.CUI.utils.CommandListener
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
     *@see Client.ui.CUI.utils.CommandListener
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
    protected CUI(IToStringRemote context) {
        this.context = context;
        this.addCommandListener(new ChangeDirListener());
        this.addCommandListener(new HelpListener());

    }

    /**
     *
     * @param context  - Der Kontext für den eine CUI erzeugt werden soll
     * @param parent - übergeordnete CUI
     */
    public CUI(IToStringRemote context, CUI parent) {
        this(context);
        this.parent = parent;
    }


    /**
     * Dient zum wechseln in den untergeordneten Kontext
     */
    protected abstract void goIntoChildContext(LinkedHashMap<String,CommandListenerArgument> args);

    /**
     * Dient zum wechseln in den untergeordneten Kontext und führt den wechsel auch tatsächlich durch.
     * @param context In welchen context er gehen soll
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
     * @return Normalisierter Input
     */
    protected String[] normalizeArguments(final String rawInput) {
        String[] splits = this.normalizeRawInput(rawInput);
        if (splits.length == 1) {
            return new String[1];
        } else {
            return Arrays.copyOfRange(splits, 1, splits.length);
        }
    }

    /**
     * Versucht einen Befehl und die Argumente auszuführen.
     * @param command - Der Befehl
     * @param args - Die Argumente als String Array
     */
    protected void fireCommandEvent(final String command, final String[] args)  {

        if (!commands.containsKey(command)) {
            IO.println("Es konnte keine Aktion mit dem Namen '" + command + "' gefunden werden verwende 'help' um eine Auswahl der Möglichkeiten zu erhalten.");
            return;
        }

        CommandListener currentCommandListener = commands.get(command);

        //Set Arguments
        Iterator<Map.Entry<String,CommandListenerArgument>> requiredArgumentsInterator = currentCommandListener.getArguments().entrySet().iterator();
        for (int i = 0;  requiredArgumentsInterator.hasNext(); i++){
            java.util.Map.Entry<String, CommandListenerArgument> requiredCommandEntry = requiredArgumentsInterator.next();
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
     */
    protected void fireCommandEvent(String rawInput)  {
        this.fireCommandEvent(this.normalizeCommand(rawInput), this.normalizeArguments(rawInput));
    }

    /**
     * Versucht eine Eingabe eines Nutzers zu formatieren.
     * @param listener - Unformtierter Input vom User
     */
    protected void fireCommandEvent(CommandListener listener)  {
        listener.actionPerformed(new ActionEvent(this, 42, listener.getCommand()));
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
        String contextstr;
        try {
            contextstr = context.toStringRemote();
        }catch (RemoteException e){
            throw new RuntimeException(e);
        }
        return prefix + contextstr + " > ";
    }

    /**
     * Gibt den Aktuellen status der CUI zurück
     * @return Aktueller status der CUI
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
