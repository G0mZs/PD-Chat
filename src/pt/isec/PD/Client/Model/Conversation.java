package pt.isec.PD.Client.Model;

import pt.isec.PD.Data.Message;
import pt.isec.PD.Data.User;

import java.util.ArrayList;

public class Conversation {
    private ArrayList<Message> messages;
    private User user;


    public Conversation(User user) {
        this.user = user;
        messages = new ArrayList<>();
    }


    public void addMessage(Message message) {
        messages.add(message);
    }


    public ArrayList<Message> getMessages() {
        return messages;
    }


    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }


    public User getUser() {
        return user;
    }


    public void setUser(User user) {
        this.user = user;
    }

}
