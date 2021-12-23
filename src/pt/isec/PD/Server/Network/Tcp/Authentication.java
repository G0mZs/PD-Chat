package pt.isec.PD.Server.Network.Tcp;

import pt.isec.PD.Data.Message;
import pt.isec.PD.Server.Database.DbHelper;

import java.io.*;
import java.net.*;

public class Authentication extends Thread{

    private DbHelper dbHelper;
    private Socket socket;
    private ObjectOutputStream out = null;
    private ObjectInputStream in = null;

    public Authentication(Socket socket,DbHelper dbHelper){
        this.socket = socket;
        this.dbHelper = dbHelper;
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run(){

        try {

            while (true) {

                Message message = (Message) in.readObject();
                switch (message.getType()) {
                    case CONNECT_TCP:
                        System.out.println(message.getMessage());
                        /*Message msg = new Message(Message.Type.SKRT,"Ayoo");
                        out.writeObject(msg);
                        out.flush();*/
                        break;
                    case LOGIN:
                        checkLogin(dbHelper.Login(message.getUser().getUsername(), message.getUser().getPassword()), out);
                        break;
                    case REGISTER:
                        System.out.println("Checking Register");
                        checkRegister(dbHelper.Register(message.getUser().getName(), message.getUser().getUsername()), out);
                        break;
                }
            }

        } catch(IOException e){
            e.printStackTrace();
        } catch(ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    public void checkLogin(boolean login,ObjectOutputStream out){

    }

    public void checkRegister(boolean register,ObjectOutputStream out){

        try {

            Message msg;
            if(register == false){

                msg = new Message(Message.Type.REGISTER_FAILED, "", null);
            }
            else{

                msg = new Message(Message.Type.REGISTER_SUCESS, "", null);
            }
            out.writeObject(msg);
            out.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
