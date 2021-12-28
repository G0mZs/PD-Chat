package pt.isec.PD.Server.Network.Udp;

import pt.isec.PD.Data.Constants;
import pt.isec.PD.Data.Message;
import pt.isec.PD.Data.Utils;
import pt.isec.PD.Server.Model.Server;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;

public class UdpServerListener extends Thread{

    private final static int BUFFER = 4096;
    private Server server;
    private DatagramSocket socket = null;
    private InetAddress grdsAddress;
    private int grdsPort;
    private int serverTcpPort;
    private ArrayList<Integer> activeServers;


    /**
     * <p>
     * Constructor for UDPListener.
     * </p>
     *
     * @param   {@link } object.
     * @param   port       a int.
     * @throws java.net.SocketException if any.
     */

    public UdpServerListener(int port, String grdsAddress, Server server, int serverTcpPort) throws SocketException {

        this.server = server;
        try {
            this.grdsAddress = InetAddress.getByName(grdsAddress);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        this.grdsPort = port;
        this.serverTcpPort = serverTcpPort;
        this.activeServers = new ArrayList<>();
    }

    public ArrayList<Integer> getActiveServers() {
        return activeServers;
    }

    public void initializeGrdsServerConnection() {

        try {
            this.socket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
    }

        try {

            Message msg = new Message(Message.Type.SERVER_CONNECTION,String.valueOf(serverTcpPort));
            sendMessage(msg,grdsAddress.getHostAddress(),grdsPort);
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

        boolean running = true;
        byte[] buffer = new byte[BUFFER];

        initializeGrdsServerConnection();


        while (running) {

            try {
                Arrays.fill(buffer, (byte) 0);
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                Object readObject = Utils.convertFromBytes(buffer);

                if (readObject instanceof Message) {
                    Message message = (Message) readObject;

                    switch ((message.getType())) {
                        case SERVER_CONNECTION:
                            saveOtherServers(message.getMessage());
                            break;
                    }
                } else {

                    System.err.println("Received unrecognized data on UDP socket! Ignoring...");

                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (socket != null && socket.isConnected()) {
            socket.close();
        }
    }

    public void saveOtherServers(String tcpPort){
        int port = Integer.parseInt(tcpPort);
        activeServers.add(port);

    }
}
