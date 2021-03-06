package pt.isec.PD.GRDS.Network.Udp;

import pt.isec.PD.Data.Models.Constants;
import pt.isec.PD.Data.Models.Message;
import pt.isec.PD.Data.Models.Utils;

import java.io.IOException;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class UdpGrdsManager extends Thread {

    private final static int BUFFER = 4096;
    private int port;
    private DatagramSocket ds;
    private ArrayList<Integer> clients;
    private ArrayList<Integer> udpPorts;
    private ArrayList<Integer> TcpPorts;
    private int rr_index;

    public UdpGrdsManager(int port) {
        clients = new ArrayList<>();
        TcpPorts = new ArrayList<Integer>();
        udpPorts = new ArrayList<Integer>();
        this.port = port;
        rr_index=0;
    }



    public void run() {

        byte[] buffer = new byte[BUFFER];

        try {
            ds = new DatagramSocket(this.port);
        } catch (SocketException e) {
            e.printStackTrace();
        }

        while (true) {

            try {

                Arrays.fill(buffer, (byte) 0);
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                ds.receive(packet);

                Object readObject = Utils.convertFromBytes(buffer);
                if (readObject instanceof Message) {
                    Message message = (Message) readObject;

                    switch ((message.getType())) {
                        case CLIENT_CONNECTION:
                            ClientConnection(packet);
                            break;
                        case SERVER_CONNECTION:
                            ServerConnection(packet,message.getMessage());
                            break;
                        case CLIENT_SERVER_CONNECTION:
                            ClientServerConnection(packet);
                            break;
                        case TCP_PORT:
                            addServer(packet,message);
                            break;
                    }
                } else {

                    System.err.println("Received unrecognized data on UDP socket! Ignoring...");

                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    public ArrayList<Integer> getTcpPorts() {
        return TcpPorts;
    }

    public void ClientConnection(DatagramPacket dp) throws SocketException {

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String localTime = sdf.format(new Date());
        byte[] localTimeBytes = localTime.getBytes();

        dp.setData(localTimeBytes);
        dp.setLength(localTimeBytes.length);

        try {
            ds.send(dp);
        } catch (IOException e) {
            e.printStackTrace();
        }


        int aux = dp.getPort();

        clients.add(aux);

        System.out.println("Sent to Client: " + dp.getAddress().getHostAddress() + ":" + dp.getPort() + " - " + localTime);

    }

    public void ServerConnection(DatagramPacket dp,String tcpPort) throws IOException {

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String localTime = sdf.format(new Date());
        byte[] localTimeBytes = localTime.getBytes();

        dp.setData(localTimeBytes);
        dp.setLength(localTimeBytes.length);

        try {
            ds.send(dp);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int port = Integer.parseInt(tcpPort);
        TcpPorts.add(port);
        udpPorts.add(dp.getPort());

        System.out.println("Sent to Server: " + dp.getAddress().getHostAddress() + ":" + dp.getPort() + " - " + localTime);

        for (int i = 0; i < udpPorts.size(); i++) {
            if (udpPorts.get(i) != dp.getPort()) {

                Message msg = new Message(Message.Type.SERVER_CONNECTION, String.valueOf(port));
                byte buffer[];
                buffer = Utils.convertToBytes(msg);

                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(Constants.SERVER_ADDRESS), udpPorts.get(i));
                ds.send(packet);
            }
        }
        for (int j = 0; j < TcpPorts.size(); j++) {
            if (TcpPorts.get(j) != port) {
                System.out.println("1");
                Message msg = new Message(Message.Type.SERVER_CONNECTION, String.valueOf(TcpPorts.get(j)));
                byte buffer[];
                buffer = Utils.convertToBytes(msg);

                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(Constants.SERVER_ADDRESS), dp.getPort());
                ds.send(packet);

            }

        }


    }

    public void ClientServerConnection(DatagramPacket dp) throws Exception {

        if(rr_index>=TcpPorts.size())
            rr_index=0;
        if(TcpPorts.size() != 0) {
            String portTcp = String.valueOf(TcpPorts.get(rr_index));
            sendMessage(new Message(Message.Type.SERVER_PORT, portTcp), dp.getAddress().getHostAddress(), dp.getPort());
            rr_index++;
        }

    }

    public void sendMessage(Message message, String address, int port) throws Exception {
        byte buffer[];
        buffer = Utils.convertToBytes(message);

        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(address), port);
        ds.send(packet);
    }

    public void addServer(DatagramPacket dp,Message msg){

        int port = Integer.parseInt(msg.getMessage());

        //aux.getUdpServerManager().setServerUdpPort(dp.getPort());
        //aux.getUdpServerManager().setServerAddress(dp.getAddress());
        //aux.getTcpServerManager().setServerTcpPort(port);
        TcpPorts.add(port);
    }


}
