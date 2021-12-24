package pt.isec.PD.Client.TextInferface;

import pt.isec.PD.Client.Model.Client;

import java.io.IOException;
import java.util.Scanner;

public class ClientTextInterface {

    private  Client client;
    private final Scanner s;
    boolean exit;

    public ClientTextInterface(Client client){
        this.client = client;
        this.s = new Scanner(System.in, "UTF-8");
    }

    public void uiMain(){

        exit = false;
        int value;
        client.startConnection();

        while(!exit){

            System.out.println("");
            System.out.println("------ User Main Menu ------");
            System.out.println("");
            System.out.println("1 --> Login");
            System.out.println("2 --> Register");
            System.out.println("3 --> Exit");
            System.out.println("");

            System.out.print("Answer: ");
            while (!s.hasNextInt()) {
                s.next();
            }

            value = s.nextInt();



            switch (value){
                case 1:
                    uiLogin();
                    break;
                case 2:
                    uiRegister();
                    break;
                case 3:
                    exit = true;
                    //Send message to Grds and Server
                    break;
                default:
                    System.out.println("Invalid option");
            }

        }
    }

    public void uiLogin(){

        do {

            Scanner sc = new Scanner(System.in);

            String username = null, password = null;

            System.out.println("");
            System.out.println("------ Login Into Your Account ------");
            System.out.println("");

            System.out.println("Enter your username: ");

            username = sc.nextLine();

            System.out.println("Enter your password: ");

            password = sc.nextLine();

            client.sendLoginData(username, password);

            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }while(client.getLoginState() == false);

        client.setInfoUser();
        uiUser();

    }

    public void uiRegister() {

        do {

            Scanner sc = new Scanner(System.in);
            String username = null, password = null, name = null;

            System.out.println("");
            System.out.println("------ Register as a new user ------");
            System.out.println("");

            System.out.println("Enter your name: ");

            name = sc.nextLine();

            System.out.println("Enter your username: ");

            username = sc.nextLine();

            System.out.println("Enter your password: ");

            password = sc.nextLine();


            client.sendRegisterData(name, username, password);

            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }while (client.getRegisterState() == false);


        client.setRegisterState(false);

    }

    public void uiUser(){

        int value;


        while(client.getLoginState()) {

            System.out.println("");
            System.out.println("Welcome " + client.getUser().getName() + " " + client.getUser().getId() + " to your main menu");
            System.out.println("");
            System.out.println("Choose an option below");

            System.out.println("1 --> Edit Personal Data");
            System.out.println("2 --> List and Search Users");
            System.out.println("3 --> Contacts Menu");
            System.out.println("4 --> Groups Menu");
            System.out.println("5 --> Logout");
            System.out.println("");

            System.out.print("Answer: ");
            while (!s.hasNextInt()) {
                s.next();
            }

            value = s.nextInt();

                switch (value) {
                    case 1:
                        uiPersonalData();
                        break;
                    case 2:
                        //uiSearchUsers();
                        break;
                    case 3:
                        //uiContacts();
                        break;
                    case 4:
                        //uiGroups();
                        break;
                    case 5:
                        client.setLoginState(false);
                        client.logout(client.getUser().getId());
                        break;
                    default:
                        System.out.println("Invalid option");
                }
        }
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        client.setInfoUser();

    }

    public void uiPersonalData(){

        int value;
        boolean leave = false;

        while(!leave) {
            System.out.println("");
            System.out.println("Name: " + client.getUser().getName() + " Username: " + client.getUser().getUsername() + " Password: " + client.getUser().getPassword());
            System.out.println("Edit Personal Data");
            System.out.println("");
            System.out.println("1 --> Change Name");
            System.out.println("2 --> Change Username");
            System.out.println("3 --> Change Password");
            System.out.println("4 --> Return to Main Menu");

            System.out.println("");

            System.out.print("Answer: ");
            while (!s.hasNextInt()) {
                s.next();
            }

            value = s.nextInt();

            switch (value) {
                case 1:
                    //uiChangeName()
                    break;
                case 2:
                    //uiChangeUsername();
                    break;
                case 3:
                    //uiChangePassword();
                    break;
                case 4:
                    leave = true;
                    break;
                default:
                    System.out.println("Invalid option");
            }
        }

    }

    public void uiChangeName(){

        Scanner sc = new Scanner(System.in);
        String name = null;

        System.out.println("");
        System.out.println("Enter your new Name: ");

        name = sc.nextLine();

        //client.changeName(name);

    }

    public void uiChangeUsername(){

        Scanner sc = new Scanner(System.in);
        String username = null;

        System.out.println("");
        System.out.println("Enter your new Username: ");

        username = sc.nextLine();

        //client.changeUsername(username);

    }

    public void uiChangePassword(){

        Scanner sc = new Scanner(System.in);
        String password = null;

        System.out.println("");
        System.out.println("Enter your new Password: ");

        password = sc.nextLine();

        //client.changePassword(password);

    }


}