package exceptions;


public class PlayerNameAlreadyChooseException extends Exception {
    public PlayerNameAlreadyChooseException(String name){
        super("Der Spielername " + name + " wird bereits im Spiel verwendets");
    }
}
