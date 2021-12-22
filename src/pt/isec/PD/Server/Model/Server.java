package pt.isec.PD.Server.Model;


import pt.isec.PD.Data.Constants;
import pt.isec.PD.Server.Database.DbHelper;
import pt.isec.PD.Server.Network.Tcp.TcpServerManager;
import pt.isec.PD.Server.Network.Udp.PortSender;
import pt.isec.PD.Server.Network.Udp.UdpServerManager;


import java.io.IOException;
import java.sql.SQLException;

public class Server {

    private TcpServerManager tcpServerManager;
    private UdpServerManager udpServerManager;
    private PortSender portSender;
    private DbHelper dbHelper;
    private boolean isRunning = true;

    public Server() throws IOException {

        tcpServerManager = new TcpServerManager();
        udpServerManager = new UdpServerManager(Constants.UDP_PORT,Constants.GRDS_ADDRESS,tcpServerManager.getServerTcpPort());
        portSender = new PortSender(tcpServerManager.getServerTcpPort(),Constants.GRDS_ADDRESS,Constants.UDP_PORT);
        dbHelper = new DbHelper("localhost","mydb","root","sql098");
    }

    public UdpServerManager getUdpServerManager() {
        return this.udpServerManager;
    }

    public TcpServerManager getTcpServerManager() {
        return this.tcpServerManager;
    }

    public void startUdp(){ udpServerManager.start();}

    public void startTcp(){
        tcpServerManager.start();
    }

    public void startPortSender(){portSender.start();}

    public void connectDatabase(){dbHelper.connectDatabase();}
}
