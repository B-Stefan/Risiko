package main.java.logic.data.orders;

import main.java.logic.*;
import main.java.logic.data.Continent;
import main.java.logic.data.Player;
import main.java.logic.exceptions.PlayerAlreadyHasAnOrderException;

import java.util.List;
import java.util.Random;
import java.util.ArrayList;

/**
 * @author Jennifer Theloy, Thu Nguyen, Stefan Bieliauskas
 *
 * Diese Klasse bildet die Funktionen zum zufälligen generieren der Orders ab
 */
public class OrderManager {


    /**
     * Beinhaltet alle Klassen, für die eine Order erstellt werdne kann
     */
    public static final Class[] orderTypes = new Class[] {
            OrderTakeOverContinents.class,
            OrderTakeOverCountries.class,
            OrderTerminatePlayer.class
    };


    /**
     * Hilfsfunktion, um einen zufälligen Index von 0 bis max zu erzeugen
     * @param max Maximaler Index, der zufällig ausgegeben werden darf
     * @return Zufälliger Index zwischen 0 und max
     */
    private static int getRandomIndex(final int max){
        return new Random().nextInt(max);
    }

    /**
     * Hilfsfunktion, um einen Zufälligen Eintrag aus einer Liste zu bekommen
     * @param choosList  Liste, aus der ein Eintrag gewählt werden soll
     * @return Zufälligen Eintrag aus der Liste
     */
    private static Object getRandomFromList(final List<?> choosList){
        int randIndex = getRandomIndex(choosList.size());
        return choosList.get(randIndex);
    }

    /**
     * Hilfsfunktion um einen zufälligen Eintrag aus einer Liste zu bekommen, der nicht dem Paramter exclude entspricht.
     * @param choosList Liste, aus der ein Eintrag gewählt werden soll
     * @param exclude Dieses Objekt wird nicht als zufälliger Wert ausgegeben
     * @return Zufälligen Eintrag aus der Liste, außer exclude
     */
    private static Object getRandomFromList(final List<?> choosList, final Object exclude){
        ArrayList<Object> excludeList  = new ArrayList<Object>();
        excludeList.add(exclude);
        return getRandomFromList(choosList, excludeList);

    }

    /**
     * Hilfsfunktion um einen zufälligen Eintrag aus einer Liste zu bekommen, der nicht dem Paramter exclude entspricht.
     * @param choosList Liste, aus der ein Eintrag gewählt werden soll
     * @param excludeList Liste mit Objekten, die nicht ausgewählt werden sollen.
     * @return
     */
    private static Object getRandomFromList(List<?> choosList, final List<?> excludeList){
        choosList = new ArrayList<Object>(choosList);//Kopieren, da wir nicht die Liste der Kontinte in der map verändert möchten.
        choosList.removeAll(excludeList); // alle Objekte bis auf die aus der excludeListe
        if(choosList.size() == 0){
            return null;
        }
        int randIndex = getRandomIndex(choosList.size());
        return choosList.get(randIndex);
    }


    /**
     * Dient zur Ermittlung eines zufälligen Kontinents
     * @param allContinents Liste aller Kontinente
     * @param exclude Auszuschließendes Kontinent
     * @return Zufälliges Kontinent, außer das exclude
     */
    private static Continent getRandomContinent(final List<Continent> allContinents,final Continent exclude){
        return (Continent) getRandomFromList(allContinents,exclude);
    }

    /**
     * Dient zur Ermittlung eines zufälligen Kontinents
     * @param allContinents Liste aller Kontinente
     * @return Zufälliges Kontinent
     */
    private static Continent getRandomContinent(List<Continent> allContinents){
        return (Continent) getRandomFromList(allContinents);
    }


    /**
     *  Dient zur Ermittlung eines zufälligen Spielers
     * @param allPlayers Liste aller Spieler
     * @param exclude Spieler der nicht ausgewählt werden darf
     * @return Zufälliger Spieler der nicht exclude entspricht
     */
    private static Player getRandomPlayer(final List<Player> allPlayers,final  Player exclude){
        return (Player) getRandomFromList(allPlayers,exclude);
    }

    /**
     * Dient zur Ermittlung eines zufälligen Spielers
     * @param allPlayers Liste aller Spieler
     * @return Zufälligen Spieler
     */
    private static Player getRandomPlayer(List<Player> allPlayers){
        return (Player) getRandomFromList(allPlayers);
    }

    /**
     *
     * @param agend Spieler, für den ein Auftrag erzeugt werden soll
     * @param game Das dazugehöriege Spiel
     * @return Auftrag für den Spieler
     * @throws PlayerAlreadyHasAnOrderException
     */
    public static IOrder createRandomOrder(final Player agend, final Game game) {

        int randIndex = new Random().nextInt(orderTypes.length);
        Class randOrderType = orderTypes[randIndex];



        if(randOrderType == OrderTakeOverContinents.class){

            Continent contigent1  = getRandomContinent(game.getMap().getContinents());
            Continent contigent2  = getRandomContinent(game.getMap().getContinents(), contigent1);

            return new OrderTakeOverContinents(contigent1,contigent2,agend);
        }
        else if(randOrderType == OrderTakeOverCountries.class){
            boolean withTwoArmys = new Random().nextBoolean();
            return new OrderTakeOverCountries(withTwoArmys,agend);
        }

        else if(randOrderType == OrderTerminatePlayer.class){
            Player playerToTerminate = getRandomPlayer(game.getPlayers(),agend);
            return new OrderTerminatePlayer(playerToTerminate,agend);
        }
        return null;

    }

    public static void createOrdersForPlayers(final List<Player> players, final  Game game) throws PlayerAlreadyHasAnOrderException{
        List<PlayerAlreadyHasAnOrderException> exceptions = new ArrayList<PlayerAlreadyHasAnOrderException>();

        //Für jeden Spieler eine Order setzten, exceptions sammeln, da die Player danach eventuell ja funktionieren könnten.
        for(Player player : players){
            if (player.getOrder() != null){
                exceptions.add(new PlayerAlreadyHasAnOrderException(player));
            }
            else {
                IOrder newOrder = createRandomOrder(player, game);
                player.setOrder(newOrder);
            }
        }

        //exceptions ausgeben, auch wenn hierbei nur die erste ausgegeben wird.
        if(!exceptions.isEmpty()){
            for(PlayerAlreadyHasAnOrderException e : exceptions){
                throw  e;
            }
        }
    }

}
