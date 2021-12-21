package pt.isec.PD.Client;

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

        System.out.println("");
        System.out.println("------ User Main Menu ------");
        System.out.println("");
        System.out.println("1 --> Login");
        System.out.println("2 --> Register");
        System.out.println("3 --> Exit");

        while (!s.hasNextInt()) {
            s.next();
        }

        value = s.nextInt();

        while(!exit){

            switch (value){
                case 1:
                    //uiLogin()
                    break;
                case 2:
                    //uiRegister()
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


        String username = null,password = null;

        do {
            System.out.println("");
            System.out.println("------ Login Menu ------");
            System.out.println("");

            System.out.println("Enter your username: ");

            username = s.nextLine();

            System.out.println("Enter your password: ");

            password = s.nextLine();

        }while(client.checkLogin(username,password) == false);

    }



}