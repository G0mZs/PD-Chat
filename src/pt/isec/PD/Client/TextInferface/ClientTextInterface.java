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
            System.out.println("1 --> Send Contact Request");
            System.out.println("2 --> List Contacts");
            System.out.println("3 --> Delete Contact");
            System.out.println("4 --> Pending Contact Requests");
            System.out.println("0 --> Return to Main Menu");

            System.out.print("\n");

            System.out.print("Answer: ");
            while (!s.hasNextInt()) {
                s.next();
            }

            value = s.nextInt();

            switch (value) {
                case 1 -> uiContactRequest();
                case 2 -> uiListContacts();
                case 3 -> uiDeleteContact();
                case 4 -> uiPendingRequests();
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

    public void uiGroups(){

        int value;
        boolean leave = false;

        while(!leave) {

            System.out.print("\n");
            System.out.println("---------- Groups ----------");
            System.out.print("\n");
            System.out.println("1 --> List your Groups");
            System.out.println("2 --> Create Group");
            System.out.println("3 --> Edit Group"); // menu com alterar o nome de um grupo,expulsar membros e extinguir grupo
            System.out.println("4 --> Join Group");
            System.out.println("0 --> Return to Main Menu");

            System.out.print("\n");

            System.out.print("Answer: ");
            while (!s.hasNextInt()) {
                s.next();
            }

            value = s.nextInt();

            switch (value) {
                case 1:
                    //uiListGroups();
                    break;
                case 2:
                    //uiCreateGroup();
                    break;
                case 3:
                    //uiEditGroup();
                    break;
                case 4:
                    //uiJoinGroup();
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

    public void uiContactRequest(){

        String username;
        Scanner sc = new Scanner(System.in);

        System.out.print("\n");
        System.out.println("Enter the username you want to send a contact request: ");
        username = sc.nextLine();

        handler.sendContactRequest(username);


    }

    public void uiListContacts(){

        for(int i = 0; i < handler.getContacts().size();i++){
            System.out.print("\n");
            System.out.println("----------- Lista de Contactos ----------");
            System.out.print("\n");
            String connected = handler.returnState(handler.getContacts().get(i).getState());
            System.out.println("Contacto --> Nome: " + handler.getContacts().get(i).getName() + " Username: " + handler.getContacts().get(i).getUsername() + " State: " + connected);
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

        //handler.refuseContactRequest(username);
    }

    public void uiListPendingRequests(){

        if(handler.getPendingRequests().size() == 0){
            System.out.println("You have no Pending Contact Requests");
        }
        else{
            for(int i = 0; i < handler.getPendingRequests().size(); i++){
                System.out.println("Contact Request from --> [" + handler.getPendingRequests().get(i).getUsername() + "]");
            }
        }
    }


}