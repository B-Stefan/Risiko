package commons.exceptions;

import commons.configuration.FightConfiguration;

/**
 * Created by Stefan on 12.07.14.
 */
public class FightMoveMinimumOneArmy extends Exception{

    public FightMoveMinimumOneArmy(){
        super("Sie müssen midenstens "+ FightConfiguration.NUMBER_OF_ARMIES_TO_OCCUPIED_COUNTRY +" Armee auf das neu besetzte Land ziehen");
    }
}
