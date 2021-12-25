package pt.isec.PD.Server.Network.Tcp;

import pt.isec.PD.Data.Message;
import pt.isec.PD.Data.User;
import pt.isec.PD.Server.Database.DbHelper;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Authentication extends Thread{

    private DbHelper dbHelper;
    private Socket socket;
    private ObjectOutputStream out = null;
    private ObjectInputStream in = null;

    public Authentication(Socket socket,DbHelper dbHelper){
        this.socket = socket;
        this.dbHelper = dbHelper;
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run(){

        try {

            while (true) {

                Message message = (Message) in.readObject();
                switch (message.getType()) {
                    case CONNECT_TCP:
                        System.out.println(message.getMessage());
                        break;
                    case LOGIN:
                        checkLogin(dbHelper.Login(message.getUser().getUsername(), message.getUser().getPassword()), out,message);
                        break;
                    case REGISTER:
                        checkRegister(dbHelper.Register(message.getUser().getName(), message.getUser().getUsername(),message.getUser().getPassword()), out,message);
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
                }
            }

        } catch(IOException e){
            e.printStackTrace();
        } catch(ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    public void checkLogin(boolean login,ObjectOutputStream out,Message message){

        try{
            Message msg;
            if(login == false){

                msg = new Message(Message.Type.LOGIN_FAILED, "", null);
            }
            else{
                String name = dbHelper.getName(message.getUser().getUsername());
                int id = dbHelper.getId(message.getUser().getUsername());
                message.getUser().setName(name);
                message.getUser().setId(id);
                message.getUser().setConnected(true);
                msg = new Message(Message.Type.LOGIN_SUCESS, "", message.getUser());
            }
            out.writeObject(msg);
            out.flush();
        }catch(IOException e){
            e.printStackTrace();
        }

    }

    public void checkRegister(boolean register,ObjectOutputStream out,Message message){

        try {

            Message msg;
            if(register == false){

                msg = new Message(Message.Type.REGISTER_FAILED, "", null);
            }
            else{

                msg = new Message(Message.Type.REGISTER_SUCESS, "", null);
            }
            out.writeObject(msg);
            out.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void logout(int id,ObjectOutputStream out){

        dbHelper.userDisconnected(id);
        User auxUser = new User(0,null,null,null);
        auxUser.setConnected(false);

        Message msg;

        msg = new Message(Message.Type.LOGOUT_COMPLETE,null,auxUser);
        msg.getUser().setConnected(auxUser.getState());

        try {

            out.writeObject(msg);
            out.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void changePassword(int id,String password,ObjectOutputStream out){

        dbHelper.changePassword(id,password);

        Message msg;

        msg = new Message(Message.Type.PASSWORD_CHANGED,null,new User(0,null,password,null));

        try {

            out.writeObject(msg);
            out.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void changeName(int id,String name,ObjectOutputStream out){

        Message msg;

        if(dbHelper.checkName(name)){
            dbHelper.changeName(id,name);
            msg = new Message(Message.Type.NAME_CHANGED_SUCESS,null,new User(0,null,null,name));
        }
        else{

            msg = new Message(Message.Type.NAME_CHANGED_FAILED,null,new User(0,null,null,null));
        }


        try {

            out.writeObject(msg);
            out.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void changeUsername(int id,String username,ObjectOutputStream out){

        Message msg;

        if(dbHelper.checkUsername(username)){
            dbHelper.changeUsername(id,username);
            msg = new Message(Message.Type.USERNAME_CHANGED_SUCESS,null,new User(0,username,null,null));
        }
        else{

            msg = new Message(Message.Type.USERNAME_CHANGED_FAILED,null,new User(0,null,null,null));
        }

        try {

            out.writeObject(msg);
            out.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void searchUser(String username,ObjectOutputStream out){

        Message msg;

        User auxUser = dbHelper.searchUser(username);

        if(auxUser == null){
            msg = new Message(Message.Type.USER_DONT_EXIST,null,null);
        }
        else{
            msg = new Message(Message.Type.USER_RECEIVED,null,new User(auxUser.getId(),auxUser.getUsername(),null,auxUser.getName()));
            msg.getUser().setConnected(auxUser.getState());
        }

        try {

            out.writeObject(msg);
            out.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendUsersList(ObjectOutputStream out){

        Message msg;
        ArrayList<User> listUsers;
        listUsers = dbHelper.getAllUsers();
        msg = new Message(Message.Type.LIST_RECEIVED,null,new User(0,null,null,null));
        msg.setUsersInfo(listUsers);

        try {

            out.writeObject(msg);
            out.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
