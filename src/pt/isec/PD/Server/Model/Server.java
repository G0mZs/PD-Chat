package pt.isec.PD.Server.Model;


import pt.isec.PD.Server.Database.DbHelper;
import pt.isec.PD.Server.Network.CommunicationHandler;


import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;


public class Server {

    private DbHelper dbHelper;
    private ServerDetails serverDetails;
    private CommunicationHandler communication;
    private boolean running = true;

    public Server(DbHelper dbHelper) throws IOException {

        this.dbHelper = dbHelper;
        this.serverDetails = new ServerDetails("Server", getHostAddress());

    }

    public String getHostAddress() {
        InetAddress hostAddress = null;

        try {
            hostAddress = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        return hostAddress.getHostAddress();
    }

    public ServerDetails getServerDetails() {
        return serverDetails;
    }

    public void setServerDetails(ServerDetails serverDetails) {
        this.serverDetails = serverDetails;
    }

    public CommunicationHandler getCommunication() {
        return communication;
    }

    public void setCommunication(CommunicationHandler communication) {
        this.communication = communication;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public int getTcpPort() {
        return serverDetails.getTcpPort();
    }

    public DbHelper getDbHelper() {
        return dbHelper;
    }




}
