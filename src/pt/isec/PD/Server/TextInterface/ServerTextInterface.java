package pt.isec.PD.Server.TextInterface;

import pt.isec.PD.Server.Model.Server;

import java.io.IOException;

public class ServerTextInterface {

    private Server server;

    public ServerTextInterface(Server server){
        this.server = server;
    }

    public void uiMain(){

        server.startTcp();
        server.startUdp();
    }
}
