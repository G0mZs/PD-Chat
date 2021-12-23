package pt.isec.PD.Client.Network.Tcp;


import pt.isec.PD.Data.Message;
import java.io.*;


public class TcpClientManager extends Thread {

    private ObjectInputStream in = null;
    private ObjectOutputStream out = null;
    private boolean register = false;
    private boolean login = false;

    public TcpClientManager(ObjectInputStream in, ObjectOutputStream out) {

        this.in = in;
        this.out = out;
    }

    public void setLogin(boolean login) {
        this.login = login;
    }

    public void setRegister(boolean register) {
        this.register = register;
    }

    public boolean getLogin(){
        return this.login;
    }

    public boolean getRegister(){
        return this.register;
    }

    public void run() {

        try {

            while (true) {

                Object readObject = in.readObject();
                if (readObject instanceof Message) {
                    Message message = (Message) readObject;

                    switch (message.getType()) {
                        case REGISTER_SUCESS:
                            System.out.println("Registry Sucess");
                            register = true;
                            break;
                        case REGISTER_FAILED:
                            System.out.println("Registry Failed");
                            register = false;
                            break;
                    }
                } else {
                    System.err.println("Received unrecognized data on TCP socket! Ignoring...");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void sendTCPMessage(Message message) {

        try {
            out.writeObject(message);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }


}
