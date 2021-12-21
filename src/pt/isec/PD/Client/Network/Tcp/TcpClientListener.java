package pt.isec.PD.Client.Network.Tcp;


import pt.isec.PD.Data.Message;
import java.io.*;


public class TcpClientListener extends Thread {

    private ObjectInputStream in = null;
    private ObjectOutputStream out = null;

    public TcpClientListener(ObjectInputStream in,ObjectOutputStream out) {

        this.in = in;
        this.out = out;
    }


    public void run() {

        try {

            while (true) {

                Object readObject = in.readObject();
                if (readObject instanceof Message) {
                    Message message = (Message) readObject;

                    switch (message.getType()) {
                        case SKRT:

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
