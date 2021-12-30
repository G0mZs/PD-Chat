package pt.isec.PD.Server.Database;

import pt.isec.PD.Data.Group;
import pt.isec.PD.Data.User;

import java.sql.*;
import java.util.ArrayList;

/**
     * <p>DbHelper class.</p>
     *
     * @author
     * @version $Id: $Id
     */
    public class DbHelper {

    private String dbAddress;
    private String dbName;
    private String user;
    private String pass;
    private Connection connection;
    private Connection connection2;
    private Connection connection3;
    private Statement statement;
    private Statement statement2;
    private Statement statement3;

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
            connection2 = DriverManager.getConnection(dbUrl, user, pass);
            connection3 = DriverManager.getConnection(dbUrl, user, pass);

            statement = connection.createStatement();
            statement2 = connection2.createStatement();
            statement3 = connection3.createStatement();

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
                if (username.equals(resultSet.getString("username")) && password.equals(resultSet.getString("password"))) {
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
            statement.executeUpdate("UPDATE utilizador SET conectado = 1 WHERE username = '" + username + "';");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void userDisconnected(int id){
        try {
            statement.executeUpdate("UPDATE utilizador SET conectado = 0 WHERE idUtilizadores = " + id + ";");
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
                    User auxUser = new User(resultSet.getInt("idUtilizadores"),resultSet.getString("username"),resultSet.getString("password"),resultSet.getString("nome"));
                    auxUser.setConnected(resultSet.getBoolean("conectado"));
                    return auxUser;
                }
            }

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

    public ArrayList<Group> getAllGroups(){

        ArrayList<Group> groups = new ArrayList<>();

        try {
            ResultSet resultSetGroup = statement.executeQuery("select * from Grupo");

            while (resultSetGroup.next()){
                int idGrupo = resultSetGroup.getInt("idGrupos");
                String nomeGrupo = resultSetGroup.getString("nome");
                System.out.println(nomeGrupo);
                ResultSet resultSetAdmin = statement2.executeQuery("select * from utilizador where idUtilizadores="+resultSetGroup.getInt("idAdmnistrador"));
                if(!resultSetAdmin.next())
                    continue;
                User admin = new User(resultSetAdmin.getInt("idUtilizadores"),resultSetAdmin.getString("username"),null,resultSetAdmin.getString("nome"));
                resultSetAdmin.close();
                Group auxGroup = new Group(idGrupo,admin,nomeGrupo);

                ResultSet resultSetUsers =  statement3.executeQuery(
                        "select * " +
                            "from Grupo_has_Utilizador " +
                            "inner join utilizador on Utilizador_idUtilizadores=idUtilizadores "+
                            "where Grupo_idGrupos="+auxGroup.getId()+" and "+
                            "aceite=1");
                while (resultSetUsers.next()){
                    User auxUser = new User(resultSetUsers.getInt("idUtilizadores"),resultSetUsers.getString("username"),null,resultSetUsers.getString("nome"));
                    auxGroup.addMember(auxUser);
                }
                resultSetUsers.close();
                groups.add(auxGroup);
            }
            resultSetGroup.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return groups;
    }

    public Boolean createGroup(Group group){

        try {
            ResultSet resultSetGroupExist = statement.executeQuery("select * from Grupo where nome='"+group.getName()+"' and idAdmnistrador='"+group.getAdmnistrator().getId()+"'");
            if(resultSetGroupExist.next())
                return false;
            System.out.println();
            statement.executeUpdate("INSERT INTO Grupo (idAdmnistrador,nome) Values("+group.getAdmnistrator().getId()+",'"+group.getName()+"')");
            ResultSet resultSetGroup = statement.executeQuery("select * from Grupo where nome='"+group.getName()+"' and idAdmnistrador='"+group.getAdmnistrator().getId()+"'");
            resultSetGroup.next();
            group.setId(resultSetGroup.getInt("idGrupos"));
            addMember(group.getAdmnistrator().getId(),group.getId(),true);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return  true;
    }

    public Boolean addMember(int userId,int groupId,boolean join){

        try {
            ResultSet resultSetMemberExist = statement.executeQuery("select * from Grupo_has_Utilizador where Utilizador_idUtilizadores="+userId+" and Grupo_idGrupos="+groupId);
            if(resultSetMemberExist.next())
                return false;
            statement.executeUpdate("INSERT INTO Grupo_has_Utilizador Values((Select idGrupos from Grupo where idGrupos="+groupId+"),(Select idUtilizadores from Utilizador where idUtilizadores="+userId+"),"+ (join? 1 : 0) +")");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }

        return  true;
    }

    public Boolean editGroupName(String name, String newName, User user){

        try {
            ResultSet resultSetIsUserAdmin = statement.executeQuery("select * from Grupo where idAdmnistrador='"+user.getId()+"' and (nome='"+name+"' or nome='"+newName+"')");
            if(!resultSetIsUserAdmin.next())
                return false;
            statement2.executeUpdate("Update Grupo Set nome='"+newName+"' where idGrupos="+resultSetIsUserAdmin.getInt("idGrupos"));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
        return  true;
    }



    public Boolean receiveGroupRequest(int idGroup, User user){

        try {
            ResultSet resultSetGrupo = statement.executeQuery("select * from Grupo where idGrupos="+idGroup);
            if(!resultSetGrupo.next())
                return false;
            ResultSet resultSetUserIsMember = statement.executeQuery("select * from grupo_has_utilizador where Utilizador_idUtilizadores="+user.getId()+" and Grupo_idGrupos="+idGroup);
            if(resultSetUserIsMember.next())
                return false;
            addMember(user.getId(),idGroup,false);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
        return  true;
    }

}

