package pt.isec.PD.Server.Database;

import java.sql.*;

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

    public boolean Register(String nome, String username) {

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

        //Insert dos dados na Database, 1: Meter o id a contar do 0 crescentemente, e meter username,pass,nome introduzidos, coonectado a falso ou seja 0
        return true;

    }

    public boolean Login(String username, String password) {

        try {

            ResultSet resultSet = statement.executeQuery("select * from utilizador");

            while (resultSet.next()) {
                if (username.equals(resultSet.getString("username")) && password.equals(resultSet.getString("password"))) {
                    //Meter o conectado do utilizador a true;
                    return true;
                }
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return false;
    }

}

