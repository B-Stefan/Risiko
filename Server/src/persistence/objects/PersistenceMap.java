package persistence.objects;
import logic.Game;
import logic.data.*;
import logic.data.Map;
import persistence.PersistenceManager;
import persistence.exceptions.PersistenceEndpointIOException;


import java.util.*;

public class PersistenceMap extends PersitenceObject<Map> {

    public final List<String> players = new ArrayList<String>();
    private final UUID id;
    private final List<String> countries = new ArrayList<String>();
    public PersistenceMap(Map map, PersistenceManager manager) throws PersistenceEndpointIOException{
        super(map,manager);
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
