package pt.isec.PD.Client.Network.Tcp;


import pt.isec.PD.Data.Message;
import java.io.*;


public class TcpClientListener extends Thread {

    private ObjectInputStream in = null;

    public TcpClientListener(ObjectInputStream in) {

        this.in = in;
    }


    public void run() {

        try {

            while (true) {

                Object readObject = in.readObject();
                if (readObject instanceof Message) {
                    Message message = (Message) readObject;

                    switch (message.getType()) {
                        case SKRT:
                            System.out.println(message.getMessage());
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
}
