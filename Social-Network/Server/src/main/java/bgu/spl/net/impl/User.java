package bgu.spl.net.impl;

public class User {
    private String name;
    private String password;
    private String birthday;
    private int age;
    private int connId;
    private int numofposts;
    private int numoffollowers;
    private int numoffollowing;
    public User(String _name, String _password,String _birthday,int _connId){
        name=_name;
        password=_password;
        birthday=_birthday;
        connId=_connId;
        age=2022-Integer.parseInt(_birthday.substring(6));
        numofposts=0;
        numoffollowers=0;
        numoffollowing=0;

    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setConnId(int connId) {
        this.connId = connId;
    }

    public void setNumofposts(int numofposts) {
        this.numofposts = numofposts;
    }

    public void setNumoffollowers(int numoffollowers) {
        this.numoffollowers = numoffollowers;
    }

    public void setNumoffollowing(int numoffollowing) {
        this.numoffollowing = numoffollowing;
    }

    public int getNumofposts() {
        return numofposts;
    }

    public int getNumoffollowers() {
        return numoffollowers;
    }

    public int getNumoffollowing() {
        return numoffollowing;
    }

    public int getConnId() {
        return connId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getBirthday() {
        return birthday;
    }
}
