package pt.isec.PD.Server.Database;

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

            ResultSet resultSet = statement.executeQuery("select * from utilizador");

            while (resultSet.next()) {
                if (id == resultSet.getInt("idUtilizadores")) {
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
                        statement.executeUpdate("UPDATE utilizador_has_utilizador SET aceite = 1 WHERE Utilizador_idUtilizadores = " + sender + " AND Utilizador_idUtilizadores1 = " + receiver + " AND aceite = " + "2 ;");
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
                        statement.executeUpdate("UPDATE utilizador_has_utilizador SET aceite = 0 WHERE Utilizador_idUtilizadores = " + sender + " AND Utilizador_idUtilizadores1 = " + receiver + " AND aceite = " + "2 ;");
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

    public boolean removeContact(int idUser,int idContact){

        try {
            ResultSet resultSet = statement.executeQuery("select * from utilizador_has_utilizador");

            while (resultSet.next()){
                if(resultSet.getInt("Utilizador_idUtilizadores") == idUser && resultSet.getInt("Utilizador_idUtilizadores1") == idContact && resultSet.getInt("aceite") == 1){
                    statement.executeUpdate("DELETE FROM mydb.utilizador_has_utilizador WHERE Utilizador_idUtilizadores = " + idUser + " AND Utilizador_idUtilizadores1 = " + idContact + " AND aceite = 1;" );
                    return true;
                }
                else if(resultSet.getInt("Utilizador_idUtilizadores") == idContact && resultSet.getInt("Utilizador_idUtilizadores1") == idUser && resultSet.getInt("aceite") == 1){
                    statement.executeUpdate("DELETE FROM mydb.utilizador_has_utilizador WHERE Utilizador_idUtilizadores = " + idContact + " AND Utilizador_idUtilizadores1 = " + idUser + " AND aceite = 1;");
                    return true;
                }
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return false;
    }

}

