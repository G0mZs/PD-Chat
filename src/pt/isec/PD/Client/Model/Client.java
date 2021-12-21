package pt.isec.PD.Client.Model;

import pt.isec.PD.Client.Network.Udp.UdpClientManager;
import pt.isec.PD.Data.Constants;

import java.net.SocketException;

public class Client {

    private UdpClientManager udpClientManager;

    public Client() throws SocketException {

        this.udpClientManager = new UdpClientManager(Constants.UDP_PORT,Constants.GRDS_ADDRESS);


    }

    public UdpClientManager getUdpClientManager() {
        return udpClientManager;
    }

    public void startConnection(){

        udpClientManager.start();

    }


}
