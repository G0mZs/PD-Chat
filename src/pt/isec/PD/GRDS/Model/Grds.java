package pt.isec.PD.GRDS.Model;

import pt.isec.PD.Data.Models.Constants;
import pt.isec.PD.GRDS.Network.CommunicationHandler;
import pt.isec.PD.GRDS.Network.Rmi.RmiService;
import pt.isec.PD.GRDS.Network.Udp.UdpGrdsManager;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class Grds{

    private CommunicationHandler handler;

    public Grds(){

    }

    public CommunicationHandler getHandler() {
        return handler;
    }

    public void setHandler(CommunicationHandler handler) {
        this.handler = handler;
    }


}
