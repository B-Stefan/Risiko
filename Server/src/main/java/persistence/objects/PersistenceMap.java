package persistence.objects;
import exceptions.PersistenceEndpointIOException;
import interfaces.data.ICountry;
import interfaces.data.IMap;
import logic.data.Map;
import persistence.PersistenceManager;


import java.util.*;

public class PersistenceMap extends PersitenceObject<IMap> {

    public final List<String> players = new ArrayList<String>();
    private final UUID id;
    private final List<String> countries = new ArrayList<String>();
    public PersistenceMap(IMap map, PersistenceManager manager) throws PersistenceEndpointIOException{
        super(map,manager);
        this.id = map.getId();
        for (ICountry country: map.getCountries()){
            this.countries.add(country.getId().toString());
        }

    }

    @Override
    public UUID getID() {
        return this.id;
    }

    @Override
    public IMap convertToSourceObject(PersistenceManager manager) throws PersistenceEndpointIOException{
        /*
         * Erstellen eines neuen Map Objects, da die Map nicht dynamisch sondern statisch ist reicht hier einfach das erzeugen.
         * Sollte sicht dies ändern müsste hier das laden der Countries angestoßen werden
         */
        Map newMap = new Map();
        return newMap;
    }
}
