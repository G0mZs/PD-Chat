package pt.isec.PD.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Message implements Serializable {

    private static final long serialVersionUID = 1L;
    private int idMessage,id;
    private User user;
    private Group group;
    private Request request;
    private ArrayList<Request> requests;
    private Timestamp time;
    private Type type;
    private String message,name,name2;
    private ArrayList<User> usersInfo;
    private ArrayList<Group> listGroups;

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

    public Message(Type type, ArrayList<Group> listGroups, int i){
        this.type = type;
        this.listGroups = listGroups;
    }
    public Message(Type type,Group group){
        this.type = type;
        this.group = group;
    }
    public Message(Type type,String name,String name2,User user){
        this.type = type;
        this.name = name;
        this.name2 = name2;
        this.user = user;
    }

    public Message(Type type, int id, User user){
        this.type = type;
        this.id = id;
        this.user = user;
    }

    public Message(Type type, Request request, User user){
        this.type = type;
        this.request = request;
        this.user = user;
    }

    public Message(Type type,ArrayList<Request> requests){
        this.type = type;
        this.requests = requests;
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

    public ArrayList<Group> getListGroups() {return listGroups;}

    public String getName() {
        return name;
    }

    public String getName2() {
        return name2;
    }

    public int getId() {
        return id;
    }

    public Request getRequest() {
        return request;
    }

    public ArrayList<Request> getRequests() {
        return requests;
    }

    public enum Type {
        CLIENT_CONNECTION,SERVER_CONNECTION,CLIENT_SERVER_CONNECTION,SERVER_PORT,CONNECT_TCP,TCP_PORT,LOGIN,LOGIN_SUCESS,LOGIN_FAILED,LOGOUT,LOGOUT_COMPLETE,REGISTER,REGISTER_SUCESS,REGISTER_FAILED,CHANGE_PASSWORD,
        CHANGE_USERNAME,CHANGE_NAME,PASSWORD_CHANGED,USERNAME_CHANGED_SUCESS,USERNAME_CHANGED_FAILED,NAME_CHANGED_SUCESS,NAME_CHANGED_FAILED,SEARCH_USER,LIST_USERS,USER_RECEIVED,LIST_RECEIVED,USER_DONT_EXIST,
        LIST_GROUPS,CREATE_GROUP,EDIT_GROUP,JOIN_GROUP,CREATE_GROUP_COMPLETED,CREATE_GROUP_FAILED,EDIT_GROUP_NAME_COMPLETED,EDIT_GROUP_NAME_FAILED,GROUP_REQUEST, GROUP_REQUEST_COMPLETED, GROUP_REQUEST_FAILED,
        LIST_REQUEST,LIST_MYGROUPS,GROUP_REQUEST_RESPONSE,GROUP_RESPONSE_COMPLETED,GROUP_RESPONSE_FAILED,GROUP_EXIT,GROUP_EXIT_COMPLETED,GROUP_EXIT_FAILED,LIST_MYADMINGROUPS,
        DELETE_GROUP,DELETE_GROUP_COMPLETED,DELETE_GROUP_FAILED,REMOVE_MEMBER,REMOVE_MEMBER_COMPLETED,REMOVE_MEMBER_FAILED,
    }
}
