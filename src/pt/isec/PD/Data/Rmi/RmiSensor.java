package pt.isec.PD.Data.Rmi;


public interface RmiSensor extends java.rmi.Remote {



    public RmiServerDetails getServers() throws java.rmi.RemoteException;

    public void addListener(RmiListeners listener) throws java.rmi.RemoteException;


    public void removeListener(RmiListeners listener) throws java.rmi.RemoteException;


}

