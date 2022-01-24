package pt.isec.PD.Client.Model;

import pt.isec.PD.Data.Models.Group;
import pt.isec.PD.Data.Models.Message;
import pt.isec.PD.Data.Models.Request;
import pt.isec.PD.Data.Models.User;

import java.io.File;
import java.util.ArrayList;

public class Chat {
    private User user;

    private ArrayList<User> contacts;
    private ArrayList<User> pendingRequests;
    private ArrayList<Message> historic;
    private ArrayList<Conversation> conversations;
    private ArrayList<Group> groups;
    private ArrayList<Request> pendingGroupRequests;
    private ArrayList<Message> groupHistoric;

    private File saveLocation;

    //private DefaultMutableTreeNode files;

    private boolean running = true;

    private File sharedFolder;

    public Chat() {

        this.contacts = new ArrayList<>();
        this.historic = new ArrayList<>();
        this.groups = new ArrayList<>();
        this.conversations = new ArrayList<>();
        this.pendingGroupRequests = new ArrayList<>();
        this.groupHistoric = new ArrayList<>();
    }

    public ArrayList<Group> getGroups() {
        return groups;
    }

    public void setGroups(ArrayList<Group> groups) {
        this.groups = groups;
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

    public ArrayList<Request> getPendingGroupRequests() {
        return pendingGroupRequests;
    }

    public void setPendingGroupRequests(ArrayList<Request> pendingGroupRequests) {
        this.pendingGroupRequests = pendingGroupRequests;
    }

    public ArrayList<Message> getGroupHistoric() {
        return groupHistoric;
    }

    public void setGroupHistoric(ArrayList<Message> groupHistoric) {
        this.groupHistoric = groupHistoric;
    }
}



