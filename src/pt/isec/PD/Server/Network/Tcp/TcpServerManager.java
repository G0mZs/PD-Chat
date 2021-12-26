package pt.isec.PD.Server.Network.Tcp;

import pt.isec.PD.Server.Model.Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpServerManager extends Thread{

    private Server model;
    private int tcpPort;
    ServerSocket server = null;

    public TcpServerManager(Server model) throws IOException {

        this.model = model;
        server = new ServerSocket(0);
        this.tcpPort = server.getLocalPort();
    }

    public int getTcpPort() {
        return tcpPort;
    }

    public void setTcpPort(int tcpPort) {
        this.tcpPort = tcpPort;
    }

    public void run() {

        try {

            while(model.isRunning()) {

                Socket socket;
                socket = server.accept();
                //ClientHandler newClient = new ClientHandler(socket);
                //clients.add(newClient);
                new Thread(new Authentication(socket,model)).start();

            }

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (server != null)
                try {
                    server.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }




}
