package pt.isec.PD.Server.Network.Udp;

import pt.isec.PD.Data.Constants;
import pt.isec.PD.Data.Message;
import pt.isec.PD.Data.Utils;

import java.net.*;

public class PortSender extends Thread{

    private int tcpPort;
    private InetAddress grdsAddress;
    private int grdsPort;
    private DatagramSocket socket;

    public PortSender(int tcpPort,String grdsAddress,int grdsPort){
        this.tcpPort = tcpPort;

        try {
            this.grdsAddress = InetAddress.getByName(grdsAddress);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        this.grdsPort = grdsPort;
    }


    public void run(){

        boolean running = true;

        while(running) {
            try {

                this.socket = new DatagramSocket();

                Message msg = new Message(Message.Type.TCP_PORT, String.valueOf(this.tcpPort));
                sendMessage(msg, Constants.GRDS_ADDRESS, Constants.UDP_PORT);
                DatagramPacket packet = new DatagramPacket(new byte[256], 256);
                socket.receive(packet);

                Thread.sleep(20000);

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (SocketException e) {
                e.printStackTrace();
            } catch (Exception e) {
            e.printStackTrace();
            }
        }

        if (socket != null && socket.isConnected()) {
            socket.close();
        }
        //Se não receber a mensagem do servidor passado 3 vezes, o Grds esquece o servidor.
    }

    public void sendMessage(Message message, String address, int port) throws Exception {

        byte buffer[];
        buffer = Utils.convertToBytes(message);

        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(address), port);
        socket.send(packet);
    }
}
