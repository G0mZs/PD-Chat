package pt.isec.PD.Server.TextInterface;

import pt.isec.PD.Server.Model.Server;

import java.io.IOException;

public class ServerTextInterface {

    private static Server server;

    public static void main(String[] args) throws IOException {
        server = new Server();
        server.getServerCommunicationHandler().startTcp();
        server.getServerCommunicationHandler().startUdp();

    }
}
