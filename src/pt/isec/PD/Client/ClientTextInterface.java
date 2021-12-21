package pt.isec.PD.Client;

import pt.isec.PD.Client.Model.Client;

import java.io.IOException;

public class ClientTextInterface {

    private  Client client;

    public ClientTextInterface(Client client){
        this.client = client;
    }

    public void begin(){
        client.startConnection();

        System.out.println("");
        System.out.println("------ User Main Menu ------");
        System.out.println("");
        System.out.println("1 --> Login");
        System.out.println("2 --> Registo");
    }



}