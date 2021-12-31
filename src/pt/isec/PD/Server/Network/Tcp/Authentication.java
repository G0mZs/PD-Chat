package pt.isec.PD.Server.Network.Tcp;

import pt.isec.PD.Data.Group;
import pt.isec.PD.Data.Message;
import pt.isec.PD.Data.Request;
import pt.isec.PD.Data.User;
import pt.isec.PD.Server.Model.ClientDetails;
import pt.isec.PD.Server.Model.Server;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Authentication extends Thread{

    private Server model;
    private Socket socket;


    public Authentication(Socket socket, Server model){
        this.socket = socket;
        this.model = model;
    }

    public void run(){

        try {

            ObjectOutputStream out;
            out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());


            while (true) {

                Message message = (Message) in.readObject();
                switch (message.getType()) {
                    case CONNECT_TCP:
                        System.out.println(message.getMessage());
                        break;
                    case LOGIN:
                        checkLogin(model.getDbHelper().Login(message.getUser().getUsername(), message.getUser().getPassword()), out,message);
                        break;
                    case REGISTER:
                        checkRegister(model.getDbHelper().Register(message.getUser().getName(), message.getUser().getUsername(),message.getUser().getPassword()), out,message);
                        break;
                    case LOGOUT:
                        logout(message.getUser().getId(),out);
                        break;
                    case CHANGE_PASSWORD:
                        changePassword(message.getUser().getId(),message.getUser().getPassword(),out);
                        break;
                    case CHANGE_NAME:
                        changeName(message.getUser().getId(),message.getUser().getName(),out);
                        break;
                    case CHANGE_USERNAME:
                        changeUsername(message.getUser().getId(),message.getUser().getUsername(),out);
                        break;
                    case SEARCH_USER:
                        searchUser(message.getUser().getUsername(),out);
                        break;
                    case LIST_USERS:
                        sendUsersList(out);
                        break;
                    case LIST_GROUPS:
                        listGroups(out);
                        break;
                    case CREATE_GROUP:
                        createGroup(message.getGroup(),out);
                        break;
                    case EDIT_GROUP:
                        editGroupName(message.getName(),message.getName2(),message.getUser(),out);
                        break;
                    case GROUP_REQUEST:
                        receiveGroupRequest(message.getId(),message.getUser(),out);
                        break;
                    case GROUP_REQUEST_RESPONSE:
                        receiveGroupRequestResponse(message.getRequest(),message.getUser(),out);
                        break;
                    case LIST_REQUEST:
                        getListRequest(message.getUser(),out);
                        break;
                    case GROUP_EXIT:
                        exitGroup(message.getId(),message.getUser(),out);
                        break;
                    case LIST_MYGROUPS:
                        getMyGroups(out);
                        break;
                    case LIST_MYADMINGROUPS:
                        getMyAdminGroups(message.getUser(),out);
                        break;
                    case DELETE_GROUP:
                        deleteGroup(message.getId(),message.getUser(),out);
                        break;
                    case REMOVE_MEMBER:
                        removeMember(message.getId(),message.getId2(), message.getUser(),out);
                }
            }

        } catch(IOException e){
            e.printStackTrace();
        } catch(ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    public synchronized void checkLogin(boolean login,ObjectOutputStream out,Message message){

        try{
            Message msg;
            if(login == false){

                msg = new Message(Message.Type.LOGIN_FAILED);
            }
            else{
                String name = model.getDbHelper().getName(message.getUser().getUsername());
                int id = model.getDbHelper().getId(message.getUser().getUsername());
                message.getUser().setName(name);
                message.getUser().setId(id);
                message.getUser().setConnected(true);
                ClientDetails client = new ClientDetails(message.getUser(),socket,out);
                model.getClients().add(client);
                msg = new Message(Message.Type.LOGIN_SUCESS,message.getUser());
            }
            out.writeObject(msg);
            out.flush();
        }catch(IOException e){
            e.printStackTrace();
        }

    }

    public synchronized void checkRegister(boolean register,ObjectOutputStream out,Message message){

        try {

            Message msg;
            if(register == false){

                msg = new Message(Message.Type.REGISTER_FAILED);
            }
            else{

                msg = new Message(Message.Type.REGISTER_SUCESS);
            }
            out.writeObject(msg);
            out.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public synchronized void logout(int id,ObjectOutputStream out){

        model.getDbHelper().userDisconnected(id);
        User auxUser = new User(0,null,null,null);
        auxUser.setConnected(false);

        Message msg;

        msg = new Message(Message.Type.LOGOUT_COMPLETE,auxUser);
        msg.getUser().setConnected(auxUser.getState());

        try {

            out.writeObject(msg);
            out.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void changePassword(int id,String password,ObjectOutputStream out){

        model.getDbHelper().changePassword(id,password);

        Message msg;

        msg = new Message(Message.Type.PASSWORD_CHANGED,new User(0,null,password,null));

        try {

            out.writeObject(msg);
            out.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void changeName(int id,String name,ObjectOutputStream out){

        Message msg;

        if(model.getDbHelper().checkName(name)){
            model.getDbHelper().changeName(id,name);
            msg = new Message(Message.Type.NAME_CHANGED_SUCESS,new User(0,null,null,name));
        }
        else{

            msg = new Message(Message.Type.NAME_CHANGED_FAILED,new User(0,null,null,null));
        }


        try {

            out.writeObject(msg);
            out.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void changeUsername(int id,String username,ObjectOutputStream out){

        Message msg;

        if(model.getDbHelper().checkUsername(username)){
            model.getDbHelper().changeUsername(id,username);
            msg = new Message(Message.Type.USERNAME_CHANGED_SUCESS,new User(0,username,null,null));
        }
        else{

            msg = new Message(Message.Type.USERNAME_CHANGED_FAILED,new User(0,null,null,null));
        }

        try {

            out.writeObject(msg);
            out.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public synchronized void searchUser(String username,ObjectOutputStream out){

        Message msg;

        User auxUser = model.getDbHelper().searchUser(username);

        if(auxUser == null){
            msg = new Message(Message.Type.USER_DONT_EXIST);
        }
        else{
            msg = new Message(Message.Type.USER_RECEIVED,new User(auxUser.getId(),auxUser.getUsername(),null,auxUser.getName()));
            msg.getUser().setConnected(auxUser.getState());
        }

        try {

            out.writeObject(msg);
            out.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void sendUsersList(ObjectOutputStream out){

        Message msg;
        ArrayList<User> listUsers;
        listUsers = model.getDbHelper().getAllUsers();
        msg = new Message(Message.Type.LIST_RECEIVED,new User(0,null,null,null));
        msg.setUsersInfo(listUsers);

        try {

            out.writeObject(msg);
            out.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public synchronized void listGroups(ObjectOutputStream out){
        Message msg;
        ArrayList<Group> listGroups = model.getDbHelper().getAllGroups();
        msg = new Message(Message.Type.LIST_GROUPS, listGroups,0);

        try {
            out.writeObject(msg);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void createGroup(Group group, ObjectOutputStream out){
        Message msg;
        if (model.getDbHelper().createGroup(group)) {
            msg = new Message(Message.Type.CREATE_GROUP_COMPLETED);
        } else {
            msg = new Message(Message.Type.CREATE_GROUP_FAILED);
        }
        try {
            out.writeObject(msg);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void editGroupName(String name, String newName, User user, ObjectOutputStream out){
        Message msg;
        if (model.getDbHelper().editGroupName(name,newName,user)) {
            msg = new Message(Message.Type.EDIT_GROUP_NAME_COMPLETED);
        } else {
            msg = new Message(Message.Type.EDIT_GROUP_NAME_FAILED);
        }
        try {
            out.writeObject(msg);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void receiveGroupRequest(int idGroup, User user, ObjectOutputStream out){
        Message msg;
        if (model.getDbHelper().receiveGroupRequest(idGroup,user)) {
            msg = new Message(Message.Type.GROUP_REQUEST_COMPLETED);
        } else {
            msg = new Message(Message.Type.GROUP_REQUEST_FAILED);
        }
        try {
            out.writeObject(msg);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void receiveGroupRequestResponse(Request request, User user, ObjectOutputStream out){
        Message msg;
        if (model.getDbHelper().receiveGroupRequestResponse(request,user)) {
            msg = new Message(Message.Type.GROUP_RESPONSE_COMPLETED);
        } else {
            msg = new Message(Message.Type.GROUP_RESPONSE_FAILED);
        }
        try {
            out.writeObject(msg);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void getListRequest(User user, ObjectOutputStream out){
        Message msg = new Message(Message.Type.LIST_REQUEST,model.getDbHelper().getListRequest(user));
        try {
            out.writeObject(msg);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void exitGroup(int id, User user, ObjectOutputStream out){
        Message msg;
        if (model.getDbHelper().exitGroup(id,user)) {
            msg = new Message(Message.Type.GROUP_EXIT_COMPLETED);
        } else {
            msg = new Message(Message.Type.GROUP_RESPONSE_FAILED);
        }
        try {
            out.writeObject(msg);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void getMyGroups(ObjectOutputStream out){
        Message msg = new Message(Message.Type.LIST_MYGROUPS,model.getDbHelper().getAllGroups(),0);
        try {
            out.writeObject(msg);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void getMyAdminGroups(User user, ObjectOutputStream out){
        Message msg = new Message(Message.Type.LIST_MYGROUPS,model.getDbHelper().getMyGroups(user),0);
        try {
            out.writeObject(msg);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void deleteGroup(int id, User user, ObjectOutputStream out){
        Message msg;
        if (model.getDbHelper().deleteGroup(id,user)) {
            msg = new Message(Message.Type.DELETE_GROUP_COMPLETED);
        } else {
            msg = new Message(Message.Type.DELETE_GROUP_FAILED);
        }
        try {
            out.writeObject(msg);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void removeMember(int groupId,int userId, User user, ObjectOutputStream out){
        Message msg;
        if (model.getDbHelper().removeMember(userId,groupId,user)) {
            msg = new Message(Message.Type.REMOVE_MEMBER_COMPLETED);
        } else {
            msg = new Message(Message.Type.REMOVE_MEMBER_FAILED);
        }
        try {
            out.writeObject(msg);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
