package pt.isec.PD.GRDS.Network;

import pt.isec.PD.Data.Models.Constants;
import pt.isec.PD.Data.Models.User;
import pt.isec.PD.Data.Rmi.RmiServerDetails;
import pt.isec.PD.GRDS.Model.Grds;
import pt.isec.PD.GRDS.Network.Rmi.RmiService;
import pt.isec.PD.GRDS.Network.Udp.UdpGrdsManager;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class CommunicationHandler{


    private UdpGrdsManager udpGrdsManager;
    private Grds grds;
    public RmiService rmiService;

    public CommunicationHandler(Grds grds){
        this.grds = grds;
        udpGrdsManager = new UdpGrdsManager(Constants.UDP_PORT);

    }

    public List<Integer> getServerTcpPorts(){
        return udpGrdsManager.getTcpPorts();
    }

    public void registerRmiService() {
        try {
            Registry r;

            try {
                r = LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
                System.setProperty("java.rmi.server.hostname", InetAddress.getLocalHost().getCanonicalHostName());
            } catch (RemoteException e) {
                e.printStackTrace();
                r = LocateRegistry.getRegistry();
            }

            RmiServerDetails aux = new RmiServerDetails();

            aux.setTcpPorts((ArrayList<Integer>) getServerTcpPorts());


            rmiService = new RmiService(aux);


            r.bind("GRDS_Service", (Remote) rmiService);

        }catch(RemoteException e){
            System.out.println("Erro remoto - " + e);
        }catch(IOException e){
            System.out.println("Erro E/S - " + e);
        }catch(Exception e){
            System.out.println("Erro - " + e);
        }finally{
            if(rmiService != null){
                // algo
                try{
                    UnicastRemoteObject.unexportObject(rmiService, true);
                }catch(NoSuchObjectException e){}
            }
        }

    }

    public void removeRmi(){

        try {
            Naming.unbind("rmi://127.0.0.1/GRDS_Service");
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
    }



    public void UdpThread(){
        udpGrdsManager.start();
    }

    public void exit(){
        removeRmi();
    }
}

