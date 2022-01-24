package pt.isec.PD.Server.Network.Udp;

import pt.isec.PD.Data.Models.Message;
import pt.isec.PD.Data.Models.Utils;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class UdpMessageSender {
    private DatagramSocket socket;


    public UdpMessageSender() {
        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(Message message, String hostname, int port) throws Exception {
        byte buffer[];
        buffer = Utils.convertToBytes(message);

        InetAddress address = InetAddress.getByName(hostname);
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, port);
        socket.send(packet);
    }
}