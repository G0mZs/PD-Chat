package pt.isec.PD.Client.Network.Tcp;


import pt.isec.PD.Data.Message;
import pt.isec.PD.Data.User;

import java.io.*;


public class TcpClientManager extends Thread {

    private ObjectInputStream in = null;
    private ObjectOutputStream out = null;
    private boolean register = false;
    private boolean login = false;
    private Message message;

    public TcpClientManager(ObjectInputStream in, ObjectOutputStream out) {

        this.in = in;
        this.out = out;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
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
                            System.out.println("Registration Sucess");
                            register = true;
                            break;
                        case REGISTER_FAILED:
                            System.out.println("Registration Failed");//Depois meter msg de erro para caso o nome ou username ser usado
                            register = false;
                            break;
                        case LOGIN_SUCESS:
                            System.out.println("Login Sucess");
                            setMessage(message);
                            login = true;
                            break;
                        case LOGIN_FAILED:
                            System.out.println("Login Failed");
                            login = false;
                            break;
                        case LOGOUT_COMPLETE:
                            System.out.println("Logout Sucess");
                            setMessage(message);
                            break;
                        case PASSWORD_CHANGED:
                            System.out.println("Your Password has been changed");
                            getMessage().getUser().setPassword(message.getUser().getPassword());
                            break;
                        case USERNAME_CHANGED_SUCESS:
                            System.out.println("Your Username has been changed");
                            getMessage().getUser().setUsername(message.getUser().getUsername());
                            break;
                        case USERNAME_CHANGED_FAILED:
                            System.out.println("This Username is already in use!");
                            break;
                        case NAME_CHANGED_SUCESS:
                            System.out.println("Your Name has been changed");
                            getMessage().getUser().setName(message.getUser().getName());
                            break;
                        case NAME_CHANGED_FAILED:
                            System.out.println("This Name is already in use");
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
