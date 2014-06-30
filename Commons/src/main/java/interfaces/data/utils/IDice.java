package interfaces.data.utils;

/**
 * Created by Stefan on 29.06.14.
 */
public interface IDice extends Comparable<IDice> {

    /**
     * Gibt die Gewürfelte Zahl aus
     * @return
     */
    public int getDiceNumber();
    /**
     *  throw the dice
     */
    public void throwDice();
	public boolean isDiceHigherOrEqual(IDice iDice);
}
