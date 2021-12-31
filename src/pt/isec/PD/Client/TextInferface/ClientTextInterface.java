package pt.isec.PD.Client.TextInferface;

import pt.isec.PD.Client.Model.Client;
import pt.isec.PD.Data.Group;
import pt.isec.PD.Data.Request;

import java.util.ArrayList;
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
            System.out.println("0 --> Exit");
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
                case 0:
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
                Thread.sleep(100);
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
                Thread.sleep(100);
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
            System.out.println("Welcome [" + client.getUser().getName() + "] to your application");
            System.out.println("");
            System.out.println("Choose an option below");

            System.out.println("1 --> Edit Personal Data");
            System.out.println("2 --> List and Search Users");
            System.out.println("3 --> Contacts Menu");
            System.out.println("4 --> Groups Menu");
            System.out.println("0 --> Logout");
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
                        uiSearchUsers();
                        break;
                    case 3:
                        uiContacts();
                        break;
                    case 4:
                        uiGroups();
                        break;
                    case 0:
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
            System.out.println("Edit Personal Data");
            System.out.println("Name: " + client.getUser().getName() + " Username: " + client.getUser().getUsername() + " Password: " + client.getUser().getPassword());
            System.out.println("");
            System.out.println("1 --> Change Name");
            System.out.println("2 --> Change Username");
            System.out.println("3 --> Change Password");
            System.out.println("0 --> Return to Main Menu");

            System.out.println("");

            System.out.print("Answer: ");
            while (!s.hasNextInt()) {
                s.next();
            }

            value = s.nextInt();

            switch (value) {
                case 1:
                    uiChangeName();
                    break;
                case 2:
                    uiChangeUsername();
                    break;
                case 3:
                    uiChangePassword();
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

    public void uiChangeName(){

        Scanner sc = new Scanner(System.in);
        String name = null;

        System.out.println("");
        System.out.println("Enter your new Name: ");

        name = sc.nextLine();

        client.changeName(name);

    }

    public void uiChangeUsername(){

        Scanner sc = new Scanner(System.in);
        String username = null;

        System.out.println("");
        System.out.println("Enter your new Username: ");

        username = sc.nextLine();

        client.changeUsername(username);

    }

    public void uiChangePassword(){

        Scanner sc = new Scanner(System.in);
        String password = null;

        System.out.println("");
        System.out.println("Enter your new Password: ");

        password = sc.nextLine();

        client.changePassword(password);

    }

    public void uiSearchUsers(){

        int value;
        boolean leave = false;

        while(!leave) {
            System.out.println("");
            System.out.println("Choose a option below");
            System.out.println("");
            System.out.println("1 --> List Users");
            System.out.println("2 --> Search User");
            System.out.println("0 --> Return to Main Menu");

            System.out.println("");

            System.out.print("Answer: ");
            while (!s.hasNextInt()) {
                s.next();
            }

            value = s.nextInt();

            switch (value) {
                case 1:
                    uiListUsers();
                    break;
                case 2:
                    uiSearchUser();
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

    public void uiSearchUser(){

        Scanner sc = new Scanner(System.in);
        String username = null;

        System.out.println("");
        System.out.println("Enter the username of the user you want to search: ");

        username = sc.nextLine();

        client.searchUser(username);

    }

    public void uiListUsers(){

        client.listUsers();
    }

    public void uiContacts(){

        int value;
        boolean leave = false;

        while(!leave) {

            System.out.println("");
            System.out.println("---------- Contacts ----------");
            System.out.println("");
            System.out.println("1 --> Send Contact Request");
            System.out.println("2 --> List Contacts");
            System.out.println("3 --> Delete Contact");
            System.out.println("0 --> Return to Main Menu");

            System.out.println("");

            System.out.print("Answer: ");
            while (!s.hasNextInt()) {
                s.next();
            }

            value = s.nextInt();

            switch (value) {
                case 1:
                    //uiContactRequest();
                    break;
                case 2:
                    //uiCheckContacts();
                    break;
                case 3:
                    //uiDeleteContact();
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

    public void uiGroups(){

        int value;
        boolean leave = false;

        while(!leave) {

            System.out.println("");
            System.out.println("---------- Groups ----------");
            System.out.println("");
            System.out.println("1 --> List your Groups");
            System.out.println("2 --> Create Group");
            System.out.println("3 --> Edit Group"); // expulsar membros e extinguir grupo
            System.out.println("4 --> Join Group");
            System.out.println("5 --> Accept members");
            System.out.println("6 --> Exit Group");
            System.out.println("7 --> Remove member from Group");
            System.out.println("8 --> Delete Group");
            System.out.println("0 --> Return to Main Menu");

            System.out.println("");

            System.out.print("Answer: ");
            while (!s.hasNextInt()) {
                s.next();
            }

            value = s.nextInt();

            switch (value) {
                case 1:
                    uiListGroups();
                    break;
                case 2:
                    uiCreateGroup();
                    break;
                case 3:
                    uiEditGroup();
                    break;
                case 4:
                    uiJoinGroup();
                    break;
                case 5:
                    uiAcceptGroup();
                    break;
                case 6:
                    uiExitGroup();
                    break;
                case 7:
                    //uiRemoveMember();
                    break;
                case 8:
                    //uiDeleteGroup();
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

    public void uiListGroups() {
        client.listGroups();
    }


    public void uiCreateGroup() {
        do {

            Scanner sc = new Scanner(System.in);
            String name;

            System.out.println("");
            System.out.println("------ Create new group ------");
            System.out.println("");

            System.out.println("Enter the group name: ");

            name = sc.nextLine();

            client.createGroup(name,client.getUser());

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }while (!client.getTaskCompleted());
    }

    public void uiEditGroup() {
        do {

            Scanner sc = new Scanner(System.in);
            String name,newName;

            System.out.println("");
            System.out.println("------ Edit group ------");
            System.out.println("");

            System.out.println("Enter the name of the group you want to edit: ");

            name = sc.nextLine();

            System.out.println("Enter the new name of the group: ");

            newName = sc.nextLine();

            client.editGroup(name,newName,client.getUser());

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }while (!client.getTaskCompleted());
    }

    public void uiJoinGroup() {
        Scanner sc = new Scanner(System.in);
        String idGroup;
        int id;

        System.out.println("");
        System.out.println("------ Join group ------");
        System.out.println("");

        client.listGroups();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Enter the id of the group you want to join: ");
        idGroup = sc.nextLine();

        try{
            id=Integer.parseInt(idGroup);
        }catch (Exception e){
            id=-1;
        }
        client.sendResquestGroup(id,client.getUser());

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void uiAcceptGroup() {
        Scanner sc = new Scanner(System.in);
        String idRequest;
        int num=0;
        ArrayList<Request> requests=null;
        System.out.println("");
        System.out.println("------ Accept new members ------");
        System.out.println("");

        client.listRequest();
        do{
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }while (!client.getTaskCompleted());

        requests=client.getRequestForGroups();
        if(requests == null || requests.size()==0){
            System.out.println("No request available");
            return;
        }else
            for (Request request : requests)
                System.out.println(num+" - "+request.getUserName()+" want to join "+request.getGroupName());

        System.out.println("Enter the number of request you want to accept: (-1 to exit)");
        idRequest = sc.nextLine();

        try{
            num=Integer.parseInt(idRequest);
        }catch (Exception e){
            num=-1;
        }

        if(-1 < num && num<requests.size())
            client.sendResquestResponse(requests.get(num),client.getUser());
        else
            System.out.println("Invalid option");
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void uiExitGroup() {
        Scanner sc = new Scanner(System.in);
        String idRequest;
        int num=0;
        ArrayList<Group> groups=null;
        System.out.println("");
        System.out.println("------ Exit group ------");
        System.out.println("");

        client.listMyGroups();
        do{
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }while (!client.getTaskCompleted());

        groups=client.getMyGroups();
        if(groups == null || groups.size()==0){
            System.out.println("You are not in any group");
            return;
        }else
            for (Group group : groups)
                System.out.println(group.getId()+" - "+group.getName());

        System.out.println("Enter the id of the group you want to exit: (-1 to exit)");
        idRequest = sc.nextLine();

        try{
            num=Integer.parseInt(idRequest);
        }catch (Exception e){
            num=-1;
        }

        if(-1 != num)
            client.exitGroup(num,client.getUser());
        else
            System.out.println("Invalid option");
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}