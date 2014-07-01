package logic;

import interfaces.IClient;
import ui.GUI.JGameGUI;
import ui.GUI.utils.JModalDialog;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by Stefan on 01.07.14.
 */
public class ClientEventProcessor extends UnicastRemoteObject implements IClient {

    private final JGameGUI gameGUI;
    /**
     * Creates and exports a new UnicastRemoteObject object using an anonymous port.
     * <p>
     * <p>The object is exported with a server socket created using the {@link RMISocketFactory} class.
     *
     * @throws java.rmi.RemoteException if failed to export object
     * @since JDK1.1
     */
    protected ClientEventProcessor(final JGameGUI gameGUI) throws RemoteException {
        super();
        this.gameGUI = gameGUI;
    }

    public void updateGUI()throws RemoteException{
        JModalDialog.showInfoDialog(this.gameGUI,"Update","Update");
    }
}
