package pt.isec.PD.Data.Models;

import java.io.Serializable;
import java.util.ArrayList;

public class Group implements Serializable {

    private int id;
    private User admnistrator;
    private String name;
    private ArrayList<User> members;

    public Group(int id,User admnistrator,String name){
        this.id = id;
        this.admnistrator = admnistrator;
        this.name = name;
        this.members = new ArrayList<>();
    }
    public Group(int id,String name){
        this.id = id;
        this.name = name;
        this.members = new ArrayList<>();
    }

    public Group(User admnistrator,String name){
        this.admnistrator = admnistrator;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public User getAdmnistrator() {
        return admnistrator;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<User> getMembers() {return members; }

    public  void addMember(User user){members.add(user); }

    public void setId(int id) {
        this.id = id;
    }
}
