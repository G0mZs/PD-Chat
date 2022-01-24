package pt.isec.PD.Data.Models;

import java.io.Serializable;

public class Request implements Serializable {
    private int idUser, idGroup;
    private String userName, groupName;

    public Request(int idUser, int idGroup) {
        this.idUser = idUser;
        this.idGroup = idGroup;
    }

    public Request(int idUser, int idGroup, String userName, String groupName) {
        this.idUser = idUser;
        this.idGroup = idGroup;
        this.userName = userName;
        this.groupName = groupName;
    }

    public Request(String username,int idGroup){
        this.userName = username;
        this.idGroup = idGroup;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public int getIdGroup() {
        return idGroup;
    }

    public void setIdGroup(int idGroup) {
        this.idGroup = idGroup;
    }
}