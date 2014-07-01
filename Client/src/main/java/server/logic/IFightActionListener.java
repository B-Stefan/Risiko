package server.logic;

import interfaces.IFight;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Stefan on 01.07.14.
 */
public interface IFightActionListener  {

    /**
     * Invoked when an action occurs.
     *
     * @param fight
     */
    void actionPerformed(IFight fight);
}
