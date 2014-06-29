package interfaces;

import exceptions.RoundCompleteException;
import exceptions.ToManyNewArmysException;
import exceptions.TurnNotCompleteException;
import interfaces.data.IPlayer;

/**
 * Created by Stefan on 29.06.14.
 */
public interface IRound {
    /**
     * Setzt den nächsten Spieler als aktuellen Spieler
     * @throws RoundCompleteException
     */
    public void setCurrentPlayer() throws RoundCompleteException;

    /**
     * Getter für den aktuellen Spieler
     * @return currentPayler: gibt aktuellen Spieler
     */
    public IPlayer getCurrentPlayer();



    /**
     * Erzeugt und setzt den nächsten Turn, wenn erlaubt
     * @throws exceptions.ToManyNewArmysException
     * @throws exceptions.TurnNotCompleteException
     * @throws RoundCompleteException
     */
    public void setNextTurn() throws ToManyNewArmysException, TurnNotCompleteException, RoundCompleteException;


    /**
     * Pürft, ob die Runde komplett abgeschlossen ist, wenn ja True
     * @return True wenn Runde abgeschlossen ist
     * @throws ToManyNewArmysException
     */
    public boolean isComplete() throws ToManyNewArmysException;

    /**
     * GIbt den aktuellen Turn zurück
     * @return
     */
    public ITurn getCurrentTurn();

    }
