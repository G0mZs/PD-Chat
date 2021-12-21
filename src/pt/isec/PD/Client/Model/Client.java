package pt.isec.PD.Client.Model;

import pt.isec.PD.Client.Network.Tcp.TcpClientListener;
import pt.isec.PD.Client.Network.Udp.UdpClientManager;
import pt.isec.PD.Data.Constants;
import pt.isec.PD.Data.Message;
import pt.isec.PD.Data.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class Client {

    private UdpClientManager udpClientManager;
    private User user;
    private ObjectInputStream input = null;
    private ObjectOutputStream output = null;
    private Socket s = null;
    boolean exit;

    public Client(){

        this.udpClientManager = new UdpClientManager(Constants.UDP_PORT,Constants.GRDS_ADDRESS);

    }

    public UdpClientManager getUdpClientManager() {
        return udpClientManager;
    }

    public User getUser() { return user;}

    public void setUser(User user) { this.user = user;}

    public void startConnection(){

        exit = false;
        udpClientManager.start();

        while(!exit){

            udpClientManager.askForServerConnection();
            if(checkTcpPort()){

                try {

                    exit = true;
                    connectTcp(Constants.SERVER_ADDRESS,udpClientManager.getServerTcpPort());
                    new Thread(new TcpClientListener(input,output)).start();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public boolean checkLogin(String username,String password){

        if(username == null || password == null){
            return false;
        }

        /*Enviar mensagem tcp ao servidor para autenticar
        else if(tcpClientManager.getLoginSucess() == false){
            return false
            else return true;
         */
        return true;
    }

    public boolean checkTcpPort(){

        if(udpClientManager.getServerTcpPort() == 0){
            return false;
        }
        else {
            return true;
        }
    }

    private void connectTcp(String serverIp,int serverPort) throws Exception {

        InetAddress add = InetAddress.getByName(serverIp);

        s = new Socket(add, serverPort);
        input = new ObjectInputStream(s.getInputStream());
        output = new ObjectOutputStream(s.getOutputStream());

        output.writeObject(new Message(Message.Type.CONNECT_TCP,"Wassup"));
        output.flush();

    }

    public void disconnect() {
        stopTcp();
    }

    public void stopTcp() {
        if (s != null && s.isConnected()) {
            try {
                input.close();
                output.close();
                s.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
