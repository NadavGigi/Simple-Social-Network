package bgu.spl.net.srv;

import bgu.spl.net.impl.BGSServer.ConnectionsImpl;
import bgu.spl.net.impl.Messages.*;
import bgu.spl.net.impl.User;

import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

public class DataBase {



    //Singelton Pattern
    private static class Holder{
        private static DataBase instance = new DataBase();
        public static boolean initialized;
    }

    private ConcurrentHashMap<String, User> Users;
    private Vector<String> loggedIn;
    private ConcurrentHashMap<String,Vector<User>> followers;
    private ConcurrentHashMap<String, Vector<PostMessage>> Posts;
    private ConcurrentHashMap<String, Vector<PMMessage>> PM;
    private ConcurrentHashMap<String,Vector<User>> whoIfollow;
    private ConcurrentHashMap<String,Vector<User>> whoIblocked;
    private ConcurrentHashMap<User,Integer> connections;
    private ConnectionsImpl conn;
    private Vector<String> filters;
    private int usersCounter;//will represent the connId for the connection
    private DataBase() {
        Users=new ConcurrentHashMap<String,User>();
        loggedIn=new Vector<String>();
        followers=new ConcurrentHashMap<String,Vector<User>>();
        Posts=new ConcurrentHashMap<String, Vector<PostMessage>>();//the key is the one that publish the post
        PM=new ConcurrentHashMap<String, Vector<PMMessage>>();//the key is the reciever
        whoIfollow=new ConcurrentHashMap<String,Vector<User>>();
        whoIblocked=new ConcurrentHashMap<String,Vector<User>>();
        filters=new Vector<String>();//TODO
        filters.add("trump");filters.add("TRUMP");filters.add("WAR");filters.add("BLOOD");filters.add("war");
        conn=ConnectionsImpl.getInstance();
        connections=new ConcurrentHashMap<User,Integer>();
        usersCounter=0;
    }
    public boolean isConnect(String s){
        if(Users.get(s)!=null){
            if(connections.get(Users.get(s))!=null)
                return true;
        }
        return false;
    }
    public static DataBase getInstance() {
        return Holder.instance;
    }
    public void addConnection(String userName, int connId) {
        connections.put(Users.get(userName),connId);
    }
    public int getUserConnId(String userName){
        return connections.get(Users.get(userName));
    }

    public String getUserPassword(String userName){
        return Users.get(userName).getPassword();
    }

    public void setLoggedIn(String userName){
        loggedIn.add(userName);
    }

    public boolean isLoggedIn(String userName){
        return loggedIn.contains(userName);
    }

    public boolean isRegistered(String userName){
        return Users.containsKey(userName);
    }
    public boolean registerUser(String userName, String password, String birthday){
        if(isRegistered(userName))
            return false;
        User user=new User(userName,password,birthday,usersCounter);
        usersCounter++;
        Users.put(userName,user);
        followers.put(userName,new Vector<User>());
        Posts.put(userName,new Vector<PostMessage>());
        PM.put(userName,new Vector<PMMessage>());
        whoIfollow.put(userName,new Vector<User>());
        return true;
    }

    public boolean checkPassword(String userName, String password){
        if(Users.get(userName)!=null&&password.equals(Users.get(userName).getPassword()))
           return true;
        return false;
    }


    public boolean logUserIn(String userName, String password){
        if(isLoggedIn(userName))
            return false;
        if(checkPassword(userName,password)){
            setLoggedIn(userName);
            return true;
        }
        return false;
    }

    public boolean logUserOut(String userName){
        return loggedIn.remove(userName);
    }
    public boolean Follow(String userName,String ToFollow){
        if(!isRegistered(userName)
                ||!isLoggedIn(userName)
                ||!isRegistered(ToFollow)
                ||isBlocked(userName,ToFollow)
                ||followers.get(ToFollow).contains(Users.get(userName)))
            return false;
        followers.get(ToFollow).add(Users.get(userName));
        whoIfollow.get(userName).add(Users.get(ToFollow));
        Users.get(userName).setNumoffollowing(Users.get(userName).getNumoffollowing()+1);
        Users.get(ToFollow).setNumoffollowers(Users.get(ToFollow).getNumoffollowers()+1);
        return true;
    }
    public boolean unFollow(String userName,String TounFollow){
        if(!isRegistered(userName)||!isLoggedIn(userName)
                ||!isRegistered(TounFollow)
                ||isBlocked(userName,TounFollow)||
        !followers.get(TounFollow).contains(Users.get(userName)))
            return false;
        followers.get(TounFollow).remove(Users.get(userName));
        whoIfollow.get(userName).remove(Users.get(TounFollow));
        Users.get(userName).setNumoffollowing(Users.get(userName).getNumoffollowing()-1);
        Users.get(TounFollow).setNumoffollowers(Users.get(TounFollow).getNumoffollowers()-1);
        return true;
    }
    public boolean AddPM(PMMessage msg){
        if(!isRegistered(msg.getUserName())
                ||!isLoggedIn(msg.getUserName())
                ||!isRegistered(msg.getWhotoSend())
                ||isBlocked(msg.getUserName(),msg.getWhotoSend()))
            return false;
        String[] temp=msg.getContent().split(" ");
        String newContent="";
        for(int i=0;i<temp.length;i++){
            if(filters.contains(temp[i]))
                newContent=newContent+"<filtered> ";
            else
                newContent=newContent+temp[i]+" ";
        }
        msg.setContent(newContent);
        PM.get(msg.getUserName()).add(msg);
        if(connections.get(Users.get(msg.getWhotoSend()))!=null)
            conn.send(connections.get(Users.get(msg.getWhotoSend())),
                    new ClientMessage("NOTIFICATION PM "+" "+msg.getUserName()+
                             " "+msg.getContent()+" "+msg.getDate()+" ",(short)9));
        return true;
    }


    public ConcurrentHashMap<User, Integer> getConnections() {
        return connections;
    }

    //TODO FIXING EVERYTHING
    public boolean AddPost(String userName,PostMessage msg){
        if(!isLoggedIn(userName)||!isRegistered(userName))
            return false;
        Users.get(userName).setNumofposts(Users.get(userName).getNumofposts()+1);
        for(User user:followers.get(userName)) {
            if (connections.get(user) != null && !isBlocked(user.getName(), userName)) {
                if(msg.getUsers().contains(user.getName())){
                    msg.removeUser(user.getName());
                }
                conn.send(connections.get(user),
                        new ClientMessage("NOTIFICATION Public" + " " + userName + " " + msg.getContent() + " ", (short) 9));
            }
        }
        for(String S:msg.getUsers()) {
            if (Users.get(S)!=null&&connections.get(Users.get(S)) != null && !isBlocked(S, userName)) {
                conn.send(connections.get(Users.get(S)),
                        new ClientMessage("NOTIFICATION Public" + " " + userName + " " + msg.getContent() + " ", (short) 9));
            }
        }

        Posts.get(userName).add(msg);
        return true;
    }
    public boolean getSTAT(StatMessage msg){
        String name=msg.getUserName();
        String newContent="";
        if(isRegistered(name)&&isLoggedIn(name)){
            for(String s :msg.getUsernames()){
                User user=Users.get(s);
                if(user!=null&&!isBlocked(msg.getUserName(),s)) {
                    newContent = newContent + " " + user.getName() + " " + user.getAge() + " " + user.getNumofposts() + " " +
                                user.getNumoffollowers() + " " + user.getNumoffollowing() + " \n";
                }
            }
            msg.setContent(newContent);
            return true;
        }
        return false;
    }
    public boolean LogStat(LogstatMessage lgm){
        if(isRegistered(lgm.getUserName())&&isLoggedIn(lgm.getUserName())) {
            String newContent;
            newContent="";
            if (loggedIn.size() == 0) {
                lgm.setContent(newContent);
                return true;
            } else {
                for (String s : loggedIn) {
                    User user=Users.get(s);
                    if(user!=null&&!isBlocked(lgm.getUserName(),s))
                        newContent = newContent + " " + user.getName() + " " + user.getAge() + " " + user.getNumofposts() + " " +
                                user.getNumoffollowers() + " " + user.getNumoffollowing() + " \n";
                }
                lgm.setContent(newContent);
                return true;
            }
        }
        return false;
    }
    public boolean Block(BlockMessage bm) {
        if(!isRegistered(bm.getUserName())||!isRegistered(bm.getUserToBlock()))
            return false;
        whoIfollow.get(bm.getUserToBlock()).remove(bm.getUserName());
        whoIfollow.get(bm.getUserName()).remove(bm.getUserToBlock());
        followers.get(bm.getUserName()).remove(bm.getUserToBlock());
        followers.get(bm.getUserToBlock()).remove(bm.getUserName());
        if(!whoIblocked.contains(bm.getUserName()))
            whoIblocked.put(bm.getUserName(),new Vector<User>());
        if(whoIblocked.get(bm.getUserName())
                .contains(Users.get(bm.getUserToBlock())))
           return true;
        whoIblocked.get(bm.getUserName()).add(Users.get(bm.getUserToBlock()));
        return true;
    }
    public boolean isBlocked(String user1,String user2){
        if(whoIblocked.contains(user1)){
            if(whoIblocked.get(user1).contains(Users.get(user2)))
                return true;
        }
        else if(whoIblocked.contains(user2)){
            if(whoIblocked.get(user2).contains(Users.get(user1)))
                return true;
        }
        return false;
    }


}