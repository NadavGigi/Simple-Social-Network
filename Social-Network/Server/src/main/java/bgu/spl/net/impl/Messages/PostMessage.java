package bgu.spl.net.impl.Messages;

import bgu.spl.net.api.Message;
import bgu.spl.net.srv.DataBase;

import java.util.Vector;

public class PostMessage extends Message {
    private Vector<String> users;
    public PostMessage(String _userName,String _content) {
        super((short)5);
        userName=_userName;
        content=_content;
        users=new Vector<String>();
        String[] temp=content.split(" ");
        for(int i=0;i<temp.length;i++){
            if(temp[i].startsWith("@")&&db.isRegistered(temp[i].substring(1)))
                users.add(temp[i].substring(1));
        }
    }
    public String response() {
        if(db.AddPost(userName,this))
            return "ACK 5 "+getContent();
        return "ERROR 5 ";
    }

    public Vector<String> getUsers() {
        return users;
    }

    public String getUserName() {
        return userName;
    }
    public void removeUser(String s){
        users.remove(s);
    }


}
