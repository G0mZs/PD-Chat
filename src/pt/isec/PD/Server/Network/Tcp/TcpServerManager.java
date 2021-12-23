package pt.isec.PD.Server.Network.Tcp;

import pt.isec.PD.Data.Message;
import pt.isec.PD.Server.Database.DbHelper;
import pt.isec.PD.Server.Model.Server;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpServerManager extends Thread{


    private int serverTcpPort;
    private InetAddress serverAddress;
    private ServerSocket serverSocket;
    private Socket socket = null;
    private DbHelper dbHelper;

    public TcpServerManager(DbHelper dbHelper) throws IOException {

        this.serverSocket = new ServerSocket(0);
        this.dbHelper = dbHelper;
    }

    public int getServerTcpPort() {
        return serverSocket.getLocalPort();
    }

    public InetAddress getServerAddress() {
        return serverAddress;
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public void setServerTcpPort(int serverTcpPort) {
        this.serverTcpPort = serverTcpPort;
    }

    public int getServerTcpPort1(){
        return this.serverTcpPort;
    }

    public void run() {

        dbHelper.connectDatabase();

        try {

            while(true) {

                socket = serverSocket.accept();
                new Thread(new Authentication(socket,dbHelper)).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (serverSocket != null)
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }




}
