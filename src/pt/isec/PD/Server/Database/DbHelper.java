package pt.isec.PD.Server.Database;

import pt.isec.PD.Data.Group;
import pt.isec.PD.Data.Message;
import pt.isec.PD.Data.User;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;



    public class DbHelper {

    private String dbAddress;
    private String dbName;
    private String user;
    private String pass;
    private Connection connection;
    private Statement statement;

    public DbHelper(String dbAddress, String dbName, String user, String pass) {

        this.dbAddress = dbAddress;
        this.dbName = dbName;
        this.user = user;
        this.pass = pass;
    }

    public void connectDatabase() {

        try {

            String dbUrl = "jdbc:mysql://" + dbAddress + "/" + dbName;

            connection = DriverManager.getConnection(dbUrl, user, pass);

            statement = connection.createStatement();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public boolean Register(String nome, String username,String pass) {

        try {

            ResultSet resultSet = statement.executeQuery("select * from utilizador");

            while (resultSet.next()) {
                if (nome.equals(resultSet.getString("nome")) || username.equals(resultSet.getString("username"))) {
                    return false;
                }
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        int id = generateId();
        insertUser(id,nome,username,pass,0);
        return true;

    }

    public boolean Login(String username, String password) {

        try {

            ResultSet resultSet = statement.executeQuery("select * from utilizador");

            while (resultSet.next()) {
                if (username.equals(resultSet.getString("username")) && password.equals(resultSet.getString("password")) && resultSet.getInt("conectado") == 0) {
                    userConnected(username);
                    return true;
                }
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return false;
    }

    public int generateId(){

        int lastId = 0;
        try {

            ResultSet resultSet = statement.executeQuery("select * from utilizador");

            while(resultSet.next()){
                lastId = resultSet.getInt("idUtilizadores");
            }


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return lastId + 1;
    }

    public void insertUser(int id,String name,String username,String pass,int conectado){


        String combined = id +"," +"'" + username + "'," + "'" + pass + "'," + "'" + name + "'," + conectado;

        try {
            statement.executeUpdate("INSERT INTO `mydb`.`utilizador`(`idUtilizadores`,`username`,`password`,`nome`,`conectado`) VALUES (" + combined + ");");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public String getName(String username){

        String name = null;

        try {

            ResultSet resultSet = statement.executeQuery("select * from utilizador");

            while(resultSet.next()){
               if(username.equals(resultSet.getString("username"))){
                   name = resultSet.getString("nome");
               }
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return name;
    }

    public int getId(String username){

        int id = 0;

        try {

            ResultSet resultSet = statement.executeQuery("select * from utilizador");

            while(resultSet.next()){
                if(username.equals(resultSet.getString("username"))){
                    id = resultSet.getInt("idUtilizadores");
                }
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return id;
    }

    public void userConnected(String username){

        try {
            Statement st1 = connection.createStatement();
            st1.executeUpdate("UPDATE utilizador SET conectado = 1 WHERE username = '" + username + "';");
            st1.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void userDisconnected(int id){
        try {
            Statement st1 = connection.createStatement();
            statement.executeUpdate("UPDATE utilizador SET conectado = 0 WHERE idUtilizadores = " + id + ";");
            st1.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void changePassword(int id,String password){
        try {
            statement.executeUpdate("UPDATE utilizador SET password ='" + password + "' WHERE idUtilizadores = " + id + ";");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void changeUsername(int id,String username){
        try {
            statement.executeUpdate("UPDATE utilizador SET username ='" + username + "' WHERE idUtilizadores = " + id + ";");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void changeName(int id,String name){
        try {
            statement.executeUpdate("UPDATE utilizador SET nome ='" + name + "' WHERE idUtilizadores = " + id + ";");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public boolean checkName(String nome){

        try {

            ResultSet resultSet = statement.executeQuery("select * from utilizador");

            while (resultSet.next()) {
                if (nome.equals(resultSet.getString("nome"))) {
                    return false;
                }
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return true;
    }

    public boolean checkUsername(String username){

        try {

            ResultSet resultSet = statement.executeQuery("select * from utilizador");

            while (resultSet.next()) {
                if (username.equals(resultSet.getString("username"))) {
                    return false;
                }
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return true;
    }

    public User searchUser(String username){

        try {

            ResultSet resultSet = statement.executeQuery("select * from utilizador");

            while (resultSet.next()) {
                if (username.equals(resultSet.getString("username"))) {
                    User auxUser = new User(resultSet.getInt("idUtilizadores"),resultSet.getString("username"),null,resultSet.getString("nome"));
                    auxUser.setConnected(resultSet.getBoolean("conectado"));
                    return auxUser;
                }
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return null;

    }


    public User getUser(int id){

        try {
            Statement st1 = connection.createStatement();
            ResultSet resultSet = st1.executeQuery("select * from utilizador");

            while (resultSet.next()) {
                if (id == resultSet.getInt("idUtilizadores")) {
                    User auxUser = new User(resultSet.getInt("idUtilizadores"),resultSet.getString("username"),null,resultSet.getString("nome"));
                    auxUser.setConnected(resultSet.getBoolean("conectado"));
                    return auxUser;
                }
            }
            st1.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return null;

    }

    public ArrayList<User> getAllUsers(){

        ArrayList<User> users = new ArrayList<>();

        try {

            ResultSet resultSet = statement.executeQuery("select * from utilizador");

            while (resultSet.next()){
                User auxUser = new User(resultSet.getInt("idUtilizadores"),resultSet.getString("username"),null,resultSet.getString("nome"));
                auxUser.setConnected(resultSet.getBoolean("conectado"));
                users.add(auxUser);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return users;
    }

    public void createContactRequest(int sender,int receiver){

        String combined = sender + "," + receiver + "," + "2";

        try {
            statement.executeUpdate("INSERT INTO `mydb`.`utilizador_has_utilizador`(`Utilizador_idUtilizadores`,`Utilizador_idUtilizadores1`,`aceite`) VALUES (" + combined + ");");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public boolean checkRequest(int sender,int receiver){

        try {
            ResultSet resultSet = statement.executeQuery("select * from utilizador_has_utilizador");

            while (resultSet.next()){
                if(resultSet.getInt("Utilizador_idUtilizadores") == sender && resultSet.getInt("Utilizador_idUtilizadores1") == receiver && resultSet.getInt("aceite") != 0 || resultSet.getInt("Utilizador_idUtilizadores") == receiver && resultSet.getInt("Utilizador_idUtilizadores1") == sender && resultSet.getInt("aceite") != 0){
                    return false;
                }
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return true;

    }

    public boolean requestTrue(int sender,int receiver){
        try {
            ResultSet resultSet = statement.executeQuery("select * from utilizador_has_utilizador");

            while (resultSet.next()){
                if(resultSet.getInt("Utilizador_idUtilizadores") == sender && resultSet.getInt("Utilizador_idUtilizadores1") == receiver && resultSet.getInt("aceite") == 2){

                    try {
                        Statement st1 = connection.createStatement();
                        st1.executeUpdate("UPDATE utilizador_has_utilizador SET aceite = 1 WHERE Utilizador_idUtilizadores = " + sender + " AND Utilizador_idUtilizadores1 = " + receiver + " AND aceite = " + "2 ;");
                        st1.close();
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                    return true;
                }
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return false;
    }

    public boolean requestFalse(int sender,int receiver){
        try {
            ResultSet resultSet = statement.executeQuery("select * from utilizador_has_utilizador");

            while (resultSet.next()){
                if(resultSet.getInt("Utilizador_idUtilizadores") == sender && resultSet.getInt("Utilizador_idUtilizadores1") == receiver && resultSet.getInt("aceite") == 2){

                    try {
                        Statement st1 = connection.createStatement();
                        st1.executeUpdate("UPDATE utilizador_has_utilizador SET aceite = 0 WHERE Utilizador_idUtilizadores = " + sender + " AND Utilizador_idUtilizadores1 = " + receiver + " AND aceite = " + "2 ;");
                        st1.close();
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                    return true;
                }
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return false;
    }

    public ArrayList<User> getContacts(int id){

        ArrayList<Integer> idLists = new ArrayList<>();
        ArrayList<User> contacts = new ArrayList<>();

        try {

            ResultSet resultSet = statement.executeQuery("select * from utilizador_has_utilizador");

            while(resultSet.next()){
                if(resultSet.getInt("Utilizador_idUtilizadores") == id && resultSet.getInt("aceite") == 1){
                    idLists.add(resultSet.getInt("Utilizador_idUtilizadores1"));
                }else if(resultSet.getInt("Utilizador_idUtilizadores1") == id && resultSet.getInt("aceite") == 1){
                    idLists.add(resultSet.getInt("Utilizador_idUtilizadores"));
                }
            }

            ResultSet resultSet1 = statement.executeQuery("select * from utilizador");

            while(resultSet1.next()){

                int i;
                for(i = 0; i < idLists.size(); i++){
                    if(resultSet1.getInt("idUtilizadores") == idLists.get(i)){
                        User user = new User(resultSet1.getInt("idUtilizadores"),resultSet1.getString("username"),null,resultSet1.getString("nome"));

                            if(resultSet1.getInt("conectado") == 1){
                                user.setConnected(true);
                            }else{
                                user.setConnected(false);
                            }

                        contacts.add(user);
                    }
                }
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return contacts;

    }

    public ArrayList<User> getPendingRequests(int id){

        ArrayList<Integer> idLists = new ArrayList<>();
        ArrayList<User> pendingRequests = new ArrayList<>();

        try {

            ResultSet resultSet = statement.executeQuery("select * from utilizador_has_utilizador");

            while(resultSet.next()){
                if(resultSet.getInt("Utilizador_idUtilizadores1") == id && resultSet.getInt("aceite") == 2){
                    idLists.add(resultSet.getInt("Utilizador_idUtilizadores"));
                }
            }

            ResultSet resultSet1 = statement.executeQuery("select * from utilizador");

            while(resultSet1.next()){

                int i;
                for(i = 0; i < idLists.size(); i++){
                    if(resultSet1.getInt("idUtilizadores") == idLists.get(i)){
                        User user = new User(resultSet1.getInt("idUtilizadores"),resultSet1.getString("username"),null,resultSet1.getString("nome"));

                        pendingRequests.add(user);
                    }
                }
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return pendingRequests;

    }

    public ArrayList<Message> getHistoric(int id1,int id2){


        ArrayList<Message> historic = new ArrayList<>();

        try {

            ResultSet resultSet = statement.executeQuery("select * from mensagem");

            while(resultSet.next()){
                if(resultSet.getInt("IdAutor") == id1 && resultSet.getInt("IdReceiver") == id2){

                    User author = getUser(resultSet.getInt("IdAutor"));

                    String data = resultSet.getString("Data");
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    LocalDateTime dateTime = LocalDateTime.parse(data,formatter);

                    Message msg = new Message(resultSet.getInt("idMensagem"),author,resultSet.getString("tipo"),resultSet.getString("mensagem"),dateTime,resultSet.getString("estado"));

                    historic.add(msg);
                }else if(resultSet.getInt("IdAutor") == id2 && resultSet.getInt("IdReceiver") == id1){

                    User author = getUser(resultSet.getInt("IdAutor"));

                    String data = resultSet.getString("Data");
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    LocalDateTime dateTime = LocalDateTime.parse(data,formatter);

                    Message msg = new Message(resultSet.getInt("idMensagem"),author,resultSet.getString("tipo"),resultSet.getString("mensagem"),dateTime,resultSet.getString("estado"));

                    historic.add(msg);
                }
            }


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return historic;
    }

    public boolean removeContact(int idUser,int idContact){

        try {
            ResultSet resultSet = statement.executeQuery("select * from utilizador_has_utilizador");

            while (resultSet.next()){
                if(resultSet.getInt("Utilizador_idUtilizadores") == idUser && resultSet.getInt("Utilizador_idUtilizadores1") == idContact && resultSet.getInt("aceite") == 1){
                    Statement st1 = connection.createStatement();
                    st1.executeUpdate("DELETE FROM mydb.utilizador_has_utilizador WHERE Utilizador_idUtilizadores = " + idUser + " AND Utilizador_idUtilizadores1 = " + idContact + " AND aceite = 1;" );
                    st1.close();
                    return true;
                }
                else if(resultSet.getInt("Utilizador_idUtilizadores") == idContact && resultSet.getInt("Utilizador_idUtilizadores1") == idUser && resultSet.getInt("aceite") == 1){
                    Statement st1 = connection.createStatement();
                    statement.executeUpdate("DELETE FROM mydb.utilizador_has_utilizador WHERE Utilizador_idUtilizadores = " + idContact + " AND Utilizador_idUtilizadores1 = " + idUser + " AND aceite = 1;");
                    st1.close();
                    return true;
                }
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return false;
    }

    public boolean verifyContact(int sender,int receiver){

        try {
            ResultSet resultSet = statement.executeQuery("select * from utilizador_has_utilizador");

            while (resultSet.next()){
                if(resultSet.getInt("Utilizador_idUtilizadores") == sender && resultSet.getInt("Utilizador_idUtilizadores1") == receiver && resultSet.getInt("aceite") == 1 || resultSet.getInt("Utilizador_idUtilizadores") == receiver && resultSet.getInt("Utilizador_idUtilizadores1") == sender && resultSet.getInt("aceite") == 1 ){

                    return true;
                }
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return false;
    }

    public void addMessage(int idSender, int idReceiver, String type, String message, LocalDateTime localDateTime,String state){

        String dateTime = localDateTime.toString();
        int idMessage = generateMessageId();

        String combined = idMessage + "," + idSender + "," + idReceiver + "," + "NULL" + ",'" + type + "','" + message + "','" + dateTime + "','" + state + "'";

        try {
            statement.executeUpdate("INSERT INTO `mydb`.`mensagem`(`idMensagem`,`IdAutor`,`IdReceiver`,`idGrupo`,`tipo`,`mensagem`,`Data`,`estado`) VALUES (" + combined + ");");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public int generateMessageId(){

        int lastId = 0;
        try {

            ResultSet resultSet = statement.executeQuery("select * from mensagem");

            while(resultSet.next()){
                lastId = resultSet.getInt("idMensagem");
            }


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return lastId + 1;
    }

    public void updateHistoric(int idsender,int idreceiver){

        try {

            ResultSet resultSet = statement.executeQuery("select * from mensagem");

            while (resultSet.next()){

                if(resultSet.getInt("IdAutor") == idreceiver && resultSet.getInt("IdReceiver") == idsender){

                    seenMessage(idreceiver);

                }
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void seenMessage(int idreceiver){

        try {
            Statement st = connection.createStatement();
            st.executeUpdate("UPDATE mensagem SET estado = 'Vista' WHERE IdAutor = " + idreceiver + ";");
            st.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public boolean checkMessageId(String idMessage){

        int idMsg = Integer.parseInt(idMessage);

        try {

            ResultSet resultSet = statement.executeQuery("select * from mensagem");

            while (resultSet.next()) {
                if (resultSet.getInt("idMensagem") == idMsg){
                    return true;
                }
            }

        }catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return false;
    }

    public boolean checkMessageAuthor(String idMessage,int idAuthor){

        int idMsg = Integer.parseInt(idMessage);

        try {

            ResultSet resultSet = statement.executeQuery("select * from mensagem");

            while (resultSet.next()) {
                if (resultSet.getInt("idMensagem") == idMsg && resultSet.getInt("IdAutor") == idAuthor){
                    return true;
                }
            }

        }catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return false;
    }

    public void deleteMessage(String idMessage){

        int idMsg = Integer.parseInt(idMessage);

        try{

            statement.executeUpdate("DELETE FROM mydb.mensagem WHERE idMensagem = " + idMsg);

        }catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public ArrayList<Group> getAllGroups(){

        ArrayList<Group> groups = new ArrayList<>();
        int idGrupo;
        String nomeGrupo;

        try {

            Statement statement2 = connection.createStatement();
            Statement statement3 = connection.createStatement();
            ResultSet resultSetGroup = statement.executeQuery("select * from Grupo");

            while (resultSetGroup.next()){

                idGrupo = resultSetGroup.getInt("idGrupos");
                nomeGrupo = resultSetGroup.getString("nome");

                ResultSet resultSetAdmin = statement2.executeQuery("select * from utilizador where idUtilizadores="+resultSetGroup.getInt("idAdmnistrador"));
                if(!resultSetAdmin.next())
                    continue;

                User admin = new User(resultSetAdmin.getInt("idUtilizadores"),resultSetAdmin.getString("username"),null,resultSetAdmin.getString("nome"));
                resultSetAdmin.close();
                Group group = new Group(idGrupo,admin,nomeGrupo);

                ResultSet resultSetUsers =  statement3.executeQuery(
                        "select * " +
                                "from Grupo_has_Utilizador " +
                                "inner join utilizador on Utilizador_idUtilizadores=idUtilizadores "+
                                "where Grupo_idGrupos="+ group.getId() +" and "+
                                "aceite=1");

                while (resultSetUsers.next()){
                    User member = new User(resultSetUsers.getInt("idUtilizadores"),resultSetUsers.getString("username"),null,resultSetUsers.getString("nome"));
                    group.addMember(member);
                }
                resultSetUsers.close();
                groups.add(group);
            }
            resultSetGroup.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return groups;
    }

    public boolean checkGroupName(int idAdmin,String name){

        try{

            ResultSet resultSet = statement.executeQuery("select * from grupo");

            while (resultSet.next()){
                if(resultSet.getInt("idAdmnistrador") == idAdmin && resultSet.getString("nome").equals(name)){
                    return false;
                }
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return true;
    }

    public void createGroup(int idAdmin,String name){

        int idGroup = generateGroupId();

        try{

            statement.executeUpdate("INSERT INTO Grupo (idGrupos,idAdmnistrador,nome) Values(" + idGroup + ","+ idAdmin +",'" + name + "')");

        }catch (SQLException throwables) {
        throwables.printStackTrace();
        }
    }

    public int generateGroupId(){

        int lastId = 0;
        try {

            ResultSet resultSet = statement.executeQuery("select * from grupo");

            while(resultSet.next()){
                lastId = resultSet.getInt("idGrupos");
            }


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return lastId + 1;
    }

    public boolean checkGroupExistence(int idAdmin,String name){

        try{

            ResultSet resultSet = statement.executeQuery("select * from grupo");

            while (resultSet.next()){
                if(resultSet.getInt("idAdmnistrador") == idAdmin && resultSet.getString("nome").equals(name)){
                    return true;
                }
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return false;
    }

    public void changeGroupName(int idAdmin,String name,String newName){

        try{

            statement.executeUpdate("UPDATE grupo SET nome = '" + newName + "' WHERE idAdmnistrador = " + idAdmin + " AND nome = '" + name + "';");

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public boolean checkGroupId(int idGroup){
        try{

            ResultSet resultSet = statement.executeQuery("select * from grupo");

            while (resultSet.next()){
                if(resultSet.getInt("idGrupos") == idGroup){
                    return true;
                }
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return false;
    }

    public int getAdminId(int idGroup){

        int idAdmin = 0;

        try{

            ResultSet resultSet = statement.executeQuery("select * from grupo");

            while (resultSet.next()){
                if(resultSet.getInt("idGrupos") == idGroup){
                    idAdmin = resultSet.getInt("idAdmnistrador");
                    return idAdmin;
                }
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return idAdmin;
    }

    public boolean checkGroupRequests(int idGroup,int idRequester){

        try{
            ResultSet resultSet = statement.executeQuery("select * from grupo_has_utilizador");

            while(resultSet.next()){
                if(resultSet.getInt("Grupo_idGrupos") == idGroup && resultSet.getInt("Utilizador_idUtilizadores") == idRequester && resultSet.getInt("aceite") != 0){
                    return false;
                }
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return true;
    }

    public void addGroupRequest(int idGroup, int idRequester){

        String combined = idGroup + "," + idRequester + "," + "2";

        try {
            statement.executeUpdate("INSERT INTO `mydb`.`grupo_has_utilizador`(`Grupo_idGrupos`,`Utilizador_idUtilizadores`,`aceite`) VALUES (" + combined + ");");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /*
    public Boolean receiveGroupRequest(int idGroup, User user){

        try {
            ResultSet resultSetGrupo = statement.executeQuery("select * from Grupo where idGrupos="+idGroup);
            if(!resultSetGrupo.next())
                return false;
            ResultSet resultSetUserIsMember = statement.executeQuery("select * from grupo_has_utilizador where Utilizador_idUtilizadores="+user.getId()+" and Grupo_idGrupos="+idGroup);
            if(resultSetUserIsMember.next())
                return false;
            addGroupRequest(user.getId(),idGroup);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
        return  true;
    }

    public Boolean addGroupRequest(int userId, int groupId){

        try {
            ResultSet resultSetMemberExist = statement.executeQuery("select * from Grupo_has_Utilizador where Utilizador_idUtilizadores="+userId+" and Grupo_idGrupos="+groupId);
            if(resultSetMemberExist.next())
                return false;
            statement.executeUpdate("INSERT INTO Grupo_has_Utilizador Values((Select idGrupos from Grupo where idGrupos="+groupId+"),(Select idUtilizadores from Utilizador where idUtilizadores="+userId+"),0)");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }

        return  true;
    }

    public Boolean receiveGroupRequestResponse(Request request, User user){

        try {
            statement.executeUpdate("Update Grupo_has_Utilizador  Set aceite=1 where Utilizador_idUtilizadores="+request.getIdUser()+" and Grupo_idGrupos="+request.getIdGroup());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
        return  true;
    }

    public ArrayList<Request> getListRequest(User user){

        ArrayList<Request> requests = new ArrayList<>();
        try {
            ResultSet resultSetRequest = statement.executeQuery("select * from grupo_has_utilizador Inner Join grupo on idGrupos=Grupo_idGrupos Inner Join utilizador on idUtilizadores=Utilizador_idUtilizadores where aceite=0 and idAdmnistrador="+user.getId());
            while(resultSetRequest.next()){
                requests.add(new Request(
                        resultSetRequest.getInt("Utilizador_idUtilizadores"),
                        resultSetRequest.getInt("Grupo_idGrupos"),
                        resultSetRequest.getString("utilizador.nome"),
                        resultSetRequest.getString("grupo.nome")
                ));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            requests = new ArrayList<>();
        }
        return  requests;
    }

    public boolean exitGroup(int id,User user){
        try {
            ResultSet resultSetUserIsAdmin = statement.executeQuery("select * from grupo where idGrupos="+id+" and idAdmnistrador="+user.getId());
            if(resultSetUserIsAdmin.next())
                return false;
            statement.executeUpdate("Delete from grupo_has_utilizador where Grupo_idGrupos="+id+" and Utilizador_idUtilizadores="+user.getId());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
        return  true;
    }

    public ArrayList<Group> getMyGroups(User user){

        ArrayList<Group> groups = new ArrayList<>();
        try {
            ResultSet resultSetRequest = statement.executeQuery("select * from grupo_has_utilizador Inner Join grupo on idGrupos=Grupo_idGrupos Inner Join utilizador on idUtilizadores=Utilizador_idUtilizadores where aceite=1 and idUtilizadores="+user.getId());
            while(resultSetRequest.next()){
                groups.add(new Group(
                        resultSetRequest.getInt("idGrupos"),
                        resultSetRequest.getString("nome")
                ));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            groups = new ArrayList<>();
        }
        return  groups;
    }

    public boolean deleteGroup(int id,User user){
        try {
            ResultSet resultSetUserIsAdmin = statement.executeQuery("select * from grupo where idGrupos="+id+" and idAdmnistrador="+user.getId());
            if(!resultSetUserIsAdmin.next())
                return false;
            statement.executeUpdate("Delete from grupo_has_utilizador where Grupo_idGrupos="+id);
            statement.executeUpdate("Delete from grupo where idGrupos="+id);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
        return  true;
    }

    public boolean removeMember(int userId,int groupId,User user){
        try {
            ResultSet resultSetUserIsAdmin = statement.executeQuery("select * from grupo where idGrupos="+groupId+" and idAdmnistrador="+user.getId());
            System.out.println("select * from grupo where idGrupos="+groupId+" and idAdmnistrador="+user.getId());
            if(!resultSetUserIsAdmin.next())
                return false;
            statement.executeUpdate("Delete from grupo_has_utilizador where Grupo_idGrupos="+groupId+" and Utilizador_idUtilizadores="+userId);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
        return  true;
    }*/


}

