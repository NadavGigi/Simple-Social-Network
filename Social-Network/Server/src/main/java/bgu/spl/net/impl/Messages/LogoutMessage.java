package bgu.spl.net.impl.Messages;

import bgu.spl.net.api.Message;
import bgu.spl.net.srv.DataBase;

public class LogoutMessage extends Message {
    private String pass;
    public LogoutMessage(String _userName) {
        super((short)3);
        userName=_userName;
    }
    public String response() {
        if(db.logUserOut(userName))
            return "ACK<3 LOGOUT "+userName;
        return "ERROR 3";
    }
}
