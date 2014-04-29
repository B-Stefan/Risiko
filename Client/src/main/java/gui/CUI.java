package main.java.gui;

import java.util.Arrays;
import java.lang.reflect.Method;

/**
 * @author Stefan
 * Diese Klasse ist für die Verwaltung der einzelnen Console Line Ebenen zuständig. Eine Instanz dieser Klasse stellt eine Ebene da.
 * Beispiel: "Game > Map >" In diesem Beispiel wäre Game eine Instanze einer Klasse, die CUI implementiert und Map anlaog zu Game.
 */
public abstract class CUI {

    public static enum states {
        SILENT,
        LISTEN
    }
    public static final String präfixCUIMethods = "CUI";

    private states currentState = states.SILENT;
    private CUI parent;
    private Object context;


    private CUI child;

    protected CUI (Object context){
        this.context = context;

    }
    public CUI (Object context, CUI parent){
        this(context);
        this.parent = parent;
    }

    protected abstract void goIntoChildContext () throws Exception;

    protected void goIntoParentContext() throws Exception{
        if(this.parent != null){
            this.setCurrentState(states.SILENT);
            this.parent.listenConsole();
        }
    }

    protected void listenConsole() throws Exception{
        String rawCommand = "";
        this.setCurrentState(states.LISTEN);
        while (this.getCurrentState() == states.LISTEN) {
            IO.print(this.toString()); //Print Context
            rawCommand = IO.readString();
            this.parseCommand(rawCommand);
        }
    }
    private String[] normalizeRawInput (final String rawInput){
        return this.normalizeRawInput(rawInput, " ");
    }
    private String[] normalizeRawInput (String rawInput, final String split){
        rawInput = rawInput.trim();
        String[] splitString = rawInput.split(split);

        //Wenn input nur show oder ähnliches
        if (splitString.length == 1 || splitString.length == 0){
            String[] re = {rawInput};
            return re;
        }
        //Ansonsten gesmates Array zurückgeben
        else {
            return splitString;
        }
    }
    protected String normalizeCommand(final String rawInput){
        return CUI.präfixCUIMethods + this.normalizeRawInput(rawInput)[0];
    }
    protected String deNormalizeCommand(final String command){
        String[] splits = this.normalizeRawInput(command,CUI.präfixCUIMethods);
        if(splits.length == 1){
            return "";
        }
        return splits[1];
    }
    protected String[] normalizeArguments(final String rawInput){
        String[] splits = this.normalizeRawInput(rawInput);
        if(splits.length == 1){
            String[] re = new String[1];
            return re;
        }
        else
        {
            return  Arrays.copyOfRange(splits, 1, splits.length);
        }
    }
    protected void parseCommand (final String command, final String[] args) throws Exception{

        Class c = this.getClass();
        Class noparams[] = { String[].class};

        //Wechseln des Kontext funktionen
        if (command.equals(new String(this.normalizeCommand("cd")))) {
            if (args[0] == null){
                this.goIntoChildContext();
            }
            else {
               this.goIntoParentContext();
            }
        }
        //Klassenspezifische Funktionen
        else {
            try {
                Method  method = c.getDeclaredMethod(command,String[].class);
                method.invoke (this, new Object[] {args});
            }
            catch (NoSuchMethodException e){
                IO.println("Es konnte keine Aktion mit dem Namen '" + this.deNormalizeCommand(command)  + "' gefunden werden ");
            }
        }


    }
    protected void parseCommand (String rawInput) throws Exception{
        this.parseCommand(this.normalizeCommand(rawInput), this.normalizeArguments(rawInput));
    }

    public String toString (){
        String prefix = "";
        if (this.parent != null){
            prefix += this.parent.toString() ;
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
