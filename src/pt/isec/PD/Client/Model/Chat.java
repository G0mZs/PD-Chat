package pt.isec.PD.Client.Model;

import pt.isec.PD.Data.Message;
import pt.isec.PD.Data.User;
import pt.isec.PD.Data.Utils;

import java.io.File;
import java.util.ArrayList;

public class Chat {
    private User user;

    private ArrayList<User> contacts;
    private ArrayList<User> pendingRequests;
    private ArrayList<Message> historic;
    private ArrayList<Conversation> conversations;

    private File saveLocation;

    //private DefaultMutableTreeNode files;

    private boolean running = true;

    private File sharedFolder;

    public Chat() {

        this.contacts = new ArrayList<>();
        this.historic = new ArrayList<>();
        this.conversations = new ArrayList<>();
    }


    public File getSharedFolder() {
        return sharedFolder;
    }


    public void setSharedFolder(File sharedFolder) {
        this.sharedFolder = sharedFolder;
    }


    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }


    public ArrayList<Conversation> getConversations() {
        return conversations;
    }


    public void setConversations(ArrayList<Conversation> conversations) {
        this.conversations = conversations;
    }


    public ArrayList<User> getContacts() {
        return contacts;
    }


    public void setContacts(ArrayList<User> users) {
        this.contacts = users;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ArrayList<User> getPendingRequests() {
        return pendingRequests;
    }

    public void setPendingRequests(ArrayList<User> pendingRequests) {
        this.pendingRequests = pendingRequests;
    }

    public File getSaveLocation() {
        return saveLocation;
    }


    public void setSaveLocation(File saveLocation) {
        this.saveLocation = saveLocation;
    }

    public ArrayList<Message> getHistoric() {
        return historic;
    }

    public void setHistoric(ArrayList<Message> historic) {
        this.historic = historic;
    }

}


    /*public void setFiles(DefaultMutableTreeNode files) {
        this.files = files;
    }


    public DefaultMutableTreeNode getFiles() {
        return files;
    }*/
