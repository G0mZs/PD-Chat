package pt.isec.PD.GRDS.Network;

import pt.isec.PD.Client.Model.Client;
import pt.isec.PD.Data.Message;
import pt.isec.PD.Data.Utils;
import pt.isec.PD.Server.Model.Server;

import java.io.IOException;
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
    private ArrayList<Client> clients;
    private ArrayList<Server> servers;

    public UdpGrdsManager(int port) {
        clients = new ArrayList<Client>();
        servers = new ArrayList<Server>();
        this.port = port;
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
                            //Passar tudo isto para uma função
                            Server aux = new Server();
                            aux.getUdpServerManager().setServerUdpPort(packet.getPort());
                            aux.getUdpServerManager().setServerAddress(packet.getAddress());
                            int port = Integer.parseInt(message.getMessage());
                            aux.getTcpServerManager().setServerTcpPort(port);
                            servers.add(aux);
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


        Client aux = new Client();
        aux.getUdpClientManager().setClientPort(dp.getPort());
        aux.getUdpClientManager().setClientAddress(dp.getAddress());

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

        System.out.println("Sent to Server: " + dp.getAddress().getHostAddress() + ":" + dp.getPort() + " - " + localTime);

    }

    public void ClientServerConnection(DatagramPacket dp) throws Exception {
        //Aplicar o escalonamento circular round-robin e distribuir os servidores pelos clientes
        //Mandar duas mensagens: uma com o address do Servidor e outra com o porto de escuta Tcp do Servidor a distribuir pelo cliente
        if(servers.size() != 0) {
            String portTcp = String.valueOf(servers.get(0).getTcpServerManager().getServerTcpPort1());
            sendMessage(new Message(Message.Type.SERVER_PORT, portTcp), dp.getAddress().getHostAddress(), dp.getPort());
        }
        else{
            System.out.println("heyo");
        }

    }

    public void sendMessage(Message message, String address, int port) throws Exception {
        byte buffer[];
        buffer = Utils.convertToBytes(message);

        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(address), port);
        ds.send(packet);
    }




}
