package pt.isec.PD.Monitor;

import pt.isec.PD.Data.Rmi.RmiServerDetails;
import pt.isec.PD.Data.Rmi.RmiSensor;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class App {

    public static void main(String[] args) {

        boolean exit = true;
        try {
            String registration = "127.0.0.1";

            if (args.length > 0) {
                registration = args[0];
            }

            Registry r = LocateRegistry.getRegistry(registration);
            String[] services = r.list();
            if (services.length == 0) {
                System.out.println("No services found");
                return;
            }

            registration = "rmi://" + registration + "/" + services[0];
            System.out.println(registration);
            Remote remoteService = r.lookup(services[0]);

            System.out.println();
            RmiSensor sensor = (RmiSensor) remoteService;

            RmiServerDetails details = sensor.getServers();



            System.out.println("Servers:");
            for (Integer tcpPort : details.getTcpPorts()) {
                System.out.println("-------------------------------------------------");
                System.out.println("Server: ->  Ip:127.0.0.1   Tcp Port:" + tcpPort);
                System.out.println("-------------------------------------------------");
                System.out.println();

            }

            //RmiMonitor monitor = new RmiMonitor();
            //sensor.addListener(monitor);
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        /*} catch (AccessException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }*/
    }
    }

}
