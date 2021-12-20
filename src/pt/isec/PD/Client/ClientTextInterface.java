package pt.isec.PD.Client;

import pt.isec.PD.Client.Model.Client;

import java.io.IOException;

public class ClientTextInterface {

    private static Client client;

    public static void main(String[] args) throws IOException {
        client = new Client();
        client.mainMenu();

    }


}