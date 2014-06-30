package configuration;



/**
 * Created by Stefan on 29.06.14.
 */
public class ServerConfiguration {

    /**
     * Default Server Konfiguration
     */
    public final static ServerConfiguration DEFAULT = new ServerConfiguration(
            6789,
            "localhost",
            "GameManagerService");


    /**
     * Erstellt aus einem String array String eine Server Konfiguration
     * @param args Stringarray
     *             [0] - HOST
     *             [1] - PORT
     *             [2] - GAME_SERVICE_NAME
     * @return
     * @throws InvalidArgumentException
     * @throws ClassCastException
     */
    public static ServerConfiguration fromArgs(String[] args) throws IllegalArgumentException, ClassCastException{
        if(args.length < 3){
            throw new IllegalArgumentException(args.toString());
        }
        return new ServerConfiguration(Integer.parseInt(args[0]),args[1],args[2]);

    }

    public final int     PORT;
    public final String  SERVER_HOST;
    public final String  SERVICE_NAME;

    public ServerConfiguration(int PORT, String SERVER_HOST, String SERVICE_NAME){
        this.PORT = PORT;
        this.SERVER_HOST = SERVER_HOST;
        this.SERVICE_NAME = SERVICE_NAME;
    }
}
