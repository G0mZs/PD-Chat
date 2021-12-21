package pt.isec.PD.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Message implements Serializable {

    private int idMessage;
    private User user;
    private Group group;
    private Timestamp time;
    private Type type;
    private String message;

    public Message(Type type,String message){

        this.type = type;
        this.message = message;
    }

    public int getIdMessage() {
        return idMessage;
    }

    public User getUser() {
        return user;
    }

    public Group getGroup() {
        return group;
    }

    public Timestamp getTime() {
        return time;
    }

    public Type getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setGroup(Group group) {
        this.group = group;
    }



    public enum Type {
        CLIENT_CONNECTION,SERVER_CONNECTION,CLIENT_SERVER_CONNECTION,SERVER_PORT,CONNECT_TCP,SKRT,TCP_PORT
    }
}
