package pt.isec.PD.Server.Model;


import pt.isec.PD.Data.Constants;
import pt.isec.PD.Server.Network.Tcp.TcpServerManager;
import pt.isec.PD.Server.Network.Udp.UdpServerManager;

import java.io.IOException;

public class Server {

    private TcpServerManager tcpServerManager;
    private UdpServerManager udpServerManager;

    public Server() throws IOException {

        tcpServerManager = new TcpServerManager();
        udpServerManager = new UdpServerManager(Constants.UDP_PORT,Constants.GRDS_ADDRESS,tcpServerManager.getServerTcpPort());

    }

    public UdpServerManager getUdpServerManager() {
        return this.udpServerManager;
    }

    public TcpServerManager getTcpServerManager() {
        return this.tcpServerManager;
    }

    public void startUdp(){ udpServerManager.start();}

    public void startTcp(){
        tcpServerManager.start();
    }
}
