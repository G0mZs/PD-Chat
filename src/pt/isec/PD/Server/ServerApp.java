package pt.isec.PD.Server;

import pt.isec.PD.GRDS.Model.Grds;
import pt.isec.PD.GRDS.TextInterface.GrdsTextInterface;
import pt.isec.PD.Server.Model.Server;
import pt.isec.PD.Server.TextInterface.ServerTextInterface;

import java.io.IOException;

public class ServerApp {

    private static ServerTextInterface serverTextInterface;

    public static void main(String[] args){

        Server server = null;
        try {
            server = new Server();
        } catch (IOException e) {
            e.printStackTrace();
        }


        serverTextInterface = new ServerTextInterface(server);
        serverTextInterface.uiMain();

    }

}
