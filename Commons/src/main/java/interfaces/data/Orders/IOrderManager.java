package interfaces.data.Orders;

import interfaces.*;
import interfaces.data.*;

import java.io.Serializable;
import java.rmi.Remote;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import exceptions.PlayerAlreadyHasAnOrderException;

public interface IOrderManager extends Remote, Serializable {
	/**
    *
    * @param agend Spieler, für den ein Auftrag erzeugt werden soll
    * @param game Das dazugehöriege Spiel
    * @return Auftrag für den Spieler
    * @throws PlayerAlreadyHasAnOrderException
    */
   public IOrder createRandomOrder(final IPlayer agend, final IGame game);

   public void createOrdersForPlayers(final List<IPlayer> players, final  IGame game) throws PlayerAlreadyHasAnOrderException;
   
}
