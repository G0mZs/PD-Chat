package pt.isec.PD.Client.Network;

import pt.isec.PD.Client.Model.Chat;
import pt.isec.PD.Client.Network.Tcp.TcpClientManager;
import pt.isec.PD.Client.Network.Udp.UdpClientManager;
import pt.isec.PD.Data.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class CommunicationHandler {

    private UdpClientManager udpClientManager;
    private TcpClientManager tcpClientManager;
    private Chat chat;
    private ObjectInputStream input = null;
    private ObjectOutputStream output = null;
    private Socket s = null;
    boolean exit;

    public CommunicationHandler(Chat chat){
        this.chat = chat;
        this.udpClientManager = new UdpClientManager(chat,Constants.UDP_PORT,Constants.GRDS_ADDRESS);
    }

    public void startUDP(){ udpClientManager.start();}

    public User getUser() { return chat.getUser();}

    public void setUser(User user) { chat.setUser(user);}

    public boolean getLoginState(){
        return tcpClientManager.getLogin();
    }

    public void setLoginState(boolean state){
        tcpClientManager.setLogin(state);
    }

    public void setInfoUser(){
        setUser(tcpClientManager.getUserData());
    }

    public void startConnection(){

        exit = false;

        while(!exit){


            udpClientManager.askForServerConnection();

            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if(checkTcpPort()){

                try {

                    exit = true;
                    connectTcp(Constants.SERVER_ADDRESS,udpClientManager.getServerTcpPort());
                    tcpClientManager = new TcpClientManager(chat,input,output);
                    tcpClientManager.start();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public boolean checkTcpPort(){

        if(udpClientManager.getServerTcpPort() == 0){
            return false;
        }
        else {
            return true;
        }
    }

    private void connectTcp(String serverIp,int serverPort) throws Exception {

        InetAddress add = InetAddress.getByName(serverIp);

        this.s = new Socket(add, serverPort);
        this.output = new ObjectOutputStream(s.getOutputStream());
        this.input = new ObjectInputStream(s.getInputStream());


        output.writeObject(new Message(Message.Type.CONNECT_TCP,"Client Connected " ));
        output.flush();

    }

    public void disconnect() {
        stopTcp();
    }

    public void stopTcp() {
        if (s != null && s.isConnected()) {
            try {
                input.close();
                output.close();
                s.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendRegisterData(String name,String username, String password) {

        User auxUser = new User(0,username,password,name);

        try {

            output.writeObject(new Message(Message.Type.REGISTER,auxUser));
            output.flush();

        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    public void sendLoginData(String username,String password){

        User auxUser = new User(0,username,password,null);

        try {

            output.writeObject(new Message(Message.Type.LOGIN,auxUser));
            output.flush();

        } catch (IOException e) {
            e.printStackTrace();

        }

    }

    public void logout(int id){

        User auxUser = new User(id,null,null,null);

        try {

            output.writeObject(new Message(Message.Type.LOGOUT,auxUser));
            output.flush();

        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    public void changePassword(String password){

        User auxUser = new User(getUser().getId(),null,password,null);

        try {

            output.writeObject(new Message(Message.Type.CHANGE_PASSWORD,null,auxUser));
            output.flush();

        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    public void changeName(String name){
        User auxUser = new User(getUser().getId(),null,null,name);

        try {

            output.writeObject(new Message(Message.Type.CHANGE_NAME,auxUser));
            output.flush();

        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    public void changeUsername(String username){
        User auxUser = new User(getUser().getId(),username,null,null);

        try {

            output.writeObject(new Message(Message.Type.CHANGE_USERNAME,auxUser));
            output.flush();

        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    public void searchUser(String username){

        User auxUser = new User(0,username,null,null);

        try {

            output.writeObject(new Message(Message.Type.SEARCH_USER,auxUser));
            output.flush();

        } catch (IOException e) {
            e.printStackTrace();

        }

    }

    public void listUsers(){

        try {

            output.writeObject(new Message(Message.Type.LIST_USERS));
            output.flush();

        } catch (IOException e) {
            e.printStackTrace();

        }

    }

    public void listContacts(){

        try {

            output.writeObject(new Message(Message.Type.LIST_CONTACTS,getUser()));
            output.flush();

        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    public void listHistoric(String username){

        try {

            output.writeObject(new Message(Message.Type.LIST_HISTORIC,username,getUser()));
            output.flush();

        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    public void listPendingRequests(){

        try {

            output.writeObject(new Message(Message.Type.LIST_PENDING_REQUESTS,getUser()));
            output.flush();

        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    public void sendContactRequest(String username){

        User aux = new User(0,username,null,null);

        try {
            output.writeObject(new Message(Message.Type.CONTACT_REQUEST, chat.getUser().getUsername(),aux));
            output.flush();

        }catch (IOException e) {
            e.printStackTrace();

        }
    }

    public void deleteContact(String username){

        User aux = new User(chat.getUser().getId(),username,null,null);

        try {
            output.writeObject(new Message(Message.Type.DELETE_CONTACT,null,aux));
            output.flush();

        }catch (IOException e) {
            e.printStackTrace();

        }

    }

    public void acceptContactRequest(String username){

        User aux = new User(chat.getUser().getId(),username,null,null);

        try {
            output.writeObject(new Message(Message.Type.CONTACT_ACCEPT,null,aux));
            output.flush();

        }catch (IOException e) {
            e.printStackTrace();

        }
    }

    public void refuseContactRequest(String username){

        User aux = new User(chat.getUser().getId(),username,null,null);

        try {
            output.writeObject(new Message(Message.Type.CONTACT_REFUSED,null,aux));
            output.flush();

        }catch (IOException e) {
            e.printStackTrace();

        }
    }

    public void sendContactMessage(String contact,String content){

        User author = new User(chat.getUser().getId(),chat.getUser().getUsername(),null,chat.getUser().getName());
        LocalDateTime lt = LocalDateTime.now();

        try {
            output.writeObject(new Message(Message.Type.MESSAGE_CONTACT,content,"Message",lt,author,contact,"Não Vista"));
            output.flush();

        }catch (IOException e) {
            e.printStackTrace();

        }
    }

    public String returnState(boolean connected){
        if(connected){
            return "Online";
        }
        else{
            return "Offline";
        }
    }


    public void updateHistoric(String username){

        try {
            output.writeObject(new Message(Message.Type.MESSAGE_SEEN,username,getUser()));
            output.flush();

        }catch (IOException e) {
            e.printStackTrace();

        }
    }

    public void deleteMessage(int idMsg){

        User user = getUser();
        String idMessage = String.valueOf(idMsg);

        try {
            output.writeObject(new Message(Message.Type.DELETE_MESSAGE,idMessage,user));
            output.flush();

        }catch (IOException e) {
            e.printStackTrace();

        }

    }

    public void getGroups(){

        try {
            output.writeObject(new Message(Message.Type.LIST_GROUPS));
            output.flush();

        }catch (IOException e) {
            e.printStackTrace();

        }
    }

    public void createGroup(String name){

        User admin = getUser();

        try {
            output.writeObject(new Message(Message.Type.CREATE_GROUP,name,admin));
            output.flush();

        }catch (IOException e) {
            e.printStackTrace();

        }
    }

    public void changeGroupName(String name,String newName){

        Group group = new Group(getUser(),name);

        try {
            output.writeObject(new Message(Message.Type.CHANGE_GROUP_NAME,group,newName));
            output.flush();

        }catch (IOException e) {
            e.printStackTrace();

        }

    }

    public void sendGroupRequest(int idGroup){

        Request request = new Request(getUser().getId(),idGroup);


        try {
            output.writeObject(new Message(Message.Type.GROUP_REQUEST,request));
            output.flush();

        }catch (IOException e) {
            e.printStackTrace();

        }
    }

    public void acceptGroupRequest(String username,int idGroup){

        Request request = new Request(username,idGroup);

        try {
            output.writeObject(new Message(Message.Type.GROUP_ACCEPT,getUser().getUsername(),request));
            output.flush();

        }catch (IOException e) {
            e.printStackTrace();

        }

    }

    public void refuseGroupRequest(String username,int idGroup){

        Request request = new Request(username,idGroup);

        try {
            output.writeObject(new Message(Message.Type.GROUP_REFUSE,getUser().getUsername(),request));
            output.flush();

        }catch (IOException e) {
            e.printStackTrace();

        }
    }

    public void listPendingGroupRequests(){

        try {
            output.writeObject(new Message(Message.Type.LIST_GROUP_REQUESTS,String.valueOf(getUser().getId())));
            output.flush();

        }catch (IOException e) {
            e.printStackTrace();

        }

    }

    public void sendGroupMessage(int id,String content){

        User author = new User(chat.getUser().getId(),chat.getUser().getUsername(),null,chat.getUser().getName());
        LocalDateTime lt = LocalDateTime.now();
        Group group = new Group(id,null);

        try {
            output.writeObject(new Message(Message.Type.GROUP_MESSAGE,content,"Message",lt,author,group,"Não Vista"));
            output.flush();

        }catch (IOException e) {
            e.printStackTrace();

        }
    }

    public void listGroupHistoric(int idGroup){

        try {

            output.writeObject(new Message(Message.Type.LIST_GROUP_HISTORIC,String.valueOf(idGroup),getUser()));
            output.flush();

        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    public void updateGroupHistoric(int id){
        try {

            output.writeObject(new Message(Message.Type.MESSAGE_GROUP_SEEN,String.valueOf(id),getUser()));
            output.flush();

        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    public void deleteGroupMessage(int id){

        try {

            output.writeObject(new Message(Message.Type.DELETE_GROUP_MESSAGE,String.valueOf(id),getUser()));
            output.flush();

        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    public void deleteGroup(String name){
        try {

            output.writeObject(new Message(Message.Type.DELETE_GROUP,name,getUser()));
            output.flush();

        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    public void leaveGroup(int id){

        try {

            output.writeObject(new Message(Message.Type.LEAVE_GROUP,String.valueOf(id),getUser()));
            output.flush();

        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    public void kickMember(String groupName,String username){

        Group group = new Group(getUser(),groupName);

        try {

            output.writeObject(new Message(Message.Type.KICK_MEMBER,group,username));
            output.flush();

        } catch (IOException e) {
            e.printStackTrace();

        }
    }


    public Chat getChat() {
        return chat;
    }



}
