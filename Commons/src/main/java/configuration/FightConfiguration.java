package configuration;

/**
 * Klasse für Konfiguration des Fights
 */
public class FightConfiguration {

    /**
     * Legt fest wieviele Einheiten auf dem Land verbleiben sollen und somit nicht an einem Kampf beteiligt sein können
     */
    public static int NUMBER_OF_ARMIES_EXCLUDE_FROM_FIGHT = 1;
    /**
     * Maximale Anzahl der Armee, mit denen man verteidigen kann
     */
    public static int DEFENDER_MAX_NUMBER_OF_ARMIES_TO_DEFEND = 2;
    /**
     * Maximale Anzahl der Armeen mit denen man angreifen kann
     */
    public static int AGGRESSOR_MAX_NUMBER_OF_ARMIES_TO_ATTACK = 3;
}
