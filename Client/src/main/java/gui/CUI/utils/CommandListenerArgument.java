package main.java.gui.CUI.utils;
import main.java.gui.CUI.exceptions.InvalidCommandListernArgumentException;


/**
 * Diese Klasse beinhaltet die Struktur für ein Argument, das auf der Konsole hinter einer Befehl eingegeben wird.
 */
public class CommandListenerArgument  {
    /**
     * Der Name des Arguments, dieser wird beim späteren verarbeiten des Arguments zur Identifizierung verwendet
     * @see main.java.gui.CUI.utils.CommandListener#getArgument(String)
     */
    private final String argumentName;

    /**
     * Wert, den das Argument aktuell hat
     */
    private String argumentValue = "";
    /**
     * Hilfetext für den Benutzer
     */
    private String helpText  = "";


    /**
     * Erstellt ein Argument
     * @param name - Name der zur Identifizierung verwendet wird
     */
    public CommandListenerArgument (final String name ) {
            this.argumentName = name;

    }

    /**
     *
     * @param name  Name der zur Identifizierung verwendet wird
     * @param helpText  Hilfetext für den Nutzer
     */
    public CommandListenerArgument (final String name,final String helpText) {
        this(name);
        this.helpText = helpText;

    }

    /**
     * Versucht die Konvertiert des aktuellen argumentValue einen Int Wert
     * @return - den Aktuellen Wert in Form eines Ints
     * @throws InvalidCommandListernArgumentException
     */
    public int toInt() throws InvalidCommandListernArgumentException{
        int re;
        try {
            re = Integer.parseInt(this.argumentValue);
        }catch (NumberFormatException e){
            throw new InvalidCommandListernArgumentException(this, "Int");
        }
        return re;
    }

    /**
     * Gibt den Namen des Arguments zurück
     * @return - Name des Arguments
     */
    public String getName(){
        return this.argumentName;
    }

    /**
     * Setzt den Wert des Arguments. Dabei ist null ein ungülter Wert und wird nicht akzeptiert.
     * @param value Wert, den das Arugment annehmen soll.
     */
    public void setValue (String value){
        if (value != null){
            this.argumentValue = value;
        }
    }

    /**
     * Versucht den aktuellen Wert in einen String umzuwandeln.
     * Dabei wurde die Exception-Handling beachtet, sodass die toString Method nicht einfach überschrieben werden konnte
     * @return - Aktueller Wert des Arguments
     * @throws InvalidCommandListernArgumentException
     */
    public String toStr() throws InvalidCommandListernArgumentException {
        if (this.argumentValue.equals("")){
            //@todo Besseres Exception Handling, ein Wert mit "" kann durchaus erlaubt sein.
            throw new InvalidCommandListernArgumentException(this, "String");
        }
        return this.argumentValue;
    }

}
