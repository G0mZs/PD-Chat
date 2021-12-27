package pt.isec.PD.Client.Network.Tcp;

import pt.isec.PD.Client.Model.Chat;
import pt.isec.PD.Data.FileInfo;
import pt.isec.PD.Data.User;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class FileReceiver implements Runnable {

    private ServerSocket serverSocket;
    private String path;
    private Chat chat;
    private User user;
    private FileInfo fileInfo;


    public FileReceiver(Chat chat, int port, FileInfo fileInfo, String path, User user) {
        this.chat = chat;
        this.fileInfo = fileInfo;
        this.path = path;
        this.user = user;

        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveFile(Socket socket) throws IOException {
        String newFile = path + File.separator + fileInfo.getName();
        FileOutputStream fileWritter;
        byte[] buffer = new byte[fileInfo.getSize()];
        long startTime = System.currentTimeMillis();

        int count;
        int totalRead = 0;

        try {
            fileWritter = new FileOutputStream(newFile);
            InputStream inputStream = socket.getInputStream();

            while ((count = inputStream.read(buffer)) > 0) {
                totalRead += count;
                fileWritter.write(buffer, 0, count);
            }

            fileWritter.close();

            if (totalRead == fileInfo.getSize()) {
                //controller.fileReceived(fileInfo.getName(), user.getUsername());
                long endTime = System.currentTimeMillis();
                printTransferDetails(startTime, endTime, totalRead);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            File file = new File(newFile);
            if (file.exists())
                file.delete();
        }
    }


    @Override
    public void run() {
        Socket socket = null;
        try {
            socket = serverSocket.accept();
            saveFile(socket);
            socket.close();
        } catch (UnknownHostException e) {
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
