package pt.isec.PD.Data;

public class ContactRequest {

    private User sender;
    private User receiver;
    private boolean accept;

    public ContactRequest(User sender, User receiver,boolean accept){
        this.sender = sender;
        this.receiver = receiver;
        this.accept = accept;
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
