package main.resources;
import main.java.logic.*;

import java.util.ArrayList;

/**
 * Created by Stefan on 01.04.2014.
 */
public interface IGameGUI {
    public void onPlayerAdd(String name) throws Exception;
    public void onPlayerDelete(Player player) throws Exception;
    public void onGameStart() throws Exception;
    public ArrayList<Player> getPlayers() throws Exception;
}
