package pt.isec.PD.Client.Network.Tcp;


import pt.isec.PD.Client.Model.Chat;
import pt.isec.PD.Client.Model.Conversation;
import pt.isec.PD.Data.Message;
import pt.isec.PD.Data.User;

import java.io.*;
import java.util.ArrayList;


public class TcpClientManager extends Thread {

    private ObjectInputStream in = null;
    private ObjectOutputStream out = null;
    private boolean register = false;
    private boolean login = false;
    private Chat chat;
    private User userData;


    public TcpClientManager(Chat chat,ObjectInputStream in, ObjectOutputStream out) {

        this.chat = chat;
        this.in = in;
        this.out = out;
    }

    public User getUserData() {
        return userData;
    }

    public void setUserData(User userData) {
        this.userData = userData;
    }

    public void setLogin(boolean login) {
        this.login = login;
    }

    public void setRegister(boolean register) {
        this.register = register;
    }

    public boolean getLogin(){
        return this.login;
    }

    public boolean getRegister(){
        return this.register;
    }



    public void run() {

        try {

            while (true) {

                Object readObject = in.readObject();
                if (readObject instanceof Message) {
                    Message message = (Message) readObject;

                    switch (message.getType()) {
                        case REGISTER_SUCESS:
                            System.out.println("Registration Sucess");
                            register = true;
                            break;
                        case REGISTER_FAILED:
                            System.out.println("Registration Failed");//Depois meter msg de erro para caso o nome ou username ser usado
                            register = false;
                            break;
                        case LOGIN_SUCESS:
                            System.out.println("Login Sucess");
                            setUserData(message.getUser());
                            login = true;
                            break;
                        case LOGIN_FAILED:
                            System.out.println("Login Failed");
                            login = false;
                            break;
                        case LOGOUT_COMPLETE:
                            System.out.println("Logout Sucess");
                            setUserData(message.getUser());
                            break;
                        case PASSWORD_CHANGED:
                            System.out.println("Your Password has been changed");
                            getUserData().setPassword(message.getUser().getPassword());
                            break;
                        case USERNAME_CHANGED_SUCESS:
                            System.out.println("Your Username has been changed");
                            getUserData().setUsername(message.getUser().getUsername());
                            break;
                        case NAME_CHANGED_SUCESS:
                            System.out.println("Your Name has been changed");
                            getUserData().setName(message.getUser().getName());
                            break;
                        case USER_RECEIVED:
                            displayUser(message.getUser().getState(),message.getUser().getId(),message.getUser().getUsername(),message.getUser().getName());
                            break;
                        case LIST_RECEIVED:
                            displayUsersList(message.getUsersInfo());
                            break;
                        case CONTACT_REQUEST:
                            System.out.println("\n" + message.getMessage());
                            break;
                        case LIST_CONTACTS:
                            chat.setContacts(message.getUsersInfo());
                            break;
                        case LIST_PENDING_REQUESTS:
                            chat.setPendingRequests(message.getUsersInfo());
                            break;
                        case LIST_HISTORIC:
                            chat.setHistoric(message.getMessagesInfo());
                            System.out.println(message.getMessagesInfo().get(0).getIdMessage());
                            break;
                        case CONTACT_ACCEPT:
                            System.out.println("\n" + message.getMessage());
                            createHistoric(message.getUser());
                            break;
                        case CONTACT_REFUSED:
                            System.out.println("\n" + message.getMessage());
                            break;
                        case DELETE_CONTACT:
                            System.out.println("\n" + message.getMessage());
                            removeHistoric(message.getUser());//falta remover historico na bd
                            break;
                        case ERROR_MESSAGE:
                            System.out.println("\n" + message.getMessage());
                            break;
                        case SEND_MESSAGE:
                            addMessageToHistoricSender(message);
                            break;
                        case RECEIVE_MESSAGE:
                            System.out.println("\n" + "You have received a message from " + message.getUser().getUsername() + ". Consult your historic");
                            //mostrar msg
                            addMessageToHistoricReceiver(message);
                            break;
                    }
                } else {
                    System.err.println("Received unrecognized data on TCP socket! Ignoring...");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public String checkConnection(boolean connected){

        String state = null;

        if(!connected){
            state = "Offline";
        }
        else{
            state = "Online";
        }

        return state;
    }

    public void displayUser(boolean connection,int id,String username,String name) {

        System.out.println("");
        System.out.println("The User you searched for:");
        System.out.println("");
        String connected = checkConnection(connection);
        System.out.println("--> Id: " + id + " Username: " + username + " Name: " + name + " State: " + connected);

    }

    public void displayUsersList(ArrayList<User> usersList){

        System.out.println("");
        System.out.println("---------- List of Users -----------");

        for (User user : usersList) {
            String connected = checkConnection(user.getState());
            System.out.println("--> Id: " + user.getId() + " Username: " + user.getUsername() + " Name: " + user.getName() + " State: " + connected);
        }
        System.out.println("");
    }




    public void createHistoric(User contact){

        Conversation conversation = new Conversation(userData,contact);

        chat.getConversations().add(conversation);
    }

    public void addMessageToHistoricReceiver(Message msg){

        int i;
        for(i = 0; i < chat.getConversations().size(); i++){
            if(chat.getConversations().get(i).getUser().getUsername().equals(userData.getUsername()) && chat.getConversations().get(i).getContact().getUsername().equals(msg.getUser().getUsername())){

                chat.getConversations().get(i).addMessage(msg);

            }
        }
    }

    public void addMessageToHistoricSender(Message msg){

        int i;
        for(i = 0; i < chat.getConversations().size(); i++){
            if(chat.getConversations().get(i).getUser().getUsername().equals(userData.getUsername()) && chat.getConversations().get(i).getContact().getUsername().equals(msg.getReceiver())){

                chat.getConversations().get(i).addMessage(msg);

            }
        }
    }

    public void removeHistoric(User remove){
        int i;
        for(i = 0; i < chat.getConversations().size(); i++){
            if(chat.getConversations().get(i).getUser().getUsername().equals(userData.getUsername()) && chat.getConversations().get(i).getContact().getUsername().equals(remove.getUsername())){

                chat.getConversations().remove(i);

            }
        }
    }

}
