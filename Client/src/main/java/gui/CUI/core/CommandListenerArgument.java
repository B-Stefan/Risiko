package main.java.gui.CUI.core;

import com.sun.javaws.exceptions.InvalidArgumentException;
import main.java.gui.CUI.core.exceptions.InvalidCommandListernArgumentException;
import org.omg.CORBA.DynAnyPackage.Invalid;

/**
 * Created by Stefan on 02.05.14.
 */
public class CommandListenerArgument  {
    private final String argumentName;
    private Object defaultValue = new String("");
    private String argumentValue = "";
    private String helpText  = "";


    public CommandListenerArgument (final String name ) {
            this.argumentName = name;

    }
    public CommandListenerArgument (final String name,final String helpText) {
        this(name);
        this.helpText = helpText;

    }
    public int toInt() throws InvalidCommandListernArgumentException{
        int re;
        try {
            re = Integer.parseInt(this.argumentValue);
        }catch (NumberFormatException e){
            throw new InvalidCommandListernArgumentException(this, "Int");
        }
        return re;
    }
    public String getName(){
        return this.argumentName;
    }
    public void setValue (String value){
        if (value != null){
            this.argumentValue = value;
        }
    }


    public String toStr() throws InvalidCommandListernArgumentException {
        if (this.argumentValue == ""){
            throw new InvalidCommandListernArgumentException(this, "String");
        }
        return this.argumentValue;
    }

}
