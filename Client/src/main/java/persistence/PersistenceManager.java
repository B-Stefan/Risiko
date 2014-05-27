package main.java.persistence;

/**
 * @author Jennifer Theloy, Thu Nguyen, Stefan Bieliauskas
 */

import main.java.logic.Game;
import main.java.persistence.dataendpoints.PersistenceEndpoint;
import main.java.persistence.dataendpoints.SerializableFileEndpoint;
import main.java.persistence.objects.PersistenceGame;

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

        return DEFAULT_PATH + fileName.replace("/","");     //Falls file name mit "/filename" angegbeen wurde"
    }
    public PersistenceEndpoint<?> createHandler (Class type){

        if (type == Game.class){
                return new SerializableFileEndpoint<Game>(Game.class,PersistenceGame.class);
        }
        //@todo add more types
        return null;
    };

}
