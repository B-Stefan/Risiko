package persistence.objects;

import exceptions.PersistenceEndpointIOException;
import interfaces.data.ICountry;
import interfaces.data.IMap;
import interfaces.data.IPlayer;
import logic.data.Army;
import logic.data.Country;
import logic.data.Map;
import logic.data.Player;
import exceptions.CountriesNotConnectedException;
import persistence.PersistenceManager;

import java.awt.Color;
import java.util.HashMap;
import java.util.UUID;

public class PersistencePlayer extends PersitenceObject<IPlayer> {

    private final UUID id;
    private final String name;
    private final Color color;
    private final HashMap<UUID,Integer> countries = new HashMap<UUID,Integer>();
    public PersistencePlayer(IPlayer player, PersistenceManager manager) throws PersistenceEndpointIOException{
        super(player, manager);
        this.name = player.getName();
        this.color = player.getColor();
        this.id =  player.getId();
        for (ICountry c : player.getCountries()){
            countries.put(c.getId(),c.getNumberOfArmys());
        }
    }

    @Override
    public UUID getID() {
        return this.id;
    }

    @Override
    public IPlayer convertToSourceObject(PersistenceManager manager) throws PersistenceEndpointIOException{

        IMap defaultMap = manager.getMapHandler().get(Map.DEFAULT_MAP_UUID);
        if(defaultMap == null){
            throw new PersistenceEndpointIOException("Die Karte "+Map.DEFAULT_MAP_UUID+ " konnte nicht gefunden werden ");
        }

        //Erstellen einer Liste aller Countries um einfach darin zu suchen, die Map hat bereits eine GetCountry(String) die auf dem Namen bassiert
        HashMap<UUID,ICountry> allCountries = new HashMap<UUID,ICountry>();
        for(ICountry country: defaultMap.getCountries()){
            allCountries.put(country.getId(),country);
        }

        //Erstellen der Player instanz
        Player newInstance = new Player(this.name, this.color);

        // Zuordnen der Länder und Einheiten
        for(java.util.Map.Entry<UUID,Integer>  entry : this.countries.entrySet()){
            ICountry mapCountry = allCountries.get(entry.getKey());

            //wenn nicht gefunden
            if (mapCountry == null){
                throw new PersistenceEndpointIOException("Die UUID" + entry.getKey().toString() + " konnte nicht in der Karte" + defaultMap.getId().toString() + " gefunden werden");
            }

            mapCountry.setOwner(newInstance);
            newInstance.addCountry(mapCountry);

            //Armeen erzeugen
            for(int i = 0; i!= entry.getValue(); i++){
                try {
                    Army newArmy = new Army(newInstance);
                    mapCountry.addArmy(newArmy);
                }catch (CountriesNotConnectedException e){
                    //Diese Ecception kann nicht aufterten, da die Armee noch keine Position hat, wenn doch trotzdem weiter druchreichen
                    throw  new PersistenceEndpointIOException(e);
                }
            }

        }


        return newInstance;
    }
}
