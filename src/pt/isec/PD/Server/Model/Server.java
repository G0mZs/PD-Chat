package pt.isec.PD.Server.Model;


import pt.isec.PD.Data.Constants;
import pt.isec.PD.Server.Network.ServerCommunicationHandler;
import pt.isec.PD.Server.Network.Tcp.TcpServerManager;
import pt.isec.PD.Server.Network.Udp.UdpServerManager;

import java.io.IOException;
import java.net.SocketException;

public class Server {

    private ServerCommunicationHandler serverCommunicationHandler;

    public Server() throws IOException {
        serverCommunicationHandler = new ServerCommunicationHandler();

    }

    public ServerCommunicationHandler getServerCommunicationHandler() {
        return serverCommunicationHandler;
    }
}
