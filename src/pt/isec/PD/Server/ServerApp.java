package pt.isec.PD.Server;

import pt.isec.PD.Data.Constants;
import pt.isec.PD.Server.TextInterface.ServerTextInterface;

public class ServerApp {

    private static ServerTextInterface serverTextInterface;

    public static void main(String[] args){
        serverTextInterface = new ServerTextInterface();
        serverTextInterface.uiMain();

    }

}
