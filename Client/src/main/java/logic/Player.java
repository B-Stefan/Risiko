package main.java.logic;

/**
 * Created by Stefan on 01.04.2014.
 */
public class Player {

    private String name;

    public Player(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }


    public String ToString() {
        return getName();
    }

}
