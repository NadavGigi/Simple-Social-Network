package bgu.spl.net.impl.Messages;

import bgu.spl.net.api.Message;
import bgu.spl.net.srv.DataBase;

public class LoginMessage extends Message {
    private String pass;
    public LoginMessage(String _userName,String _pass) {
        super((short)2);
        userName=_userName;
        pass=_pass;
    }
    public String response() {
        if(db.logUserIn(userName,pass))
            return "ACK 2 LOGIN "+userName+" "+pass;
        return  "ERROR 2 ";
    }
}
