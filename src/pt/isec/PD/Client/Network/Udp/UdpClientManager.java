package pt.isec.PD.Client.Network.Udp;


import pt.isec.PD.Client.Model.Chat;
import pt.isec.PD.Data.Message;
import pt.isec.PD.Data.Utils;

import java.net.*;
import java.util.Arrays;


public class UdpClientManager extends Thread {

    private final static int BUFFER = 4096;

    private DatagramSocket socket = null;
    private InetAddress grdsAddress;
    private int grdsPort;
    private int clientPort;
    private InetAddress clientAddress;
    private int serverTcpPort;
    private Chat chat;


    public UdpClientManager(Chat chat,int port,String address){
        this.chat = chat;
        this.grdsPort = port;
        this.serverTcpPort = 0;
        try {
            this.grdsAddress = InetAddress.getByName(address);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

    }


    /** Gets */
    public InetAddress getGrdsAddress() {
        return grdsAddress;
    }

    public int getGrdsPort() {
        return grdsPort;
    }

    public int getClientPort() {
        return clientPort;
    }

    public InetAddress getClientAddress() {
        return clientAddress;
    }

    public int getServerTcpPort() {
        return serverTcpPort;
    }


    /** Sets */
    public void setClientAddress(InetAddress clientAddress) {
        this.clientAddress = clientAddress;
    }

    public void setClientPort(int clientPort) {
        this.clientPort = clientPort;
    }

    public void setServerTcpPort(int serverTcpPort) {
        this.serverTcpPort = serverTcpPort;
    }


    public void run() {
        Object readObject;
        boolean running = true;
        byte[] buffer = new byte[BUFFER];

        initializeGrdsClientConnection();


        while (running) {

            try {
                Arrays.fill(buffer, (byte) 0);
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                 readObject = Utils.convertFromBytes(buffer);

                    if (readObject instanceof Message) {
                        Message message = (Message) readObject;

                        switch ((message.getType())) {
                            case SERVER_PORT:
                                setServerTcpPort(Integer.parseInt(message.getMessage()));
                                break;
                        }
                    } else {

                        System.err.println("Received unrecognized data on UDP socket! Ignoring...");

                    }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (socket != null && socket.isConnected()) {
            socket.close();
        }
    }

    public void initializeGrdsClientConnection() {

        try {
            this.socket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }


        try {

            Message msg = new Message(Message.Type.CLIENT_CONNECTION);
            sendMessage(msg,this.grdsAddress.getHostAddress(),this.grdsPort);
            DatagramPacket packet = new DatagramPacket(new byte[256], 256);
            socket.receive(packet);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void askForServerConnection(){


        Message msg = new Message(Message.Type.CLIENT_SERVER_CONNECTION); // Onde está null no user depois meter o tcpPort do cliente


        try {
                sendMessage(msg,getGrdsAddress().getHostAddress(),getGrdsPort());
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



}
