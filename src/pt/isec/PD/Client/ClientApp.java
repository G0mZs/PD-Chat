package pt.isec.PD.Client;

import pt.isec.PD.Client.Model.Client;

import java.io.IOException;

public class ClientApp {

    private static ClientTextInterface clientTextInterface;

    public static void main(String[] args) throws IOException {
        Client client = new Client();
        clientTextInterface = new ClientTextInterface(client);
        clientTextInterface.uiMain();

    }
}
