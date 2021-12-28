package pt.isec.PD.Server.Network.Tcp;

import pt.isec.PD.Data.Constants;
import pt.isec.PD.Data.Contact;
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
            boolean running = true;

            while (running) {

                Message message = (Message) in.readObject();
                switch (message.getType()) {
                    case CONNECT_TCP:
                        System.out.println(message.getMessage());
                        break;
                    case LOGIN:
                        for(int i = 0; i < model.getCommunication().getActiveServers().size(); i++){
                            System.out.println(model.getCommunication().getActiveServers().get(i));
                        }
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
                    case CONTACT_REFUSED:
                        refuseContactRequest(message.getUser().getId(),message.getUser().getUsername(),out);
                        break;
                    case SERVER_CONTACT_REQUEST:
                        sendRequestToClient(message.getMessage(), message.getContactRequest());
                        running = false;
                        break;
                    case SERVER_ACCEPT_CONTACT:
                        sendAcceptContactToClient(message.getMessage(),message.getContactRequest());
                        running = false;
                        break;
                    case SERVER_DELETE_CONTACT:
                        sendDeleteContactToClient(message.getMessage(),message.getContactRequest());
                        running = false;
                        break;
                    case SERVER_REFUSE_CONTACT:
                        sendRefusedContactToClient(message.getMessage(),message.getUser());
                        running = false;
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
            if(!login){

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
                    User auxSender = model.getDbHelper().searchUser(sender);
                    User auxReceiver = model.getDbHelper().searchUser(receiver);
                    Contact request = new Contact(auxSender,auxReceiver);



                    for (int i = 0; i < model.getClients().size(); i++) {
                        if (model.getClients().get(i).getUser().getUsername().equals(receiver)) {


                            Message message = new Message(Message.Type.CONTACT_REQUEST, sender + " sent you a contact Request.", request.getSender());

                                try {
                                    model.getClients().get(i).getOut().writeObject(message);
                                    model.getClients().get(i).getOut().flush();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                        }
                    }

                    contactServers(new Message(Message.Type.SERVER_CONTACT_REQUEST, sender + " sent you a contact Request.", request));

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

        User contactUser = model.getDbHelper().searchUser(username);
        User user = model.getDbHelper().getUser(idUser);

        if(contactUser == null){
            msg = new Message(Message.Type.USER_DONT_EXIST);
        }
        else if(contactUser.getId() == idUser){
            msg = new Message(Message.Type.CONTACT,"You are not apart of your contact list");
        }
        else{

            boolean remove = model.getDbHelper().removeContact(idUser,contactUser.getId());

                if(remove){
                    msg = new Message(Message.Type.DELETE_CONTACT,"Contact removed !",new User(contactUser.getId(),contactUser.getUsername(),null,contactUser.getName()));
                    msg.getUser().setConnected(contactUser.getState());
                    Contact contact = new Contact(user,contactUser);
                    Message message = new Message(Message.Type.DELETE_CONTACT,"You have been removed from " + user.getUsername() + " contact list",user);

                    for(int i = 0; i < model.getClients().size(); i++){
                        if(username.equals(model.getClients().get(i).getUser().getUsername())){

                            try {
                                model.getClients().get(i).getOut().writeObject(message);
                                model.getClients().get(i).getOut().flush();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    contactServers(new Message(Message.Type.SERVER_DELETE_CONTACT,"You have been removed from " + user.getUsername() + " contact list",contact));
                }
                else{
                    msg = new Message(Message.Type.CONTACT,"Contact couldn't be removed because its not on your list !");

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
        User auxReceiver = model.getDbHelper().getUser(idAccepter);
        Message msg;
        Message message;

        if(auxSender == null){
            msg = new Message(Message.Type.USER_DONT_EXIST);
        }
        else if(auxSender.getId() == auxReceiver.getId()){
            msg = new Message(Message.Type.CONTACT,"You can't accept requests from yourself !");
        }else{

            boolean verifyRequest = model.getDbHelper().requestTrue(auxSender.getId(),idAccepter);

            if(!verifyRequest){
                msg = new Message(Message.Type.CONTACT,"There are no pending requests from this user");
            }
            else {
                Contact request = new Contact(auxSender,auxReceiver);
                msg = new Message(Message.Type.CONTACT_ACCEPT,"Contact Request Accepted",request.getSender());
                for(int i = 0; i < model.getClients().size(); i++){
                    if(auxSender.getUsername().equals(model.getClients().get(i).getUser().getUsername())){

                        message = new Message(Message.Type.CONTACT_ACCEPT,auxReceiver.getUsername() + " has accepted your contact request !",request.getReceiver());

                        try {
                            model.getClients().get(i).getOut().writeObject(message);
                            model.getClients().get(i).getOut().flush();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                contactServers(new Message(Message.Type.SERVER_ACCEPT_CONTACT,auxReceiver.getUsername() + " has accepted your contact request !",request));

            }
        }
        try {
            out.writeObject(msg);
            out.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public synchronized void refuseContactRequest(int idRefuser,String Sender,ObjectOutputStream out){

        User auxSender = model.getDbHelper().searchUser(Sender);

        User auxRefuser = model.getDbHelper().getUser(idRefuser);
        Message msg;
        Message message;

        if(auxSender == null){
            msg = new Message(Message.Type.USER_DONT_EXIST);
        }
        else if(auxSender.getId() == auxRefuser.getId()){
            msg = new Message(Message.Type.CONTACT,"You can't refuse requests from yourself !");
        }else{

            boolean verifyRequest = model.getDbHelper().requestFalse(auxSender.getId(),idRefuser);

            if(!verifyRequest){
                msg = new Message(Message.Type.CONTACT,"There are no pending requests from this user");
            }
            else {
                msg = new Message(Message.Type.CONTACT_REFUSED,"Contact Request Refused",auxSender);
                for(int i = 0; i < model.getClients().size(); i++){
                    if(auxSender.getUsername().equals(model.getClients().get(i).getUser().getUsername())){

                        message = new Message(Message.Type.CONTACT,auxRefuser.getUsername() + " has refused your contact request !");

                        try {
                            model.getClients().get(i).getOut().writeObject(message);
                            model.getClients().get(i).getOut().flush();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                contactServers(new Message(Message.Type.SERVER_REFUSE_CONTACT,auxRefuser.getUsername() + " has refused your contact request !",auxSender));

            }
        }
        try {
            out.writeObject(msg);
            out.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void contactServers(Message msg){

        for(int i = 0; i < model.getCommunication().getActiveServers().size(); i++){
            try {
                InetAddress add = InetAddress.getByName(Constants.SERVER_ADDRESS);

                Socket socket = new Socket(add, model.getCommunication().getActiveServers().get(i));

                ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream input = new ObjectInputStream(socket.getInputStream());


                output.writeObject(msg);
                output.flush();

                socket.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendRequestToClient(String msg, Contact request){

        for (int i = 0; i < model.getClients().size(); i++) {

            if (model.getClients().get(i).getUser().getUsername().equals(request.getReceiver().getUsername())) {


                Message message = new Message(Message.Type.CONTACT_REQUEST, msg, request.getSender());

                try {
                    model.getClients().get(i).getOut().writeObject(message);
                    model.getClients().get(i).getOut().flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void sendDeleteContactToClient(String msg,Contact request){
        for (int i = 0; i < model.getClients().size(); i++) {

            if (model.getClients().get(i).getUser().getUsername().equals(request.getReceiver().getUsername())) {


                Message message = new Message(Message.Type.DELETE_CONTACT, msg, request.getSender());

                try {
                    model.getClients().get(i).getOut().writeObject(message);
                    model.getClients().get(i).getOut().flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void sendAcceptContactToClient(String msg, Contact request){

        for (int i = 0; i < model.getClients().size(); i++) {

            if (model.getClients().get(i).getUser().getUsername().equals(request.getSender().getUsername())) {

                Message message = new Message(Message.Type.CONTACT_ACCEPT, msg, request.getReceiver());

                try {
                    model.getClients().get(i).getOut().writeObject(message);
                    model.getClients().get(i).getOut().flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void sendRefusedContactToClient(String msg,User sender){

        for (int i = 0; i < model.getClients().size(); i++) {

            if (model.getClients().get(i).getUser().getUsername().equals(sender.getUsername())) {

                Message message = new Message(Message.Type.CONTACT, msg);

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
