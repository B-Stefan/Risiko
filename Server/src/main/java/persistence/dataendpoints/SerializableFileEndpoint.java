package persistence.dataendpoints;

import persistence.PersistenceManager;
import persistence.objects.PersitenceObject;

import java.io.*;
import java.util.HashMap;
import java.util.UUID;

/**
 * Diese Klasse stellt die Möglichkeit zur Speicherung durch einfache Serialisierung zur Verfügung.
 * @param <T>
 */
public class SerializableFileEndpoint<T> extends AbstractFileEndpoint<T> {



    public SerializableFileEndpoint(Class<T> sourceClass, Class<? extends PersitenceObject<T>> dataClass, PersistenceManager manager) {
        super(sourceClass, dataClass, manager);
    }

    /**
     * Liest den File und packt alle Objekte des Files in die Liste chacedObjects
     */
    @Override
    protected void readFile() {
        {
            AbstractFileEndpoint.createDir();
            HashMap<UUID, PersitenceObject<T>> fileData = null;
            try {
                ObjectInputStream reader = new ObjectInputStream(new FileInputStream(AbstractFileEndpoint.convertFileNameToPath(this.fileName)));
                chachedObjects = (HashMap<UUID, PersitenceObject<T>>) reader.readObject();
            }catch (ClassNotFoundException e){
                throw new RuntimeException(e);
            }
            catch (EOFException e){
                //File ist leer

            }
            catch (InvalidClassException e){

            }
            catch (FileNotFoundException e){
                this.writeFile(); // File erstellen, wenn nicht vorhanden
            }
            catch (IOException e){
                //@Todo Besseres Exception Handling
                e.printStackTrace();
            }
            if(fileData != null){
                this.chachedObjects = fileData;
            }
        }
    }

    /**
     * Schreibt die Liste chachedObjetcts in die Datei
     */
    @Override
    protected void writeFile() {
        {
            try {
                FileOutputStream fos  = new FileOutputStream(AbstractFileEndpoint.convertFileNameToPath(this.fileName),false);
                ObjectOutputStream  writer = new ObjectOutputStream(fos);
                writer.writeObject(this.chachedObjects);
                writer.close();
            }
            catch (IOException e){
                e.printStackTrace();
            }

        }
    }
}
