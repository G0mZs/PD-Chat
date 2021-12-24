package pt.isec.PD.Data;

import java.io.Serializable;

public class User implements Cloneable, Serializable {

    private static final long serialVersionUID = 1L;
    private int id;
    private String username;
    private String password;
    private String name;
    private Boolean isConnected;


    public User(int id,String username,String password,String name){
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.isConnected = false;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Boolean getState() {
        return isConnected;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setConnected(Boolean connected) {
        isConnected = connected;
    }

    public void setId(int id) {
        this.id = id;
    }
}
