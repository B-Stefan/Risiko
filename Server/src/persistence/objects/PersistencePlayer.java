package persistence.objects;

import logic.data.Army;
import logic.data.Country;
import logic.data.Map;
import logic.data.Player;
import exceptions.CountriesNotConnectedException;
import persistence.PersistenceManager;
import persistence.exceptions.PersistenceEndpointIOException;

import java.awt.Color;
import java.util.HashMap;
import java.util.UUID;

public class PersistencePlayer extends PersitenceObject<Player> {

    private transient Player player;
    private final UUID id;
    private final String name;
    private final Color color;
    private final HashMap<UUID,Integer> countries = new HashMap<UUID,Integer>();
    public PersistencePlayer(Player player, PersistenceManager manager) throws PersistenceEndpointIOException{
        super(player, manager);
        this.player = player;
        this.name = player.getName();
        this.color = player.getColor();
        this.id =  player.getId();
        for (Country c : player.getCountries()){
            countries.put(c.getId(),c.getNumberOfArmys());
        }
    }

    @Override
    public UUID getID() {
        return this.id;
    }

    @Override
    public Player convertToSourceObject(PersistenceManager manager) throws PersistenceEndpointIOException{

        Map defaultMap = manager.getMapHandler().get(Map.DEFAULT_MAP_UUID);
        if(defaultMap == null){
            throw new PersistenceEndpointIOException("Die Karte "+Map.DEFAULT_MAP_UUID+ " konnte nicht gefunden werden ");
        }

        //Erstellen einer Liste aller Countries um einfach darin zu suchen, die Map hat bereits eine GetCountry(String) die auf dem Namen bassiert
        HashMap<UUID,Country> allCountries = new HashMap<UUID,Country>();
        for(Country country: defaultMap.getCountries()){
            allCountries.put(country.getId(),country);
        }

        //Erstellen der Player instanz
        Player newInstance = new Player(this.name, this.color);

        // Zuordnen der Länder und Einheiten
        for(java.util.Map.Entry<UUID,Integer>  entry : this.countries.entrySet()){
            Country mapCountry = allCountries.get(entry.getKey());

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