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

}

