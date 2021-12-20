package pt.isec.PD.Client.Model;

import pt.isec.PD.Client.Network.ClientComunicationHandler;
import pt.isec.PD.Data.Utils;

import java.net.SocketException;

public class Client {

    private ClientComunicationHandler clientComunicationHandler;

    public Client() throws SocketException {

        clientComunicationHandler = new ClientComunicationHandler();

    }

    public ClientComunicationHandler getClientComunicationHandler() {
        return clientComunicationHandler;
    }

    public void mainMenu(){

        clientComunicationHandler.getUdpClientManager().start();


        System.out.println("");
        System.out.println("------ User Main Menu ------");
        System.out.println("");
        System.out.println("1 --> Login");
        System.out.println("2 --> Registo");


    }


}
