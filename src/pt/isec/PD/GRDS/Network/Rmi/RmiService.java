package pt.isec.PD.GRDS.Network.Rmi;

import pt.isec.PD.Data.Rmi.RmiServerDetails;
import pt.isec.PD.Data.Rmi.RmiListeners;
import pt.isec.PD.Data.Rmi.RmiSensor;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class RmiService extends UnicastRemoteObject implements RmiSensor {

    private static final long serialVersionUID = 1L;
    private List<RmiListeners> listeners;
    private RmiServerDetails serverDetails;

    public RmiService(RmiServerDetails serverDetails) throws RemoteException {

        this.serverDetails = serverDetails;
    }



    @Override
    public RmiServerDetails getServers() throws RemoteException {
        return this.serverDetails;
    }

    @Override
    public void addListener(RmiListeners listener) throws RemoteException {
        listeners.add(listener);
    }

    @Override
    public void removeListener(RmiListeners listener) throws RemoteException {
        listeners.remove(listener);
    }
}
