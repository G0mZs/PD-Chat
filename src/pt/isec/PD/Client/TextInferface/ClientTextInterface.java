package pt.isec.PD.Client.TextInferface;

import pt.isec.PD.Client.Network.CommunicationHandler;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class ClientTextInterface {

    private final CommunicationHandler handler;
    private final Scanner s;
    boolean exit;

    public ClientTextInterface(CommunicationHandler handler){
        this.handler = handler;
        this.s = new Scanner(System.in, StandardCharsets.UTF_8);
    }

    public void uiMain(){

        exit = false;
        int value;
        handler.startUDP();
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        handler.startConnection();

        while(!exit){

            System.out.print("\n");
            System.out.println("------ User Main Menu ------");
            System.out.print("\n");
            System.out.println("1 --> Login");
            System.out.println("2 --> Register");
            System.out.println("0 --> Exit");
            System.out.print("\n");

            System.out.print("Answer: ");
            while (!s.hasNextInt()) {
                s.next();
            }

            value = s.nextInt();

            switch (value) {
                case 1 -> uiLogin();
                case 2 -> uiRegister();
                case 0 -> exit = true;  //Send message to Grds and Server
                default -> System.out.println("Invalid option");
            }

        }
    }

    public void uiLogin(){

        do {

            Scanner sc = new Scanner(System.in);

            String username,password;

            System.out.print("\n");
            System.out.println("------ Login Into Your Account ------");
            System.out.print("\n");

            System.out.println("Enter your username: ");

            username = sc.nextLine();

            System.out.println("Enter your password: ");

            password = sc.nextLine();

            handler.sendLoginData(username, password);

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }while(!handler.getLoginState());

        handler.setInfoUser();
        uiUser();

    }

    public void uiRegister() {

        do {

            Scanner sc = new Scanner(System.in);
            String username,password,name;

            System.out.print("\n");
            System.out.println("------ Register as a new user ------");
            System.out.print("\n");

            System.out.println("Enter your name: ");

            name = sc.nextLine();

            System.out.println("Enter your username: ");

            username = sc.nextLine();

            System.out.println("Enter your password: ");

            password = sc.nextLine();


            handler.sendRegisterData(name, username, password);

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }while (!handler.getRegisterState());


        handler.setRegisterState(false);

    }

    public void uiUser(){

        int value;


        while(handler.getLoginState()) {

            System.out.print("\n");
            System.out.println("Welcome [" + handler.getUser().getName() + "] to your application");
            System.out.print("\n");
            System.out.println("Choose an option below");

            System.out.println("1 --> Edit Personal Data");
            System.out.println("2 --> List and Search Users");
            System.out.println("3 --> Contacts Menu");
            System.out.println("4 --> Groups Menu");
            System.out.println("0 --> Logout");
            System.out.print("\n");

            System.out.print("Answer: ");
            while (!s.hasNextInt()) {
                s.next();
            }

            value = s.nextInt();

            switch (value) {
                case 1 -> uiPersonalData();
                case 2 -> uiSearchUsers();
                case 3 -> uiContacts();
                case 4 -> uiGroups();
                case 0 -> {
                    handler.setLoginState(false);
                    handler.logout(handler.getUser().getId());
                }
                default -> System.out.println("Invalid option");
            }
        }
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        handler.setInfoUser();

    }

    public void uiPersonalData(){

        int value;
        boolean leave = false;

        while(!leave) {
            System.out.print("\n");
            System.out.println("Edit Personal Data");
            System.out.println("Name: " + handler.getUser().getName() + " Username: " + handler.getUser().getUsername() + " Password: " + handler.getUser().getPassword());
            System.out.print("\n");
            System.out.println("1 --> Change Name");
            System.out.println("2 --> Change Username");
            System.out.println("3 --> Change Password");
            System.out.println("0 --> Return to Main Menu");

            System.out.print("\n");

            System.out.print("Answer: ");
            while (!s.hasNextInt()) {
                s.next();
            }

            value = s.nextInt();

            switch (value) {
                case 1 -> uiChangeName();
                case 2 -> uiChangeUsername();
                case 3 -> uiChangePassword();
                case 0 -> leave = true;
                default -> System.out.println("Invalid option");
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public void uiChangeName(){

        Scanner sc = new Scanner(System.in);
        String name;

        System.out.print("\n");
        System.out.println("Enter your new Name: ");

        name = sc.nextLine();

        handler.changeName(name);

    }

    public void uiChangeUsername(){

        Scanner sc = new Scanner(System.in);
        String username;

        System.out.print("\n");
        System.out.println("Enter your new Username: ");

        username = sc.nextLine();

        handler.changeUsername(username);

    }

    public void uiChangePassword(){

        Scanner sc = new Scanner(System.in);
        String password;

        System.out.print("\n");
        System.out.println("Enter your new Password: ");

        password = sc.nextLine();

        handler.changePassword(password);

    }

    public void uiSearchUsers(){

        int value;
        boolean leave = false;

        while(!leave) {
            System.out.print("\n");
            System.out.println("Choose a option below");
            System.out.print("\n");
            System.out.println("1 --> List Users");
            System.out.println("2 --> Search User");
            System.out.println("0 --> Return to Main Menu");

            System.out.print("\n");

            System.out.print("Answer: ");
            while (!s.hasNextInt()) {
                s.next();
            }

            value = s.nextInt();

            switch (value) {
                case 1 -> uiListUsers();
                case 2 -> uiSearchUser();
                case 0 -> leave = true;
                default -> System.out.println("Invalid option");
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void uiSearchUser(){

        Scanner sc = new Scanner(System.in);
        String username;

        System.out.print("\n");
        System.out.println("Enter the username of the user you want to search: ");

        username = sc.nextLine();

        handler.searchUser(username);

    }

    public void uiListUsers(){

        handler.listUsers();
    }

    public void uiContacts(){

        int value;
        boolean leave = false;

        while(!leave) {

            System.out.print("\n");
            System.out.println("---------- Contacts ----------");
            System.out.print("\n");
            System.out.println("1 --> Pending Contact Requests");
            System.out.println("2 --> Send Contact Request");
            System.out.println("3 --> Send Message");
            System.out.println("4 --> Send File");
            System.out.println("5 --> Delete Contact");
            System.out.println("6 --> Delete Message/File Notification");
            System.out.println("7 --> List Contacts");
            System.out.println("8 --> List Historic");
            System.out.println("0 --> Return to Main Menu");

            System.out.print("\n");

            System.out.print("Answer: ");
            while (!s.hasNextInt()) {
                s.next();
            }

            value = s.nextInt();

            switch (value) {
                case 1 -> uiPendingRequests();
                case 2 -> uiContactRequest();
                case 3 -> uiSendContactMessage();
                case 4 -> uiSendFile();
                case 5 -> uiDeleteContact();
                case 6 -> uiDeleteMessages();
                case 7 -> uiListContacts();
                case 8 -> uiChooseHistoric();
                case 0 -> leave = true;
                default -> System.out.println("Invalid option");
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }



    public void uiContactRequest(){

        String username;
        Scanner sc = new Scanner(System.in);

        System.out.print("\n");
        System.out.println("Enter the username you want to send a contact request: ");
        username = sc.nextLine();

        handler.sendContactRequest(username);


    }

    public void uiListContacts(){

        handler.listContacts();

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.print("\n");
        System.out.println("----------- Lista de Contactos ----------");
        System.out.print("\n");

        for(int i = 0; i < handler.getChat().getContacts().size();i++){

            String connected = handler.returnState(handler.getChat().getContacts().get(i).getState());
            System.out.println("Contacto --> Nome: " + handler.getChat().getContacts().get(i).getName() + " Username: " + handler.getChat().getContacts().get(i).getUsername() + " State: " + connected);
        }
    }

    public void uiDeleteContact(){

        String username;
        Scanner sc = new Scanner(System.in);

        System.out.print("\n");
        System.out.println("Enter the username of the contact you want to delete: ");
        username = sc.nextLine();

        handler.deleteContact(username);

    }

    public void uiPendingRequests(){

        handler.listPendingRequests();

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.print("\n");
        System.out.println("---------- Pending Requests ----------");
        System.out.print("\n");
        uiListPendingRequests();

        int value;
        boolean leave = false;

        while(!leave) {

            System.out.print("\n");
            System.out.println("1 --> Accept Request");
            System.out.println("2 --> Refuse Request");
            System.out.println("0 --> Return to Main Menu");

            System.out.print("\n");

            System.out.print("Answer: ");
            while (!s.hasNextInt()) {
                s.next();
            }

            value = s.nextInt();

            switch (value) {
                case 1 -> uiAcceptRequest();
                case 2 -> uiRefuseRequest();
                case 0 -> leave = true;
                default -> System.out.println("Invalid option");
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public void uiAcceptRequest(){

        String username;
        Scanner sc = new Scanner(System.in);

        System.out.print("\n");
        System.out.println("Enter the username of the contact request sender: ");
        username = sc.nextLine();

        handler.acceptContactRequest(username);
    }

    public void uiRefuseRequest(){
        String username;
        Scanner sc = new Scanner(System.in);

        System.out.print("\n");
        System.out.println("Enter the username of the contact request sender: ");
        username = sc.nextLine();

        handler.refuseContactRequest(username);
    }

    public void uiListPendingRequests(){

        if(handler.getChat().getPendingRequests().size() == 0){
            System.out.println("You have no Pending Contact Requests");
        }
        else{
            for(int i = 0; i < handler.getChat().getPendingRequests().size(); i++){
                System.out.println("Contact Request from --> [" + handler.getChat().getPendingRequests().get(i).getUsername() + "]");
            }
        }
    }


    private void uiDeleteMessages() {

        int id;
        Scanner sc = new Scanner(System.in);

        System.out.print("\n");
        System.out.println("Enter the id of the message you want to delete: ");
        while (!sc.hasNextInt()) {
            sc.next();
        }

        id = sc.nextInt();

        handler.deleteMessage(id);
    }


    private void uiChooseHistoric() {

        String username;
        Scanner sc = new Scanner(System.in);

        System.out.print("\n");
        System.out.println("Enter the username of the contact you want to see the historic for: ");
        username = sc.nextLine();


        uiListHistoric(username);

    }

    private void uiListHistoric(String username){


        handler.listHistoric(username);

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        System.out.print("\n");
        System.out.println("---------- Message Historic For " + username + " ----------");
        System.out.print("\n");

        int i;
        for (i = 0; i < handler.getChat().getHistoric().size(); i++) {
            System.out.println("[" + handler.getChat().getHistoric().get(i).getIdMessage() + "][" + handler.getChat().getHistoric().get(i).getState() + "][" + handler.getChat().getHistoric().get(i).getDateTime() + "] ---> " + handler.getChat().getHistoric().get(i).getUser().getUsername() + ": " + handler.getChat().getHistoric().get(i).getMessage());
        }


        handler.updateHistoric(username);

    }



    private void uiSendFile() {
    }

    public void uiSendContactMessage(){

        String username,content;
        Scanner sc = new Scanner(System.in);

        System.out.print("\n");
        System.out.println("Enter the username of the contact you want to send a message: ");
        username = sc.nextLine();

        System.out.print("\n");
        System.out.println("Enter the content of the message: ");
        content = sc.nextLine();


        handler.sendContactMessage(username,content);

    }

    public void uiGroups(){

        int value;
        boolean leave = false;

        while(!leave) {

            System.out.print("\n");
            System.out.println("---------- Groups ----------");
            System.out.print("\n");
            System.out.println("1 --> Pending Group Requests"); //Fazer
            System.out.println("2 --> Send Group Request");
            System.out.println("3 --> Send Message"); //Fazer
            System.out.println("4 --> Send File"); //Fazer
            System.out.println("5 --> Create Group");
            System.out.println("6 --> Edit Group"); // menu com alterar o nome de um grupo,expulsar membros e extinguir grupo // Feito
            System.out.println("7 --> List Groups");
            System.out.println("8 --> List Historic"); // Fazer
            System.out.println("9 --> Delete Message/File Notification"); // Fazer
            System.out.println("10 -> Leave Group");
            System.out.println("0 --> Return to Main Menu");

            System.out.print("\n");

            System.out.print("Answer: ");
            while (!s.hasNextInt()) {
                s.next();
            }

            value = s.nextInt();

            switch (value) {
                case 1:
                    //2
                    break;
                case 2:
                    uiSendGroupRequest();
                    break;
                case 3:
                    //3
                    break;
                case 4:
                    //ultimo
                    break;
                case 5:
                    uiCreateGroup();
                    break;
                case 6:
                    uiEditGroup();
                    break;
                case 7:
                    uiListGroups();
                    break;
                case 8:
                    //4
                    break;
                case 9:
                    //5
                    break;
                case 10:
                    //Eliminar o pedido aceite de adesão ao grupo, e eliminar as suas mensagens/listas do seu histórico
                    break;
                case 0:
                    leave = true;
                    break;
                default:
                    System.out.println("Invalid option");
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void uiListGroups(){

        handler.getGroups();

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.print("\n");
        System.out.println("------------------- Groups List ------------------");
        System.out.print("\n");

        int i;
        for(i = 0; i < handler.getChat().getGroups().size(); i++){
            System.out.println(" --> Group: " + handler.getChat().getGroups().get(i).getId() + " Name: " + handler.getChat().getGroups().get(i).getName() + " Admin: " + handler.getChat().getGroups().get(i).getAdmnistrator().getUsername());

            int j;
            System.out.println(" --- Members for Group " + handler.getChat().getGroups().get(i).getId() + " ---");
            System.out.println("     --> Username: " + handler.getChat().getGroups().get(i).getAdmnistrator().getUsername() + " Name: " + handler.getChat().getGroups().get(i).getAdmnistrator().getName() + " -------> [Admin]");
            for(j = 0; j < handler.getChat().getGroups().get(i).getMembers().size(); j++){
                System.out.println("     --> Username: " + handler.getChat().getGroups().get(i).getMembers().get(j).getUsername() + " Name: " + handler.getChat().getGroups().get(i).getMembers().get(j).getName() + " -------> [Member]");
            }

            System.out.print("\n");
        }


    }

    public void uiCreateGroup(){

        String name;
        Scanner sc = new Scanner(System.in);

        System.out.print("\n");
        System.out.println("Enter the [name] of the group you want to create: ");
        name = sc.nextLine();

        handler.createGroup(name);
    }

    public void uiEditGroup(){

        int value;
        boolean leave = false;

        while(!leave) {


            System.out.println("---------- Edit Group ----------");
            System.out.print("\n");
            System.out.println("1 --> Change Group Name"); //Fazer
            System.out.println("2 --> Kick Members"); //Fazer
            System.out.println("3 --> Delete Group");
            System.out.println("4 --> Return to Groups Menu");

            System.out.print("\n");

            System.out.print("Answer: ");
            while (!s.hasNextInt()) {
                s.next();
            }

            value = s.nextInt();

            switch (value) {
                case 1:
                    uiChangeGroupName();
                    break;
                case 2:
                    //uiKickMember();
                    break;
                case 3:
                    //uiDeleteGroup();
                case 4:
                    leave = true;
                    break;
                default:
                    System.out.println("Invalid option");
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void uiChangeGroupName(){

        String name,newName;
        Scanner sc = new Scanner(System.in);

        System.out.print("\n");
        System.out.println("Enter the name of the group you want to change the name: ");
        name = sc.nextLine();

        System.out.print("\n");
        System.out.println("Enter the [new name] of the group you want to create: ");
        newName = sc.nextLine();

        handler.changeGroupName(name,newName);

    }

    public void uiSendGroupRequest(){

        int id;
        Scanner sc = new Scanner(System.in);

        System.out.print("\n");
        System.out.println("Enter the id/number of the group you want to send a request: ");
        while (!sc.hasNextInt()) {
            sc.next();
        }

        id = sc.nextInt();

        handler.sendGroupRequest(id);

    }


}