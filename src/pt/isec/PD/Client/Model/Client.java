package pt.isec.PD.Client.Model;

import pt.isec.PD.Client.Network.Tcp.TcpClientManager;
import pt.isec.PD.Client.Network.Udp.UdpClientManager;
import pt.isec.PD.Data.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

public class Client {

    private UdpClientManager udpClientManager;
    private TcpClientManager tcpClientManager;
    private User user;
    private ObjectInputStream input = null;
    private ObjectOutputStream output = null;
    private Socket s = null;
    boolean exit;

    public Client(){

        this.udpClientManager = new UdpClientManager(Constants.UDP_PORT,Constants.GRDS_ADDRESS);

    }

    public UdpClientManager getUdpClientManager() {
        return udpClientManager;
    }

    public User getUser() { return user;}

    public void setUser(User user) { this.user = user;}

    public boolean getRegisterState(){
        return tcpClientManager.getRegister();
    }

    public boolean getTaskCompleted(){
        return tcpClientManager.getTaskCompleted();
    }

    public ArrayList<Request> getRequestForGroups(){
        return  tcpClientManager.getRequestForGroups();
    }

    public ArrayList<Group> getMyGroups(){
        return  tcpClientManager.getMyGroups();
    }

    public void setRegisterState(boolean state){
        tcpClientManager.setRegister(state);
    }

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
        udpClientManager.start();

        while(!exit){

            udpClientManager.askForServerConnection();
            if(checkTcpPort()){

                try {

                    exit = true;
                    connectTcp(Constants.SERVER_ADDRESS,udpClientManager.getServerTcpPort());
                    tcpClientManager = new TcpClientManager(input,output);
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


        output.writeObject(new Message(Message.Type.CONNECT_TCP,"Client Connected"));
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

            output.writeObject(new Message(Message.Type.CHANGE_PASSWORD,auxUser));
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

    public void listGroups(){
        try {
            output.writeObject(new Message(Message.Type.LIST_GROUPS));
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void listMyGroups(){
        try {
            output.writeObject(new Message(Message.Type.LIST_MYGROUPS,user));
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void listMyAdminGroups(){
        try {
            output.writeObject(new Message(Message.Type.LIST_MYADMINGROUPS,user));
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void listRequest(){
        try {
            output.writeObject(new Message(Message.Type.LIST_REQUEST,user));
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createGroup(String name, User admin){

        Group group = new Group(admin,name);

        try {

            output.writeObject(new Message(Message.Type.CREATE_GROUP,group));
            output.flush();

        } catch (IOException e) {
            e.printStackTrace();

        }

    }

    public void editGroup(String name,String newName, User user){
        try {
            output.writeObject(new Message(Message.Type.EDIT_GROUP,name,newName,user));
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendResquestGroup(int idGroup, User user){
        try {
            output.writeObject(new Message(Message.Type.GROUP_REQUEST,idGroup,user));
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendResquestResponse(Request request, User user){
        try {
            output.writeObject(new Message(Message.Type.GROUP_REQUEST_RESPONSE,request,user));
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void exitGroup(int id, User user){
        try {
            output.writeObject(new Message(Message.Type.GROUP_EXIT,id,user));
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeMember(int groupId, int userId, User user){
        try {
            output.writeObject(new Message(Message.Type.REMOVE_MEMBER,groupId,userId,user));
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteGroup(int id, User user){
        try {
            output.writeObject(new Message(Message.Type.DELETE_GROUP,id,user));
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
