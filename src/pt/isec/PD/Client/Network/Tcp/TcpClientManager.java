package pt.isec.PD.Client.Network.Tcp;


import pt.isec.PD.Data.Group;
import pt.isec.PD.Data.Message;
import pt.isec.PD.Data.User;

import java.io.*;
import java.util.ArrayList;


public class TcpClientManager extends Thread {

    private ObjectInputStream in = null;
    private ObjectOutputStream out = null;
    private boolean register = false;
    private boolean login = false;
    private boolean taskCompleted = false;
    private User userData;

    public TcpClientManager(ObjectInputStream in, ObjectOutputStream out) {

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

    public boolean getTaskCompleted(){
        if(taskCompleted){
            taskCompleted=false;
            return  true;
        }
        return  taskCompleted;
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
                        case USERNAME_CHANGED_FAILED:
                            System.out.println("This Username is already in use!");
                            break;
                        case NAME_CHANGED_SUCESS:
                            System.out.println("Your Name has been changed");
                            getUserData().setName(message.getUser().getName());
                            break;
                        case NAME_CHANGED_FAILED:
                            System.out.println("This Name is already in use");
                            break;
                        case USER_DONT_EXIST:
                            System.out.println("This Username doesn't exist");
                            break;
                        case USER_RECEIVED:
                            displayUser(message.getUser().getState(),message.getUser().getId(),message.getUser().getUsername(),message.getUser().getName());
                            break;
                        case LIST_RECEIVED:
                            displayUsersList(message.getUsersInfo());
                            break;
                        case LIST_GROUPS:
                            displayGroupsList(message.getListGroups());
                            break;
                        case CREATE_GROUP_COMPLETED:
                            System.out.println("Group created");
                            taskCompleted=true;
                            break;
                        case CREATE_GROUP_FAILED:
                            System.out.println("Group cannot be created");
                            taskCompleted=false;
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

        if(connected == false){
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

        for(int i = 0; i < usersList.size(); i++){
            String connected = checkConnection(usersList.get(i).getState());
            System.out.println("--> Id: " + usersList.get(i).getId() + " Username: " + usersList.get(i).getUsername() + " Name: " + usersList.get(i).getName() + " State: " + connected);
        }
        System.out.println("");
    }

    public void displayGroupsList(ArrayList<Group> groupList){

        System.out.println("");
        System.out.println("---------- List of Groups -----------");

        if(groupList.isEmpty())
            System.out.println("There are no groups");
        for(Group group : groupList){
            System.out.println("Name: "+group.getName());
            for (User user : group.getMembers())
                if(user.getId()==group.getAdmnistrator().getId())
                    System.out.println("Admin - "+user.getName());
                else
                    System.out.println("- "+user.getName());
            System.out.println("");
        }

        System.out.println("");
    }
}
