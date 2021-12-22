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

    public void connectDatabase(){

        try {
            String dbUrl = "jdbc:mysql://" + dbAddress + "/" + dbName;

            connection = DriverManager.getConnection(dbUrl,user,pass);

            statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("select * from utilizador");

            while(resultSet.next()){
                System.out.println(resultSet.getString("username"));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }












}
