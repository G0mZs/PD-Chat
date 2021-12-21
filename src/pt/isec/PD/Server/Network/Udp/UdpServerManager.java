package pt.isec.PD.Server.Network.Udp;

import pt.isec.PD.Client.Network.Tcp.TcpClientListener;
import pt.isec.PD.Data.Constants;
import pt.isec.PD.Data.Message;
import pt.isec.PD.Data.Utils;

import java.io.IOException;
import java.net.*;
import java.util.Arrays;

public class UdpServerManager extends Thread{

    private final static int BUFFER = 4096;

    private DatagramSocket socket = null;

    private InetAddress grdsAddress;
    private int grdsPort;
    private InetAddress serverAddress;
    private int serverUdpPort;
    private int serverTcpPort;

    /**
     * <p>
     * Constructor for UDPListener.
     * </p>
     *
     * @param   {@link } object.
     * @param   port       a int.
     * @throws java.net.SocketException if any.
     */

    public UdpServerManager(int port,String grdsAddress,int tcpPort) throws SocketException {

        try {
            this.grdsAddress = InetAddress.getByName(grdsAddress);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        this.grdsPort = port;
        this.serverTcpPort = tcpPort;

    }


    /** Gets */
    public InetAddress getGrdsAddress() {
        return grdsAddress;
    }

    public int getGrdsPort() {
        return grdsPort;
    }


    public InetAddress getServerAddress() {
        return serverAddress;
    }

    public void setGrdsPort(int grdsPort) {
        this.grdsPort = grdsPort;
    }

    public int getServerTcpPort() {
        return serverTcpPort;
    }

    /** Sets */

    public void setServerUdpPort(int serverUdpPort) {
        this.serverUdpPort = serverUdpPort;
    }

    public void setGrdsAddress(InetAddress grdsAddress) {
        this.grdsAddress = grdsAddress;
    }

    public void setServerAddress(InetAddress serverAddress) {
        this.serverAddress = serverAddress;
    }

    public void setServerTcpPort(int serverTcpPort) {
        this.serverTcpPort = serverTcpPort;
    }

    /** No Arranque do Cliente , este recebe o endereço IP e o protocolo de escuta UDP do GRDS */



    public void initializeGrdsServerConnection() {

        try {
            this.socket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
    }

        try {

            Message msg = new Message(Message.Type.SERVER_CONNECTION,String.valueOf(this.serverTcpPort));
            sendMessage(msg,Constants.GRDS_ADDRESS,Constants.UDP_PORT);
            DatagramPacket packet = new DatagramPacket(new byte[256], 256);
            socket.receive(packet);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void sendMessage(Message message, String address, int port) throws Exception {

        byte buffer[];
        buffer = Utils.convertToBytes(message);

        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(address), port);
        socket.send(packet);
    }

    public void run() {

        byte[] buffer = new byte[BUFFER];

        initializeGrdsServerConnection();


        while (true) {

            try {
                Arrays.fill(buffer, (byte) 0);
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                Object readObject = Utils.convertFromBytes(buffer);

                if (readObject instanceof Message) {
                    Message message = (Message) readObject;

                    /*switch ((message.getType())) {
                        case :
                            break;
                    }*/
                } else {

                    System.err.println("Received unrecognized data on UDP socket! Ignoring...");

                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
