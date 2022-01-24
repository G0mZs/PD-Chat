package pt.isec.PD.Server.Model;

import pt.isec.PD.Data.Models.User;

import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientDetails {

    private User user;
    private Socket socket;
    private ObjectOutputStream out;


    public ClientDetails(User user, Socket socket, ObjectOutputStream out){
        this.user = user;
        this.socket = socket;
        this.out = out;
    }

    public ObjectOutputStream getOut() {
        return out;
    }

    public User getUser() {
        return user;
    }
}
