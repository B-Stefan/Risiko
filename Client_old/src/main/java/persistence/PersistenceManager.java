package main.java.persistence;

/**
 * @author Jennifer Theloy, Thu Nguyen, Stefan Bieliauskas
 */

import main.java.persistence.objects.PersistenceGame;
import main.java.logic.Game;
import java.io.File;

/**
 *
 */
public class PersistenceManager {

    public static final String DEFAULT_PATH  = "data/";
    private static Boolean isDirCreated = false;

    public static void createDir (){
        if(!PersistenceManager.isDirCreated){
            File dir = new File(DEFAULT_PATH);
            dir.mkdirs();
            PersistenceManager.isDirCreated = true;
        }
    }
    public static String convertFileNameToPath(final String fileName){

        return DEFAULT_PATH + fileName.replace("/","");
    }
    public IPersistenceObject<?> createHandler (Class<?> type){

        if (type == Game.class){
                return new PersistenceGame();
        }
        //@todo add more types
        return null;
    };

}
