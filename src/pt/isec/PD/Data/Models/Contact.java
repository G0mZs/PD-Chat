package pt.isec.PD.Data.Models;

import java.io.Serializable;

public class Contact implements Serializable {

    private static final long serialVersionUID = 1L;
    private User sender;
    private User receiver;
    private boolean accept;

    public Contact(User sender, User receiver){
        this.sender = sender;
        this.receiver = receiver;
    }

    public User getReceiver() {
        return receiver;
    }

    public User getSender() {
        return sender;
    }

    public boolean getAccept(){
        return accept;
    }

    public void setAccept(boolean accept) {
        this.accept = accept;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }
}
