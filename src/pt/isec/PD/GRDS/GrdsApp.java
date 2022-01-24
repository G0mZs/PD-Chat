package pt.isec.PD.GRDS;

import pt.isec.PD.GRDS.Model.Grds;
import pt.isec.PD.GRDS.Network.CommunicationHandler;

import java.rmi.RemoteException;

public class GrdsApp {


    public static void main(String[] args) throws RemoteException {
        Grds grds = new Grds();
        CommunicationHandler handler = new CommunicationHandler(grds);
        grds.setHandler(handler);
        System.out.println("\nGrds Initialized");

        handler.UdpThread();
        handler.registerRmiService();

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                handler.exit();

            }
        });
    }
}
