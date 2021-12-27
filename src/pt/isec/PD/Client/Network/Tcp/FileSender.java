package pt.isec.PD.Client.Network.Tcp;

import pt.isec.PD.Client.Model.Chat;
import pt.isec.PD.Data.FileInfo;
import pt.isec.PD.Data.User;

import java.io.*;
import java.net.Socket;

public class FileSender implements Runnable {

    private Socket socket = null;
    private FileInfo fileInfo;
    private Chat chat;
    private User user;



    public FileSender(Chat chat, User user, int port, FileInfo fileInfo) {
        this.chat = chat;
        this.fileInfo = fileInfo;
        this.user = user;

        try {
            //socket = new Socket(user.getAddress(), port);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void run() {
        DataOutputStream dos = null;
        FileInputStream fis = null;
        long startTime = System.currentTimeMillis();
        long endTime;
        byte[] buffer = new byte[fileInfo.getSize()];
        int count = 0;
        int totalRead = 0;

        try {
            dos = new DataOutputStream(socket.getOutputStream());
            fis = new FileInputStream(fileInfo.getPath());

            while ((count = fis.read(buffer)) > 0) {
                dos.write(buffer, 0, count);
                totalRead += count;
            }

            fis.close();
            dos.close();
            socket.close();

            if (totalRead == fileInfo.getSize()) {
                endTime = System.currentTimeMillis();
                printTransferDetails(startTime, endTime, totalRead);
                //controller.fileSent(fileInfo.getName(), user.getUsername());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void printTransferDetails(long startTime, long endTime, int totalRead) {
        System.out.println("Transfer begun......");
        System.out.println(totalRead + " bytes written in " + (endTime - startTime) + " ms.");
    }

}
