package bgu.spl.net.impl.Messages;

import bgu.spl.net.api.Message;
import bgu.spl.net.srv.DataBase;

import java.util.Vector;

public class StatMessage extends Message {
    private Vector<String> usernames;
    public StatMessage(String _userName,Vector<String> _usernames) {
        super((short)8);
        userName=_userName;
        usernames=_usernames;
    }
    public String response() {
        if(db.getSTAT(this)){
            return "ACK 8 "+getContent();
        }
        return "ERROR 8 ";
    }

    public String getUserName() {
        return userName;
    }


    public Vector<String> getUsernames() {
        return usernames;
    }

}
