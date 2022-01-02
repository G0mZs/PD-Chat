package pt.isec.PD.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Message implements Serializable {

    private static final long serialVersionUID = 1L;
    private int idMessage;
    private User user;
    private LocalDateTime dateTime;
    private Type type;
    private String typeofMessage;
    private String message;
    private String receiver;
    private String state;
    private ArrayList<User> usersInfo;
    private ArrayList<Message> messagesInfo;
    private Contact contact;
    private Group group;
    private ArrayList<Group> groupsInfo;
    private Request request;


    public Message(int idMessage,User author,String typeofMessage,String message,LocalDateTime dateTime,String state){
        this.idMessage = idMessage;
        this.user = author;
        this.typeofMessage = typeofMessage;
        this.message = message;
        this.dateTime = dateTime;
        this.state = state;
    }

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

    public Message(Type type,ArrayList<User> users){
        this.type = type;
        this.usersInfo = users;
    }

    public Message(Type type,String message,ArrayList<Message> messages){
        this.type = type;
        this.messagesInfo = messages;
        this.message = message;
    }

    public Message(Type type,String message){
        this.type = type;
        this.message = message;
    }

    public Message(Type type,Request request){
        this.type = type;
        this.request = request;
    }

    public Message(Type type, String message, Contact request){
        this.type = type;
        this.message = message;
        this.contact = request;
    }

    public Message(Type type, String message,String typeofMessage, LocalDateTime dateTime,User author,String receiver,String state){
        this.type = type;
        this.message = message;
        this.typeofMessage = typeofMessage;
        this.dateTime = dateTime;
        this.user = author;
        this.receiver = receiver;
        this.state = state;
    }

    public Message(Type type, ArrayList<Group> listGroups1,int i) {
        this.type = type;
        this.groupsInfo = listGroups1;

    }

    public Message(Type type,Group group,String message){
        this.type = type;
        this.group = group;
        this.message = message;
    }

    public Message(Type type, String msg, Request request) {
        this.type = type;
        this.message = msg;
        this.request = request;
    }

    public ArrayList<Group> getGroupsInfo() {
        return groupsInfo;
    }

    public void setGroupsInfo(ArrayList<Group> groupsInfo) {
        this.groupsInfo = groupsInfo;
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

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getTypeofMessage() {
        return typeofMessage;
    }

    public void setTypeofMessage(String typeofMessage) {
        this.typeofMessage = typeofMessage;
    }

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

    public String getReceiver() {
        return receiver;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public ArrayList<Message> getMessagesInfo() {
        return messagesInfo;
    }

    public void setMessagesInfo(ArrayList<Message> messagesInfo) {
        this.messagesInfo = messagesInfo;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public enum Type {
        CLIENT_CONNECTION,SERVER_CONNECTION,CLIENT_SERVER_CONNECTION,SERVER_PORT,CONNECT_TCP,TCP_PORT,LOGIN,LOGIN_SUCESS,LOGIN_FAILED,LOGOUT,LOGOUT_COMPLETE,REGISTER,REGISTER_SUCESS,REGISTER_FAILED,CHANGE_PASSWORD,
        CHANGE_USERNAME,CHANGE_NAME,PASSWORD_CHANGED,USERNAME_CHANGED_SUCESS,NAME_CHANGED_SUCESS,SEARCH_USER,LIST_USERS,USER_RECEIVED,LIST_RECEIVED,CONTACT_REQUEST,ERROR_MESSAGE,
        CONTACT_ACCEPT,CONTACT_REFUSED,DELETE_CONTACT,SERVER_CONTACT_REQUEST,SERVER_DELETE_CONTACT,SERVER_ACCEPT_CONTACT,SERVER_REFUSE_CONTACT,MESSAGE_CONTACT,SEND_MESSAGE,RECEIVE_MESSAGE,SERVER_RECEIVE_MESSAGE,MESSAGE_SEEN,
        SERVER_MESSAGE_SEEN,LIST_CONTACTS,LIST_PENDING_REQUESTS,LIST_HISTORIC,DELETE_MESSAGE,LIST_GROUPS,CREATE_GROUP,CHANGE_GROUP_NAME,GROUP_REQUEST,GROUP_ACCEPT,GROUP_REFUSE,SERVER_GROUP_REQUEST,
    }
}
