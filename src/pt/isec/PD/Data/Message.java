package pt.isec.PD.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;

public class Message implements Serializable {

    private static final long serialVersionUID = 1L;
    private int idMessage;
    private User user;
    private Group group;
    private Timestamp time;
    private Type type;
    private String message;
    private ArrayList<User> usersInfo;
    private Contact contact;

    public Message(Type type,String message,User user){

        this.type = type;
        this.message = message;
        this.user = user;
        this.usersInfo = new ArrayList<>();
    }

    public Message(Type type){
        this.type = type;
    }

    public Message(Type type,User user){
        this.type = type;
        this.user = user;
    }

    public Message(Type type,String message){
        this.type = type;
        this.message = message;
    }

    public Message(Type type, String message, Contact request){
        this.type = type;
        this.message = message;
        this.contact = request;
    }


    public int getIdMessage() {
        return idMessage;
    }

    public void setIdMessage(int idMessage) {this.idMessage = idMessage;}

    public User getUser() {
        return user;
    }

    public void setUser(User user) {this.user = user;}

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {this.time = time;}

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<User> getUsersInfo() {return usersInfo;}

    public void setUsersInfo(ArrayList<User> usersInfo) {this.usersInfo = usersInfo;}

    public Contact getContactRequest() {
        return contact;
    }

    public void setContactRequest(Contact contact) {
        this.contact = contact;
    }

    public enum Type {
        CLIENT_CONNECTION,SERVER_CONNECTION,CLIENT_SERVER_CONNECTION,SERVER_PORT,CONNECT_TCP,TCP_PORT,LOGIN,LOGIN_SUCESS,LOGIN_FAILED,LOGOUT,LOGOUT_COMPLETE,REGISTER,REGISTER_SUCESS,REGISTER_FAILED,CHANGE_PASSWORD,
        CHANGE_USERNAME,CHANGE_NAME,PASSWORD_CHANGED,USERNAME_CHANGED_SUCESS,USERNAME_CHANGED_FAILED,NAME_CHANGED_SUCESS,NAME_CHANGED_FAILED,SEARCH_USER,LIST_USERS,USER_RECEIVED,LIST_RECEIVED,USER_DONT_EXIST,CONTACT_REQUEST,
        CONTACT,CONTACT_ACCEPT,CONTACT_REFUSED,DELETE_CONTACT,SERVER_CONTACT_REQUEST,SERVER_DELETE_CONTACT,SERVER_ACCEPT_CONTACT,SERVER_REFUSE_CONTACT
    }
}
