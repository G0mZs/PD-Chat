package pt.isec.PD.Server.Network.Tcp;

import pt.isec.PD.Data.Message;
import pt.isec.PD.Server.Model.Server;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpServerManager extends Thread{


    private int serverTcpPort;
    private InetAddress serverAddress;
    private ServerSocket serverSocket = null;
    private Socket socket = null;

    public TcpServerManager() throws IOException {

        this.serverSocket = new ServerSocket(0);
    }

    public void run() {

        try {

            while(true) {

                socket = serverSocket.accept();


                ObjectOutputStream out;
                out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

                Message message = (Message) in.readObject();

                switch (message.getType()) {
                    case CONNECT_TCP:
                        System.out.println(message.getMessage());
                        Message msg = new Message(Message.Type.SKRT,"Ayoo");
                        out.writeObject(msg);
                        out.flush();
                        break;
                }
            }

        } catch (IOException | ClassNotFoundException e) {
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
}
