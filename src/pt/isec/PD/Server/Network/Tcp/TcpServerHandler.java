package pt.isec.PD.Server.Network.Tcp;

import pt.isec.PD.Data.Message;
import pt.isec.PD.Data.User;
import pt.isec.PD.Server.Model.ClientDetails;
import pt.isec.PD.Server.Model.Server;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class TcpServerHandler extends Thread{

    private Server model;
    private Socket socket;


    public TcpServerHandler(Socket socket, Server model){
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
                    case CONTACT_REQUEST:
                        sendContactRequest(message.getMessage(),message.getUser().getUsername(),out);
                        break;
                    case DELETE_CONTACT:
                        removeContact(message.getUser().getId(),message.getUser().getUsername(),out);
                        break;
                    case CONTACT_ACCEPT:
                        acceptContact(message.getUser().getId(),message.getUser().getUsername(),out);
                        break;
                }
            }

        } catch(IOException | ClassNotFoundException e){
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
            if(!register){

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
        int i;

        for(i = 0; i < model.getClients().size(); i++){

            if(model.getClients().get(i).getUser().getId() == id){
                model.getClients().remove(i);
            }

        }

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

    public synchronized void sendContactRequest(String sender,String receiver,ObjectOutputStream out){

        Message msg;
        if(sender.equals(receiver)){
            msg = new Message(Message.Type.CONTACT,"Contact Request Failed ! You cant send a contact request to yourself");
        }
        else {

            if (model.getDbHelper().checkUsername(receiver)) {
                msg = new Message(Message.Type.CONTACT, "Contact Request Failed ! This username is not available");
            } else {

                int idSender = model.getDbHelper().getId(sender);
                int idReceiver = model.getDbHelper().getId(receiver);

                if(model.getDbHelper().checkRequest(idSender,idReceiver)) {

                    model.getDbHelper().createContactRequest(idSender, idReceiver);
                    msg = new Message(Message.Type.CONTACT, "Contact Request Sent !");

                    for (int i = 0; i < model.getClients().size(); i++) {
                        if (model.getClients().get(i).getUser().getUsername().equals(receiver)) {

                            //Mandar pedido de contacto(ContactRequest);
                            User aux = model.getDbHelper().searchUser(sender);
                            Message message = new Message(Message.Type.CONTACT_REQUEST, sender + " sent you a contact Request.", aux);

                                try {
                                    model.getClients().get(i).getOut().writeObject(message);
                                    model.getClients().get(i).getOut().flush();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                        }
                    }
                }
                else{
                    msg = new Message(Message.Type.CONTACT, "There is already a contact request with that data");
                }
            }

        }
        try {
            out.writeObject(msg);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public synchronized void removeContact(int idUser,String username,ObjectOutputStream out){

        Message msg;

        User auxUser = model.getDbHelper().searchUser(username);
        int idContact = auxUser.getId();

        if(auxUser == null){
            msg = new Message(Message.Type.USER_DONT_EXIST);
        }
        else if(idContact == idUser){
            msg = new Message(Message.Type.CONTACT,"You are not apart of your contact list");
        }
        else{
            //Refazer isto tudo

            boolean remove = model.getDbHelper().removeContact(idUser,idContact);

                if(remove){
                    msg = new Message(Message.Type.DELETE_CONTACT,"Contact removed !",new User(auxUser.getId(),auxUser.getUsername(),null,auxUser.getName()));
                    msg.getUser().setConnected(auxUser.getState());
                }
                else{
                    msg = new Message(Message.Type.DELETE_CONTACT,"Contact couldn't be removed because its not on your list !");

                }

        }

        try {

            out.writeObject(msg);
            out.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void acceptContact(int idAccepter,String usernameSender,ObjectOutputStream out){

        User auxSender = model.getDbHelper().searchUser(usernameSender);
        Message msg;
        Message message;
        User auxReceiver = model.getDbHelper().getUser(idAccepter);


        if(auxSender == null){
            msg = new Message(Message.Type.USER_DONT_EXIST);
        }
        else if(auxSender.getId() == auxReceiver.getId()){
            msg = new Message(Message.Type.CONTACT,"You can't accept requests from yourself !");
        }else{

            boolean verifyRequest = model.getDbHelper().checkPendingRequest(auxSender.getId(),idAccepter);

            if(!verifyRequest){
                msg = new Message(Message.Type.CONTACT,"There are no pending requests from this user");
            }
            else {
                msg = new Message(Message.Type.CONTACT_ACCEPT,"Contact Request Accepted",auxSender);
                for(int i = 0; i < model.getClients().size(); i++){
                    if(auxSender.getId() == model.getClients().get(i).getUser().getId()){

                        message = new Message(Message.Type.CONTACT_ACCEPT,auxReceiver.getUsername() + " has accepted your contact request !",auxReceiver);

                        try {

                            model.getClients().get(i).getOut().writeObject(message);
                            model.getClients().get(i).getOut().flush();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
        }

        try {

            out.writeObject(msg);
            out.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
