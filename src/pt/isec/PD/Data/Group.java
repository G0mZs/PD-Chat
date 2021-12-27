package pt.isec.PD.Data;

import java.util.ArrayList;

public class Group {

    private int id;
    private User admnistrator;
    private String name;
    private ArrayList<User> members;

    public Group(int id,User admnistrator,String name){
        this.id = id;
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

}
