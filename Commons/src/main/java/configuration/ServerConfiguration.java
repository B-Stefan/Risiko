package configuration;

/**
 * Konfigurationsklasse für den Server
 */
public class ServerConfiguration {

    /**
     * Default Server Konfiguration
     * port
     * host
     * serviceName
     */
    public final static ServerConfiguration DEFAULT = new ServerConfiguration(6789,"localhost","GameManagerService");


    /**
     * Erstellt aus einem String array String eine Server Konfiguration
     * @param args Stringarray
     *             [0] - HOST
     *             [1] - PORT
     *             [2] - GAME_SERVICE_NAME
     * @return
     * @throws IllegalArgumentException
     * @throws ClassCastException
     */
    public static ServerConfiguration fromArgs(String[] args) throws IllegalArgumentException, ClassCastException{
        if(args.length < 3){
            throw new IllegalArgumentException(args.toString());
        }
        return new ServerConfiguration(Integer.parseInt(args[0]),args[1],args[2]);

    }

    /**
     * Die Portnummer auf der der Server läuft
     */
    public final int     PORT;

    /**
     * Der Host unter der dem der Server erreichbar ist
     */
    public final String  SERVER_HOST;

    /**
     * Der ServiceName für den GameManager
     */
    public final String  SERVICE_NAME;

    /**
     *
     * @param PORT Port für den Server
     * @param SERVER_HOST Host für den Server bsp.: localhost
     * @param SERVICE_NAME Name des Service für den GameManager
     */
    public ServerConfiguration(int PORT, String SERVER_HOST, String SERVICE_NAME){
        this.PORT = PORT;
        this.SERVER_HOST = SERVER_HOST;
        this.SERVICE_NAME = SERVICE_NAME;
    }
}
