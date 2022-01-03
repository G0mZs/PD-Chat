package pt.isec.PD.Server.TextInterface;

import pt.isec.PD.Data.Constants;
import pt.isec.PD.Data.Message;
import pt.isec.PD.Server.Database.DbHelper;
import pt.isec.PD.Server.Model.ClientDetails;
import pt.isec.PD.Server.Model.Server;
import pt.isec.PD.Server.Network.CommunicationHandler;

import java.io.IOException;
import java.util.ArrayList;

public class ServerTextInterface {


    public void uiMain(){

        DbHelper helper = new DbHelper("localhost","mydb","root","sql098");
        helper.connectDatabase();

        Server server = null;
        try {
            server = new Server(helper);
        } catch (IOException e) {
            e.printStackTrace();
        }

        CommunicationHandler handler = new CommunicationHandler(server);
        if (server != null) {
            server.setCommunication(handler);
        }

        handler.startTCP();
        handler.startUDP();

        System.out.println("\nServer Initialized");

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                handler.warnClients();
                helper.close();
            }
        });
    }
}

