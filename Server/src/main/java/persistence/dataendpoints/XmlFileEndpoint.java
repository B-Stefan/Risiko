package persistence.dataendpoints;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import persistence.PersistenceManager;
import persistence.objects.PersitenceObject;

import java.io.*;
import java.util.HashMap;
import java.util.UUID;

/**
 * Diese Klasse stellt die Möglichkeit zur Speicherung durch einfache Serialisierung zur Verfügung.
 * @param <T>
 */
public class XmlFileEndpoint<T> extends AbstractFileEndpoint<T> {



    private XStream xstream;
    public XmlFileEndpoint(Class<T> sourceClass, Class<? extends PersitenceObject<T>> dataClass, PersistenceManager manager) {
        super(sourceClass, dataClass, manager);
        this.xstream = new XStream(new StaxDriver());
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
                FileInputStream reader = new FileInputStream(AbstractFileEndpoint.convertFileNameToPath(this.fileName));
                chachedObjects = (HashMap<UUID, PersitenceObject<T>>) xstream.fromXML(reader);
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
                String xml = xstream.toXML(this.chachedObjects);
                fos.write(xml.getBytes());
            }
            catch (IOException e){
                e.printStackTrace();
            }

        }
    }
}
