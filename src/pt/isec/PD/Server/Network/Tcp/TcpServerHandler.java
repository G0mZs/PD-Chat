package pt.isec.PD.Server.Network.Tcp;

import pt.isec.PD.Data.*;
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
                        checkLogin(model.getDbHelper().Login(message.getUser().getUsername(), message.getUser().getPassword()), out,message);
                        break;
                    case REGISTER:
                        checkRegister(model.getDbHelper().Register(message.getUser().getName(), message.getUser().getUsername(),message.getUser().getPassword()), out);
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
                    case LIST_CONTACTS:
                        getContacts(message,out);
                        break;
                    case LIST_PENDING_REQUESTS:
                        getPendingRequests(message,out);
                        break;
                    case LIST_HISTORIC:
                        getHistoric(message,out);
                        break;
                    case LIST_GROUP_HISTORIC:
                        getGroupHistoric(message,out);
                        break;
                    case LIST_GROUPS:
                        getGroups(out);
                        break;
                    case LIST_GROUP_REQUESTS:
                        getGroupRequests(message,out);
                        break;
                    case CREATE_GROUP:
                        createGroup(message,out);
                        break;
                    case CHANGE_GROUP_NAME:
                        changeGroupName(message,out);
                        break;
                    case GROUP_REQUEST:
                        sendGroupRequest(message.getRequest(),out);
                        break;
                    case GROUP_ACCEPT:
                        acceptGroupRequest(message,out);
                        break;
                    case GROUP_REFUSE:
                        refuseGroupRequest(message,out);
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
                    case MESSAGE_CONTACT:
                        sendContactMessage(message,out);
                        break;
                    case MESSAGE_SEEN:
                        updateHistoric(message);
                        break;
                    case DELETE_MESSAGE:
                        deleteMessage(message,out);
                        break;
                    case GROUP_MESSAGE:
                        sendGroupMessage(message,out);
                        break;
                    case DELETE_GROUP:
                        deleteGroup(message,out);
                        break;
                    case MESSAGE_GROUP_SEEN:
                        updateGroupHistoric(message);
                        break;
                    case DELETE_GROUP_MESSAGE:
                        deleteGroupMessage(message,out);
                        break;
                    case LEAVE_GROUP:
                        leaveGroup(message,out);
                        break;
                    case KICK_MEMBER:
                        kickMember(message,out);
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
                    case SERVER_RECEIVE_MESSAGE:
                        sendContactMessageToClient(message);
                        running = false;
                        break;
                    case SERVER_MESSAGE_SEEN:
                        sendMessageSeenToClient(message);
                        running = false;
                        break;
                    case SERVER_GROUP_REQUEST:
                        sendGroupRequestToClient(message);
                        running = false;
                        break;
                    case SERVER_KICK_MEMBER:
                        sendKickMemberToClient(message);
                        running = false;
                        break;
                    case SERVER_LEAVE_GROUP:
                        sendLeaveGroupToClient(message);
                        running = false;
                        break;
                    case SERVER_ACCEPT_RQUEST:

                        running = false;
                        break;
                    case SERVER_REFUSE_REQUEST:

                        running = false;
                        break;
                }
            }

        } catch (EOFException e) {
            // Client disconnected
        } catch(IOException | ClassNotFoundException e){
            e.printStackTrace();
        }finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
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

    public synchronized void checkRegister(boolean register,ObjectOutputStream out){

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

            msg = new Message(Message.Type.ERROR_MESSAGE,"This name already exists");
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

            msg = new Message(Message.Type.ERROR_MESSAGE,"This Username already exists");
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
            msg = new Message(Message.Type.ERROR_MESSAGE,"This user doesn't exist");
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
        msg = new Message(Message.Type.LIST_RECEIVED,listUsers);

        try {

            out.writeObject(msg);
            out.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public synchronized void getContacts(Message msg,ObjectOutputStream out){

        User user = msg.getUser();
        ArrayList<User> contacts = model.getDbHelper().getContacts(user.getId());

        msg = new Message(Message.Type.LIST_CONTACTS,contacts);
        try {

            out.writeObject(msg);
            out.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void getPendingRequests(Message msg,ObjectOutputStream out){

        User user = msg.getUser();
        ArrayList<User> pendingRequests = model.getDbHelper().getPendingRequests(user.getId());

        Message message = new Message(Message.Type.LIST_PENDING_REQUESTS,pendingRequests);
        try {

            out.writeObject(message);
            out.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void getHistoric(Message msg,ObjectOutputStream out){

        User user = msg.getUser();
        User contact = model.getDbHelper().searchUser(msg.getMessage());

        Message message;
        if(contact == null || contact.getId() == user.getId()){
            message = new Message(Message.Type.ERROR_MESSAGE,"The username is not valid!");
        }
        else{

            ArrayList<Message> historic = model.getDbHelper().getHistoric(user.getId(),contact.getId());
            message = new Message(Message.Type.LIST_HISTORIC,null,historic);
        }

        try {

            out.writeObject(message);
            out.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public synchronized void sendContactRequest(String sender,String receiver,ObjectOutputStream out){

        Message msg;
        if(sender.equals(receiver)){
            msg = new Message(Message.Type.ERROR_MESSAGE,"Contact Request Failed ! You cant send a contact request to yourself");
        }
        else {

            if (model.getDbHelper().checkUsername(receiver)) {
                msg = new Message(Message.Type.ERROR_MESSAGE, "Contact Request Failed ! This username is not available");
            } else {

                int idSender = model.getDbHelper().getId(sender);
                int idReceiver = model.getDbHelper().getId(receiver);

                if(model.getDbHelper().checkRequest(idSender,idReceiver)) {

                    model.getDbHelper().createContactRequest(idSender, idReceiver);
                    msg = new Message(Message.Type.ERROR_MESSAGE, "Contact Request Sent !");
                    User auxSender = model.getDbHelper().searchUser(sender);
                    User auxReceiver = model.getDbHelper().searchUser(receiver);
                    Contact request = new Contact(auxSender,auxReceiver);



                    for (int i = 0; i < model.getClients().size(); i++) {
                        if (model.getClients().get(i).getUser().getId() == idReceiver) {


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
                    msg = new Message(Message.Type.ERROR_MESSAGE, "There is already a contact request with that data");
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
            msg = new Message(Message.Type.ERROR_MESSAGE,"This user doesn't exist");
        }
        else if(contactUser.getId() == idUser){
            msg = new Message(Message.Type.ERROR_MESSAGE,"You are not apart of your contact list");
        }
        else{

            boolean remove = model.getDbHelper().removeContact(idUser,contactUser.getId());

                if(remove){
                    msg = new Message(Message.Type.DELETE_CONTACT,"Contact removed !");
                    model.getDbHelper().removeContactMessages(user.getId(),contactUser.getId());
                    Contact contact = new Contact(user,contactUser);
                    Message message = new Message(Message.Type.DELETE_CONTACT,"You have been removed from " + user.getUsername() + " contact list");

                    for(int i = 0; i < model.getClients().size(); i++){
                        if(contactUser.getId() == model.getClients().get(i).getUser().getId()){

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
                    msg = new Message(Message.Type.ERROR_MESSAGE,"Contact couldn't be removed because its not on your list !");

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
            msg = new Message(Message.Type.ERROR_MESSAGE,"This user doesn't exist");
        }
        else if(auxSender.getId() == auxReceiver.getId()){
            msg = new Message(Message.Type.ERROR_MESSAGE,"You can't accept requests from yourself !");
        }else{

            boolean verifyRequest = model.getDbHelper().requestTrue(auxSender.getId(),idAccepter);

            if(!verifyRequest){
                msg = new Message(Message.Type.ERROR_MESSAGE,"There are no pending requests from this user");
            }
            else {
                Contact request = new Contact(auxSender,auxReceiver);
                msg = new Message(Message.Type.CONTACT_ACCEPT,"Contact Request Accepted",request.getSender());
                for(int i = 0; i < model.getClients().size(); i++){
                    if(auxSender.getId() == model.getClients().get(i).getUser().getId()){

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
            msg = new Message(Message.Type.ERROR_MESSAGE,"The User doesn't exist");
        }
        else if(auxSender.getId() == auxRefuser.getId()){
            msg = new Message(Message.Type.ERROR_MESSAGE,"You can't refuse requests from yourself !");
        }else{

            boolean verifyRequest = model.getDbHelper().requestFalse(auxSender.getId(),idRefuser);

            if(!verifyRequest){
                msg = new Message(Message.Type.ERROR_MESSAGE,"There are no pending requests from this user");
            }
            else {
                msg = new Message(Message.Type.CONTACT_REFUSED,"Contact Request Refused",auxSender);
                for(int i = 0; i < model.getClients().size(); i++){
                    if(auxSender.getId() == model.getClients().get(i).getUser().getId()){

                        message = new Message(Message.Type.ERROR_MESSAGE,auxRefuser.getUsername() + " has refused your contact request !");

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

    public synchronized void sendContactMessage(Message msg,ObjectOutputStream out){

        User sender = msg.getUser();
        User contact = model.getDbHelper().searchUser(msg.getReceiver());

        Message message;

            if(contact == null){
                message = new Message(Message.Type.ERROR_MESSAGE,"This user doesn't exist");
            }
            else if(sender.getId() == contact.getId()){
                message = new Message(Message.Type.ERROR_MESSAGE,"Failed ! You can't send messages to yourself !");
            }
            else{

                boolean verifyContact = model.getDbHelper().verifyContact(sender.getId(),contact.getId());

                    if(verifyContact){

                        model.getDbHelper().addMessage(sender.getId(),contact.getId(),msg.getTypeofMessage(),msg.getMessage(),msg.getDateTime(),msg.getState());

                        message = new Message(Message.Type.SEND_MESSAGE);

                        //verifica no mesmo server se há um cliente com o username do contact, se houver manda msg para ele.
                        for(int i = 0; i < model.getClients().size(); i++){

                            if(contact.getId() == model.getClients().get(i).getUser().getId()){

                                Message message1 = new Message(Message.Type.RECEIVE_MESSAGE,msg.getMessage(),sender);

                                try {
                                    model.getClients().get(i).getOut().writeObject(message1);
                                    model.getClients().get(i).getOut().flush();

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        //verifica nos outros servidores se existe um cliente com o mesmo username do contact, se houver manda msg para ele
                        contactServers(new Message(Message.Type.SERVER_RECEIVE_MESSAGE,msg.getMessage(),msg.getTypeofMessage(),msg.getDateTime(),sender,contact.getUsername(),"Não vista"));

                    }
                    else{

                        message = new Message(Message.Type.ERROR_MESSAGE,"The user is not on your contact list");

                    }
            }


        try {
            out.writeObject(message);
            out.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public synchronized void deleteMessage(Message msg,ObjectOutputStream out){

        Message message;

        if(!model.getDbHelper().checkMessageId(msg.getMessage())){

            message = new Message(Message.Type.ERROR_MESSAGE,"There are no messages with the specified Id");

        }else{

            if(!model.getDbHelper().checkMessageAuthor(msg.getMessage(),msg.getUser().getId())){
                message = new Message(Message.Type.ERROR_MESSAGE,"You can't delete this message because you are not the author");
            }
            else{

                model.getDbHelper().deleteMessage(msg.getMessage());
                message = new Message(Message.Type.DELETE_MESSAGE,"Your message has been deleted !");
            }
        }

        try {
            out.writeObject(message);
            out.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void updateHistoric(Message msg){

        User sender = msg.getUser();
        User receiver = model.getDbHelper().searchUser(msg.getMessage());

        model.getDbHelper().updateHistoric(sender.getId(),receiver.getId());

    }

    public synchronized void getGroups(ObjectOutputStream out){

        Message msg;
        ArrayList<Group> listGroups = model.getDbHelper().getAllGroups();

        msg = new Message(Message.Type.LIST_GROUPS, listGroups,0);

        try {
            out.writeObject(msg);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void createGroup(Message msg,ObjectOutputStream out){

        Message message;


        if(model.getDbHelper().checkGroupName(msg.getUser().getId(),msg.getMessage())){

            model.getDbHelper().createGroup(msg.getUser().getId(),msg.getMessage());
            message = new Message(Message.Type.CREATE_GROUP,"The Group was created successfully");
        }
        else{
            message = new Message(Message.Type.ERROR_MESSAGE,"You already have a group with the same name!");
        }

        try {
            out.writeObject(message);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public synchronized void deleteGroup(Message msg,ObjectOutputStream out){

        Message message;

        if(model.getDbHelper().checkGroupName(msg.getUser().getId(),msg.getMessage())){

            message = new Message(Message.Type.ERROR_MESSAGE,"Delete Group failed. You introduced incorrect data");

        }else{

            int idGroup = model.getDbHelper().getGroupId(msg.getUser().getId(),msg.getMessage());

            model.getDbHelper().deleteGroupRequests(idGroup);
            model.getDbHelper().deleteGroupHistoric(idGroup);
            model.getDbHelper().deleteGroup(idGroup);
            message = new Message(Message.Type.DELETE_GROUP,"Group has been deleted!");

        }

        try {
            out.writeObject(message);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void kickMember(Message msg,ObjectOutputStream out){

            int idAdmin = msg.getGroup().getAdmnistrator().getId();
            String groupName = msg.getGroup().getName();
            int idGroup = model.getDbHelper().getGroupId(msg.getGroup().getAdmnistrator().getId(),msg.getGroup().getName());
            User userKicked = model.getDbHelper().searchUser(msg.getMessage());
            Message message;

            if(userKicked == null){
                message = new Message(Message.Type.ERROR_MESSAGE,"This username doesn't exist");
            }else {

                if (model.getDbHelper().checkGroupName(idAdmin, groupName)) {

                    message = new Message(Message.Type.ERROR_MESSAGE, "The group doesn't exist or you are not the admin");

                } else {

                    message = new Message(Message.Type.KICK_MEMBER, "The member was removed");
                    model.getDbHelper().kickMember(idGroup,userKicked.getId());

                    for(int i = 0; i < model.getClients().size(); i++) {

                        if (userKicked.getId() == model.getClients().get(i).getUser().getId()) {

                            Message message1 = new Message(Message.Type.KICK_MEMBER,"You got kicked from the group: " + groupName);

                            try {
                                model.getClients().get(i).getOut().writeObject(message1);
                                model.getClients().get(i).getOut().flush();

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    contactServers(new Message(Message.Type.SERVER_KICK_MEMBER,"You got kicked from the group: " + groupName,userKicked));

                }
            }

        try {
            out.writeObject(message);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public synchronized void changeGroupName(Message msg,ObjectOutputStream out){

        Message message;

        if(model.getDbHelper().checkGroupExistence(msg.getGroup().getAdmnistrator().getId(),msg.getGroup().getName())){

            if(msg.getGroup().getName().equals(msg.getMessage())){

                message = new Message(Message.Type.ERROR_MESSAGE,"Name Change Failed! The name you typed is already the name of the group");

            }else{

                model.getDbHelper().changeGroupName(msg.getGroup().getAdmnistrator().getId(),msg.getGroup().getName(),msg.getMessage());
                message = new Message(Message.Type.CHANGE_GROUP_NAME,"You changed your group name successfully");
            }

        }else{

            message = new Message(Message.Type.ERROR_MESSAGE,"The Group does not exist or you are not his admnistrator!");
        }

        try {
            out.writeObject(message);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public synchronized void sendGroupRequest(Request request,ObjectOutputStream out){

        Message message;
        User requester = model.getDbHelper().getUser(request.getIdUser());
        request.setUserName(requester.getUsername());

        if(model.getDbHelper().checkGroupId(request.getIdGroup())){ // verifica se o id do grupo é valido

            int idAdmin = model.getDbHelper().getAdminId(request.getIdGroup()); // vai buscar o id do admin associado ao grupo

            if(idAdmin != request.getIdUser()){ // Verifica se o utilizador está a mandar pedidos para si mesmo

                if(model.getDbHelper().checkGroupRequests(request.getIdGroup(),request.getIdUser())){ // Verifica se já existe um pedido entre o user e o grupo aceite ou pendente

                    model.getDbHelper().addGroupRequest(request.getIdGroup(),request.getIdUser());
                    message = new Message(Message.Type.GROUP_REQUEST,"The request was sent to the admin of the group!");

                    for(int i = 0; i < model.getClients().size(); i++){

                        if(idAdmin == model.getClients().get(i).getUser().getId()){

                            Message message1 = new Message(Message.Type.GROUP_REQUEST,requester.getUsername() + " sent you a group request for the group with the id [" + request.getIdGroup() + "]. Please answer the request in the pending group requests menu.");

                            try {
                                model.getClients().get(i).getOut().writeObject(message1);
                                model.getClients().get(i).getOut().flush();

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    contactServers(new Message(Message.Type.SERVER_GROUP_REQUEST, String.valueOf(idAdmin),request));

                }
                else{
                    message = new Message(Message.Type.ERROR_MESSAGE,"There is already a group request with that data");
                }

            }else{
                message = new Message(Message.Type.ERROR_MESSAGE,"You can't send requests to yourself");
            }


        }else{
            message = new Message(Message.Type.ERROR_MESSAGE,"The group doesn't exist!");
        }

        try {
            out.writeObject(message);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public synchronized void acceptGroupRequest(Message msg,ObjectOutputStream out){

        Request request = msg.getRequest();
        User requester = model.getDbHelper().searchUser(request.getUserName());
        String adminUsername = msg.getMessage();
        User admin = model.getDbHelper().searchUser(adminUsername);
        Message message;

        if(msg.getRequest().getUserName().equals(adminUsername)){

            message = new Message(Message.Type.ERROR_MESSAGE,"This user is not valid!");

        }else if(requester == null){

            message = new Message(Message.Type.ERROR_MESSAGE,"This user doesn't exist");
        }else{

            if(model.getDbHelper().checkGroupId(request.getIdGroup())){

                if(model.getDbHelper().checkAdmin(admin.getId(),request.getIdGroup())){//Verificar se o grupo pertence ao admin

                    if(model.getDbHelper().verifyGroupRequest(requester.getId(),request.getIdGroup())){ //Verificar se existe um pedido com o id do grupo e id do requester

                        model.getDbHelper().acceptGroupRequest(requester.getId(),request.getIdGroup());
                        message = new Message(Message.Type.GROUP_ACCEPT,"Success!");

                        for(int i = 0; i < model.getClients().size(); i++) {

                            if (requester.getId() == model.getClients().get(i).getUser().getId()) {

                                Message message1 = new Message(Message.Type.GROUP_ACCEPT, adminUsername + " has accepted your Group Request !");

                                try {
                                    model.getClients().get(i).getOut().writeObject(message1);
                                    model.getClients().get(i).getOut().flush();

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }


                    }else{

                        message = new Message(Message.Type.ERROR_MESSAGE,"Theres no pending group request with that data");
                    }


                }else{
                    message = new Message(Message.Type.ERROR_MESSAGE,"This group does not belong to you");
                }

            }else{

                message = new Message(Message.Type.ERROR_MESSAGE,"This group doesn't exist");
            }


        }


        try {
            out.writeObject(message);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public synchronized void refuseGroupRequest(Message msg,ObjectOutputStream out){

        Request request = msg.getRequest();
        User requester = model.getDbHelper().searchUser(request.getUserName());
        String adminUsername = msg.getMessage();
        User admin = model.getDbHelper().searchUser(adminUsername);
        Message message;

        if(msg.getRequest().getUserName().equals(adminUsername)){

            message = new Message(Message.Type.ERROR_MESSAGE,"This user is not valid!");

        }else if(requester == null){

            message = new Message(Message.Type.ERROR_MESSAGE,"This user doesn't exist");
        }else{

            if(model.getDbHelper().checkGroupId(request.getIdGroup())){

                if(model.getDbHelper().checkAdmin(admin.getId(),request.getIdGroup())){//Verificar se o grupo pertence ao admin

                    if(model.getDbHelper().verifyGroupRequest(requester.getId(),request.getIdGroup())){ //Verificar se existe um pedido com o id do grupo e id do requester


                        model.getDbHelper().refuseGroupRequest(requester.getId(),request.getIdGroup());
                        message = new Message(Message.Type.GROUP_REFUSE,"Success!");

                        for(int i = 0; i < model.getClients().size(); i++) {

                            if (requester.getId() == model.getClients().get(i).getUser().getId()) {

                                Message message1 = new Message(Message.Type.GROUP_REFUSE,adminUsername + " has refused your Group Request !");

                                try {
                                    model.getClients().get(i).getOut().writeObject(message1);
                                    model.getClients().get(i).getOut().flush();

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }



                    }else{

                        message = new Message(Message.Type.ERROR_MESSAGE,"Theres no pending group request with that data");
                    }


                }else{
                    message = new Message(Message.Type.ERROR_MESSAGE,"This group does not belong to you");
                }

            }else{

                message = new Message(Message.Type.ERROR_MESSAGE,"This group doesn't exist");
            }


        }


        try {
            out.writeObject(message);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void getGroupRequests(Message msg,ObjectOutputStream out){

        int idAdmin = Integer.parseInt(msg.getMessage());

        ArrayList<Request> groupRequests = model.getDbHelper().getGroupRequests(idAdmin);

        Message message = new Message(Message.Type.LIST_GROUP_REQUESTS,0,groupRequests);

        try {
            out.writeObject(message);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public synchronized void leaveGroup(Message msg,ObjectOutputStream out){

        User user = msg.getUser();
        Group group = model.getDbHelper().getGroup(Integer.parseInt(msg.getMessage()));
        Message message;

        if(group == null){
            message = new Message(Message.Type.ERROR_MESSAGE,"The id of the group is not valid");
        }
        else{

            if(model.getDbHelper().checkAdmin(user.getId(),group.getId())){
                message = new Message(Message.Type.ERROR_MESSAGE,"You can't leave your own group");
            }else{

                if(model.getDbHelper().checkMember(user.getId(),group.getId())){

                    message = new Message(Message.Type.LEAVE_GROUP,"You left the group");

                    model.getDbHelper().deleteRequest(user.getId(),group.getId());

                    int idAdmin = model.getDbHelper().getAdminId(group.getId());

                    for(int i = 0; i < model.getClients().size(); i++) {

                        if (idAdmin == model.getClients().get(i).getUser().getId()) {

                            Message message1 = new Message(Message.Type.LEAVE_GROUP,user.getUsername() + " has left your group " + group.getId());

                            try {
                                model.getClients().get(i).getOut().writeObject(message1);
                                model.getClients().get(i).getOut().flush();

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    contactServers(new Message(Message.Type.SERVER_LEAVE_GROUP,user.getUsername() + " has left your group " + group.getId(),new User(idAdmin,null,null,null)));


                }else{
                    message = new Message(Message.Type.ERROR_MESSAGE,"You are not a member of this group");
                }
            }
        }

        try {
            out.writeObject(message);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void sendGroupMessage(Message msg,ObjectOutputStream out){

        User user = msg.getUser();
        int idGroup = msg.getGroup().getId();
        Message message;

        if(model.getDbHelper().checkGroupId(idGroup)){



            if(model.getDbHelper().checkMemberOrAdmin(user.getId(),idGroup)){

                model.getDbHelper().addGroupMessage(msg);

                message = new Message(Message.Type.GROUP_MESSAGE,"Message Sent");



            }else{
                message = new Message(Message.Type.ERROR_MESSAGE,"You are not a member of this group");
            }

        }else{
            message = new Message(Message.Type.ERROR_MESSAGE,"This group doesn't exist");
        }



        try {
            out.writeObject(message);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void getGroupHistoric(Message msg,ObjectOutputStream out){

        User user = msg.getUser();
        int idGroup = Integer.parseInt(msg.getMessage());
        Group group = model.getDbHelper().getGroup(idGroup);

        Message message;
        if(group == null){
            message = new Message(Message.Type.ERROR_MESSAGE,"The group id/number is not valid!");
        }
        else{
            if(model.getDbHelper().checkMemberOrAdmin(user.getId(),idGroup)) {
                ArrayList<Message> historic = model.getDbHelper().getGroupHistoric(idGroup);
                message = new Message(Message.Type.LIST_GROUP_HISTORIC, null, historic);

            }
            else{
                message = new Message(Message.Type.ERROR_MESSAGE,"You are not a member or admin of this group");
            }
        }

        try {

            out.writeObject(message);
            out.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void updateGroupHistoric(Message msg){

        User user = msg.getUser();
        Group group = model.getDbHelper().getGroup(Integer.parseInt(msg.getMessage()));

        model.getDbHelper().updateGroupHistoric(user.getId(),group.getId());
    }

    public synchronized void deleteGroupMessage(Message msg,ObjectOutputStream out){

        User user = msg.getUser();
        Message message;

        if(model.getDbHelper().checkMessageAuthor(msg.getMessage(),user.getId())){

            if(model.getDbHelper().messageIsFromGroup(msg.getMessage())) {
                model.getDbHelper().deleteGroupMessage(user.getId(), msg.getMessage());
                message = new Message(Message.Type.DELETE_GROUP_MESSAGE, "Message deleted !");
            }else{
                message = new Message(Message.Type.ERROR_MESSAGE,"This message is from contacts. You can only delete group messages");
            }

        }
        else{
            message = new Message(Message.Type.ERROR_MESSAGE,"You are not the author of this message");
        }

        try {

            out.writeObject(message);
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

            if (model.getClients().get(i).getUser().getId() == request.getReceiver().getId()) {


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

            if (model.getClients().get(i).getUser().getId() == request.getReceiver().getId()) {


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

            if (model.getClients().get(i).getUser().getId() == request.getSender().getId()) {

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

            if (model.getClients().get(i).getUser().getId() == sender.getId()) {

                Message message = new Message(Message.Type.ERROR_MESSAGE, msg);

                try {
                    model.getClients().get(i).getOut().writeObject(message);
                    model.getClients().get(i).getOut().flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void sendContactMessageToClient(Message msg){

        for (int i = 0; i < model.getClients().size(); i++) {

            if (model.getClients().get(i).getUser().getUsername().equals(msg.getReceiver())) {

                Message message = new Message(Message.Type.RECEIVE_MESSAGE,msg.getUser());

                try {
                    model.getClients().get(i).getOut().writeObject(message);
                    model.getClients().get(i).getOut().flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void sendMessageSeenToClient(Message msg){

        for (int i = 0; i < model.getClients().size(); i++) {

            if (model.getClients().get(i).getUser().getId() == msg.getUser().getId()) {

                Message message = new Message(Message.Type.MESSAGE_SEEN);

                try {
                    model.getClients().get(i).getOut().writeObject(message);
                    model.getClients().get(i).getOut().flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void sendGroupRequestToClient(Message msg){

        int idAdmin = Integer.parseInt(msg.getMessage());

        for (int i = 0; i < model.getClients().size(); i++) {

            if (model.getClients().get(i).getUser().getId() == idAdmin) {

                Message message = new Message(Message.Type.GROUP_REQUEST,msg.getRequest().getUserName() + " sent you a group request for the group with the id [" + msg.getRequest().getIdGroup() + "]. Please answer the request in the pending group requests menu.");

                try {
                    model.getClients().get(i).getOut().writeObject(message);
                    model.getClients().get(i).getOut().flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void sendKickMemberToClient(Message msg){

        for (int i = 0; i < model.getClients().size(); i++) {

            if (model.getClients().get(i).getUser().getId() == msg.getUser().getId()) {

                Message message = new Message(Message.Type.KICK_MEMBER,msg.getMessage());

                try {
                    model.getClients().get(i).getOut().writeObject(message);
                    model.getClients().get(i).getOut().flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void sendLeaveGroupToClient(Message msg){


        for (int i = 0; i < model.getClients().size(); i++) {

            if (model.getClients().get(i).getUser().getId() == msg.getUser().getId()) {

                Message message = new Message(Message.Type.LEAVE_GROUP,msg.getMessage());

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
