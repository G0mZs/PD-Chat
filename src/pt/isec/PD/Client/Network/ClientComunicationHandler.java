package pt.isec.PD.Client.Network;

import pt.isec.PD.Client.Network.Tcp.TcpClientListener;
import pt.isec.PD.Client.Network.Udp.UdpClientManager;
import pt.isec.PD.Data.Constants;

public class ClientComunicationHandler {

    private UdpClientManager udpClientManager;

 public ClientComunicationHandler(){

     this.udpClientManager = new UdpClientManager(Constants.UDP_PORT,Constants.GRDS_ADDRESS);

 }
    public UdpClientManager getUdpClientManager() {
        return udpClientManager;
    }

    public void startUdp() {
     udpClientManager.start();
    }

}


