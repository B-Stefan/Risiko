package main.java.persistence.objects;
import main.java.logic.Game;
import main.java.logic.data.*;
import main.java.logic.data.Map;
import main.java.persistence.PersistenceManager;
import main.java.persistence.dataendpoints.PersistenceEndpoint;
import main.java.persistence.exceptions.PersistenceEndpointIOException;


import java.util.*;

public class PersistenceMap extends PersitenceObject<Map> {

    public final List<String> players = new ArrayList<String>();
    private final UUID id;
    private final List<String> countries = new ArrayList<String>();
    public PersistenceMap(Map map){
        super(map);
        this.id = map.getId();
        for (Country country: map.getCountries()){
            this.countries.add(country.getId().toString());
        }
    }

    @Override
    public UUID getID() {
        return this.id;
    }

    @Override
    public Map convertToSourceObject(PersistenceManager manager) throws PersistenceEndpointIOException{
        /*
         * Erstellen eines neuen Map Objects, da die Map nicht dynamisch sondern statisch ist reicht hier einfach das erzeugen.
         * Sollte sicht dies ändern müsste hier das laden der Countries angestoßen werden
         */
        Map newMap = new Map();
        return newMap;
    }
}
