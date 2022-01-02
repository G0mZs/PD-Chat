package pt.isec.PD.Client.Network.Tcp;


import pt.isec.PD.Client.Model.Chat;
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
                            break;
                        case CREATE_GROUP:
                            System.out.println("\n" + message.getMessage());
                            break;
                        case CONTACT_ACCEPT:
                            System.out.println("\n" + message.getMessage());
                            break;
                        case CONTACT_REFUSED:
                            System.out.println("\n" + message.getMessage());
                            break;
                        case DELETE_CONTACT:
                            System.out.println("\n" + message.getMessage());
                            //falta remover historico na bd
                            break;
                        case ERROR_MESSAGE:
                            System.out.println("\n" + message.getMessage());
                            break;
                        case SEND_MESSAGE:
                            System.out.println("\n" + "Message sent !");
                            break;
                        case RECEIVE_MESSAGE:
                            System.out.println("\n" + "You have received a message from " + message.getUser().getUsername() + ". Consult your historic");
                            break;
                        case DELETE_MESSAGE:
                            System.out.println("\n" + message.getMessage());
                            break;
                        case LIST_GROUPS:
                            chat.setGroups(message.getGroupsInfo());
                            break;
                        case CHANGE_GROUP_NAME:
                            System.out.println("\n" + message.getMessage());
                            break;
                        case GROUP_REQUEST:
                            System.out.println("\n" + message.getMessage());
                            break;
                        case GROUP_ACCEPT:
                            System.out.println("\n" + message.getMessage());
                            break;
                        case GROUP_REFUSE:
                            System.out.println("\n" + message.getMessage());
                            break;
                        case LIST_GROUP_REQUESTS:
                            chat.setPendingGroupRequests(message.getPendingGroupRequests());
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

        String state;

        if(!connected){
            state = "Offline";
        }
        else{
            state = "Online";
        }

        return state;
    }

    public void displayUser(boolean connection,int id,String username,String name) {

        System.out.print("\n");
        System.out.println("The User you searched for:");
        System.out.print("\n");
        String connected = checkConnection(connection);
        System.out.println("--> Id: " + id + " Username: " + username + " Name: " + name + " State: " + connected);

    }

    public void displayUsersList(ArrayList<User> usersList){

        System.out.print("\n");
        System.out.println("---------- List of Users -----------");

        for (User user : usersList) {
            String connected = checkConnection(user.getState());
            System.out.println("--> Id: " + user.getId() + " Username: " + user.getUsername() + " Name: " + user.getName() + " State: " + connected);
        }
        System.out.print("\n");
    }



}
