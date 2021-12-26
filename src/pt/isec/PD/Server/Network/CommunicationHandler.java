package pt.isec.PD.Server.Network;

import pt.isec.PD.Data.Constants;
import pt.isec.PD.Data.Message;
import pt.isec.PD.Data.User;
import pt.isec.PD.Server.Model.ClientDetails;
import pt.isec.PD.Server.Model.Server;
import pt.isec.PD.Server.Network.Tcp.TcpServerManager;
import pt.isec.PD.Server.Network.Udp.UdpMessageSender;
import pt.isec.PD.Server.Network.Udp.UdpServerListener;

import java.io.IOException;

public class CommunicationHandler {

    private Server server;
    private UdpMessageSender udpMessageSender;
    private TcpServerManager tcpServerManager;
    private UdpServerListener udpServerListener;
    //private PortSender portSender;

    public CommunicationHandler(Server server){
        this.server = server;

        try {
            this.tcpServerManager = new TcpServerManager(server);
            this.udpServerListener = new UdpServerListener(Constants.UDP_PORT, Constants.GRDS_ADDRESS,server,tcpServerManager.getTcpPort());
            //this.portSender = new PortSender(tcpServerManager.getTcpPort(),Constants.GRDS_ADDRESS,Constants.UDP_PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }


        this.udpMessageSender = new UdpMessageSender();
    }

    public void startTCP() {

        this.tcpServerManager.start();
    }


    public void startUDP() {

       this.udpServerListener.start();
    }

    /*public void sendUDPMessage(Message message, User user) {
        try {
            udpMessageSender.sendMessage(message, user.getAddress(), user.getUdpPort());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/


    public void sendTCPMessage(ClientDetails client, Message message) {
        try {
            client.getOut().writeObject(message);
            client.getOut().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public TcpServerManager getTcpServerManager() {
        return tcpServerManager;
    }

    public UdpServerListener getUdpServerManager() {
        return udpServerListener;
    }
}
