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
    private ArrayList<Integer> TcpPorts;
    private int rr_index;

    public UdpGrdsManager(int port) {
        clients = new ArrayList<Client>();
        TcpPorts = new ArrayList<Integer>();
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

        int port = Integer.parseInt(tcpPort);
        TcpPorts.add(port);

        System.out.println("Sent to Server: " + dp.getAddress().getHostAddress() + ":" + dp.getPort() + " - " + localTime);

    }

    public void ClientServerConnection(DatagramPacket dp) throws Exception {
        //Aplicar o escalonamento circular round-robin e distribuir os servidores pelos clientes
        //Mandar duas mensagens: uma com o address do Servidor e outra com o porto de escuta Tcp do Servidor a distribuir pelo cliente
        if(rr_index>=TcpPorts.size())
            rr_index=0;
        if(TcpPorts.size() != 0) {
            String portTcp = String.valueOf(TcpPorts.get(rr_index));
            sendMessage(new Message(Message.Type.SERVER_PORT, portTcp,null), dp.getAddress().getHostAddress(), dp.getPort());
            rr_index++;
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

    public void addServer(DatagramPacket dp,Message msg){

        int port = Integer.parseInt(msg.getMessage());

        //aux.getUdpServerManager().setServerUdpPort(dp.getPort());
        //aux.getUdpServerManager().setServerAddress(dp.getAddress());
        //aux.getTcpServerManager().setServerTcpPort(port);
        TcpPorts.add(port);
    }


}
